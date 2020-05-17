package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.SettingGameMessage;
import it.polimi.ingsw.virtualView.FrontEnd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;


public class ServerConnection implements Runnable {
    private Socket socket;
    private Server server;
    private boolean active = true;
    private String name = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String[] challengerChoice;
    private ClientMessage clientMessage;
    private volatile boolean updateClientMessage;
    private int gameID;
    private FrontEnd frontEnd;
    private BlockingQueue<Object> messageInQueue;
    private BlockingQueue<Object> messageOutQueue;


    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.challengerChoice = null;
        this.clientMessage = null;
        this.updateClientMessage = false;
        this.gameID = -1;
        frontEnd = null;
        messageInQueue = new LinkedBlockingDeque<>();
        messageOutQueue = new LinkedBlockingDeque<>();
    }

    private synchronized boolean isActive(){
        return active;
    }

    public synchronized void send(Object message) {
        messageOutQueue.add(message);
    }

    public void read(){
        try {
            while ( active ) {
                Object newMessage = in.readObject();
                messageInQueue.add(newMessage);
            }
        } catch (SocketTimeoutException s){
            active = false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
    }

    @Override
    public void run() {
        active = true;
         try {
             //socket.setSoTimeout(6000);
             out = new ObjectOutputStream(socket.getOutputStream());
             out.flush();
             in = new ObjectInputStream(socket.getInputStream());
             send("Welcome, server ready!\n");
             sendPing();
             new Thread (this::read).start();

             while (active) {
                 Object toSend = messageOutQueue.poll();
                 while ( toSend != null ) {
                     out.reset();
                     out.writeObject(toSend);
                     out.flush();
                     toSend = messageOutQueue.poll();
                 }

                 Object message = messageInQueue.poll();
                 if ( message != null ) {
                     if (message instanceof SettingGameMessage) {
                         initialize((SettingGameMessage) message);
                     } else if (message instanceof String) {
                         setNickname((String) message);
                     } else if (message instanceof ClientMessage) {
                         clientMessage = (ClientMessage) message;
                         updateClientMessage = true;
                         notifyFrontEnd();
                     } else if (message instanceof String[]) {
                         challengerChoice = (String[]) message;
                         notifyFrontEnd();
                     } else if (message instanceof Message) {
                         if (message.equals(Message.CLOSE)) {
                             //If the gameID is -1, the player isn't playing a match yet. Else, I'll end the match he is playing
                             if (gameID != -1) {
                                 server.endGame(gameID, this);
                                 active = false;
                             } else {
                                 server.removeFromWaitingList(this);
                                 server.removeNickname(name);
                                 active = false;
                             }
                         } else if (message.equals(Message.PONG)) {
                             System.out.println("Pong from : " + socket);
                         }
                     }
                 }
             }
             Object toSend = messageOutQueue.poll();
             while ( toSend != null ) {
                 out.reset();
                 out.writeObject(toSend);
                 out.flush();
                 toSend = messageOutQueue.poll();
             }
             //controllo se la connessione cade o se il client si disconnette
         }
         catch ( SocketTimeoutException s ){
             active = false;
         }
         catch (IOException e) {
            e.printStackTrace();
         }

         finally{
             closeConnection();
         }
    }

    public void setNickname(String name){
        if ( server.existingNickname(name) ) {
            send("Invalid Nickname");
            this.name = null;
        }
        else {
            send("Nickname accepted");
            server.addNickname(name);
            this.name = name;
        }
    }

    public void initialize(SettingGameMessage settings){
        int playersInTheGame = 0;

        // Nel server crea un nuovo GameID e aspetta che i giocatori si colleghino
        // legge quanti giocatori mettere nella partita, invia come stringa il gameID del match creato
        if ( settings.isCreatingNewGame() ) {
            server.updateCurrMatch();                       //Creato il game ID aggiornato
            settings.setGameID(server.getCurrMatch());      //Passiamo al client l'ID aggiornato

            gameID = server.getCurrMatch();
            playersInTheGame = settings.getNumberOfPlayer();
            server.createMatch(gameID, playersInTheGame, this);
            send(String.valueOf(gameID));
        }
        //se il giocatore vuole partecipare ad una partita creata da un altro
        else if ( settings.isPlayingExistingMatch() ) {
            gameID = settings.getGameID();

            if (server.addPlayer(gameID, this)) {
                // checkMatch mi dice se ho raggiunto il numero di giocatori necessario
                // per iniziare la partita corrispondente al gameID considerato
                if (server.checkMatch(gameID)) {
                    server.startGame(gameID);
                    send("Beginning new match");
                } else {
                    send("Waiting for other players");
                }
            } else {
                send("Insert valid gameID");
            }
        }

        //il server lo inserisce nella waitingConnection tenendo
        // conto del numero di giocatori con cui vuole giocare
        //Il giocatore è aggiunto e controllo se è possibile lanciare una nuova partita
        else {
            playersInTheGame = settings.getNumberOfPlayer();
            server.lobby(playersInTheGame, this);
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void sendPing(){
        new Thread ( () ->{
            while( active ){
                try {
                    sleep(5000);
                    send(Message.PING);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    public String getName() {
        return name;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String[] getChallengerChoice() {
        return challengerChoice;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    public boolean isUpdateClientMessage() {
        return updateClientMessage;
    }

    public void setUpdateClientMessage(boolean updateClientMessage) {
        this.updateClientMessage = updateClientMessage;
    }

    public void setFrontEnd(FrontEnd frontEnd) {
        this.frontEnd = frontEnd;
    }

    public void notifyFrontEnd(){
        frontEnd.updating();
    }
}

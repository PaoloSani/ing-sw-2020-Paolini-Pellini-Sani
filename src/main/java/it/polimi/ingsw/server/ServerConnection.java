package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.SettingGameMessage;
import it.polimi.ingsw.model.LiteGame;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ServerConnection implements Runnable {
    private Socket socket;
    private Server server;
    private boolean active = true;
    private String name;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ServerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    public synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    public synchronized void closeConnection() {
        send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();
        System.out.println("Deregistering client...");
       //server.deregisterConnection(this);
        System.out.println("Done!");
    }

    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }
    @Override
    public void run() {
        name = null;
        int playersInTheGame = 0;
        int gameID;
        boolean validMessage = false;


        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            send("Welcome, server ready!\n");
            SettingGameMessage settings = new SettingGameMessage();
            while ( name == null ){
                settings = (SettingGameMessage) in.readObject();

                name = settings.getNickname();


                if ( server.existingNickname(name) ) {
                    send("Invalid Nickname");
                    name = null;
                }
                else {
                    send("Nickname accepted");
                    server.addNickname(name);
                }
            }

            while ( !validMessage ) {

                settings = (SettingGameMessage) in.readObject();


                if (settings.isCreatingNewGame()) {
                    validMessage = true;
                    server.updateCurrMatch();                       //Creato il game ID aggiornato
                    settings.setGameID(server.getCurrMatch());      //Passiamo al client l'ID aggiornato

                    playersInTheGame = settings.getNumberOfPlayer();
                    server.createMatch(server.getCurrMatch(), playersInTheGame, this);
                    send(String.valueOf(server.getCurrMatch()));

                    // Nel server crea un nuovo GameID e aspetta che i giocatori si colleghino
                    // legge quanti giocatori mettere nella partita
                } else if (settings.isPlayingExistingMatch()) {
                    gameID = settings.getGameID();

                    if ( server.addPlayer(gameID,this) ) {
                        validMessage = true;
                        // checkMatch mi dice se ho raggiunto il numero di giocatori per iniziare la partita corrispondente al gameid considerato
                        if ( server.checkMatch(gameID) ){
                            //TODO: thread?
                            server.startGame(gameID);
                            send("Beginning new match");
                        }
                        else {
                            send("Waiting for other players");
                        }
                    }
                    else {
                        send("Insert valid gameID");
                    }
                    //Se il giocatore viene aggiunto
                    //controllo se è possibile runnare una nuova partita

                } else {
                    server.lobby(settings.getNickname(), playersInTheGame, this);
                    validMessage = true;
                    send("Waiting other player to join the match");
                    //il server lo inserisce nella waitingConnection tenendo
                    // conto del numero di giocatori con cui vuole giocare
                }
            }
            while (true){}
            //controllo se la connessione cade o se il client si disconnette

        } catch (IOException | ClassNotFoundException e ) {
            e.printStackTrace();
        }
        finally{
            close();
        }
    }

    //todo: modificare con metodi enumeration di God
    public String[] readChallengerMessage() {
        String message = null;
        try {
            message = (String) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //restituisce un array di stringhe, separando in modo opportuno message secondo la presenza di virgole
        return (message.split(",")); 
    }

    public ClientMessage readClientMessage() {
        ClientMessage message = null;
        try {
            message = (ClientMessage) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }


    public String getName() {
        return name;
    }

}


  /*  ByteArrayOutputStream os = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(os);
        out.reset();
                out.writeObject(pIn);
                out.flush();
                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                ObjectInputStream in = new ObjectInputStream(is);
                Person pOut = (Person)in.readObject();
                System.out.println(pOut.getName());
                System.out.println(pOut.getBirthday().getTime());
                System.out.println(pOut.getAge());
*/
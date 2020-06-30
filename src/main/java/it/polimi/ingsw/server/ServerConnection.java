package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.SettingGameMessage;
import it.polimi.ingsw.util.Message;
import it.polimi.ingsw.virtualView.FrontEnd;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.Thread.sleep;


/**
 * is responsible of the communication with a client
 */
public class ServerConnection implements Runnable {
    private final Socket socket;
    private final Server server;
    /**
     * Queues for the messages
     */
    private final BlockingQueue<Object> messageInQueue;
    private final BlockingQueue<Object> messageOutQueue;

    /**
     * Active is true if the socket can receive and process messages
     */
    private boolean active = true;

    /**
     * clientIsActive is true if the timeout hasn't expired
     */
    private boolean clientIsActive = true;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String name = null;
    private String[] challengerChoice;
    private ClientMessage clientMessage;
    private boolean updateClientMessage;
    private int gameID;
    private FrontEnd frontEnd;

    /**
     * is the constructor of a ServerConnection
     * @param socket: the socket to communicate with the corresponding client
     * @param server: the server of the game
     */
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

    /**
     * sends the message to the client
     * @param message: the Object to send
     */
    public synchronized void send(Object message) {
        messageOutQueue.add(message);
    }

    /**
     * reads messages from the socket and puts them in the messageInQueue if the socket is active
     */
    public void read(){
        try {
            while (clientIsActive) {
                Object newMessage = in.readObject();
                messageInQueue.add(newMessage);
            }
        } catch(SocketException | EOFException | SocketTimeoutException s ){
            clientIsActive = false;
            messageInQueue.add(Message.CLOSE);
        } catch ( IOException | ClassNotFoundException e) {
                e.printStackTrace();
        }
    }


    /**
     * closes the socket
     */
    public synchronized void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
    }

    /**
     * starts a connection with the client and processes the messages read from the client
     */
    @Override
    public void run() {
        active = true;
        try {
             socket.setSoTimeout(7000);
             out = new ObjectOutputStream(socket.getOutputStream());
             out.flush();
             in = new ObjectInputStream(socket.getInputStream());
             out.reset();
             send("Welcome, server ready!\n");
             sendPing();
             Thread read = new Thread(this::read);
             read.start();

             while (active) {
                 Object toSend = messageOutQueue.poll();
                 while ( toSend != null && clientIsActive ) {
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
                                 server.endMatch(gameID, this);
                                 active = false;
                                 read.interrupt();
                             } else {
                                 server.removeFromWaitingList(this);
                                 server.removeNickname(name);
                                 active = false;
                                 read.interrupt();
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
             closeConnection();
        }
        catch ( SocketException s){
            clientIsActive = false;
            if (gameID != -1) {
                server.endMatch(gameID, this);
            } else {
                server.removeFromWaitingList(this);
                server.removeNickname(name);
            }
        }
        catch ( SocketTimeoutException s ){
            active = false;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * adds a new nickname to the server or sends a message of invalid nickname
     * @param name: the nickname of the client
     */
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

    /**
     * gets the information of the match, which the client wants to play.
     * @param settings : a SettingGameMessage with all the information of the current playing request
     */
    public void initialize(SettingGameMessage settings){
        int playersInTheGame = 0;

        if ( settings.isCreatingNewGame() ) {
            server.updateCurrMatch();                       //Creato il game ID aggiornato
            settings.setGameID(server.getCurrMatch());      //Passiamo al client l'ID aggiornato

            gameID = server.getCurrMatch();
            playersInTheGame = settings.getNumberOfPlayer();
            server.createMatch(gameID, playersInTheGame, this);
            send(String.valueOf(gameID));
        }

        else if ( settings.isPlayingExistingMatch() ) {
            gameID = settings.getGameID();

            if (server.addPlayer(gameID, this)) {
                if (server.checkMatch(gameID)) {
                    server.startGame(gameID);
                    send(Message.BEGIN.toString());
                } else {
                    send(Message.WAIT.toString());
                }
            } else {
                send(Message.INVALID_ID.toString());
            }
        }

        else {
            playersInTheGame = settings.getNumberOfPlayer();
            server.lobby(playersInTheGame, this);
        }
    }

    /**
     * sends a ping message every 5 seconds to verify if the client is active
     */
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

    public void setFrontEnd(FrontEnd frontEnd) {
        this.frontEnd = frontEnd;
    }

    public String[] getChallengerChoice() {
        return challengerChoice;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    /**
     * used by the FrontEnd to read a new ClientMessage
     * @return true if the client has sent a new ClientMessage
     */
    public boolean isUpdatingClientMessage() {
        return updateClientMessage;
    }

    public void setUpdateClientMessage(boolean updateClientMessage) {
        this.updateClientMessage = updateClientMessage;
    }

    public void notifyFrontEnd(){
        frontEnd.wakeUpFrontEnd();
    }
}

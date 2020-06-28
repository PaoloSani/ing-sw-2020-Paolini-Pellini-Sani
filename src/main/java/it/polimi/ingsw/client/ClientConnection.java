package it.polimi.ingsw.client;

import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.util.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * It represents the client itself during his match
 */

public class ClientConnection implements Runnable{

    /**
     * It is the IP address of the server
     */
    private String ip;

    /**
     * It is the port of the server
     */
    private int port;

    /**
     * It is the stream which read input objects
     */
    private ObjectInputStream in;

    /**
     * It is the stream which write output objects
     */
    private ObjectOutputStream out;

    /**
     * It is the socket of the client
     */
    private Socket socket;

    /**
     * It manages all the messages coming and going on the internet
     */
    private MessageHandler messageHandler;

    /**
     * It contains all the objects which have been received from the internet
     */
    private BlockingQueue<Object> messageInQueue;

    /**
     * It contains all the objects which must be sent on the internet
     */
    private BlockingQueue<Object> messageOutQueue;

    /**
     * It tells if the client is still active or not
     */
    private boolean active = true;

    /**
     * It tells if the server is still active or not
     */
    private boolean serverIsActive = true;

    public ClientConnection(String ip, int port, MessageHandler messageHandler){
        this.ip = ip;
        this.port = port;
        this.messageHandler = messageHandler;
        messageInQueue = new LinkedBlockingDeque<>();
        messageOutQueue = new LinkedBlockingDeque<>();
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    /**
     * It adds an object to messageOutQueue, which will be sent
     * @param message is the object to send
     */
    public synchronized void send(Object message) {
        messageOutQueue.add(message);
    }

    /**
     * It adds an object to messageInQueue.
     */
    public void read(){
        try {
            while ( serverIsActive ) {
                Object newMessage = in.readObject();
                if ( newMessage instanceof Message && Message.PING.equals(newMessage)){
                    send(Message.PONG);
                }
                else {
                    if ( newMessage instanceof Message && newMessage.equals(Message.CLOSE) ||
                            ( newMessage instanceof String && ((String) newMessage).contains("You have been")) ){
                        serverIsActive = false;
                    }
                    messageInQueue.add(newMessage);
                }
            }
        }
        catch( SocketException | EOFException | SocketTimeoutException s ){
            serverIsActive = false;
            active = false;
        }
        catch ( IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            System.out.println("Connection established");
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            Thread read = new Thread(this::read);
            read.start();

            while ( active || messageInQueue.peek() != null ) {
                Object toSend = messageOutQueue.poll();
                while ( toSend != null && serverIsActive ) {
                    out.reset();
                    out.writeObject(toSend);
                    out.flush();
                    toSend = messageOutQueue.poll();
                }

                if ( ( !messageHandler.isStringRead() && !messageHandler.isLGRead()) ) {
                    Object message = messageInQueue.poll();
                    if (message != null) {
                        if ( message instanceof String ) {
                            if ( ((String) message).contains("Ending") ){
                                if ( messageHandler.getGuiToNotify() == null ) {
                                    System.out.println("  " + message);
                                }
                                else{
                                    messageHandler.setMessage( (String)message);
                                }
                            }
                            else if ( ((String) message).contains("You have been") ){
                                active = false;
                                messageHandler.setMessage((String)message);
                            }
                            else {
                                messageHandler.setStringRead(true);
                                messageHandler.setMessage((String) message);
                            }
                        } else if (message instanceof SerializableLiteGame) {
                            messageHandler.setLGRead(true);
                            messageHandler.setLiteGameFromServer((SerializableLiteGame) message);
                        } else if (message instanceof Message) {
                            if (message.equals(Message.CLOSE)) {
                                active = false;
                            }
                        }
                    }
                }
            }
        }
        catch ( IOException e) {
            messageHandler.setStringRead(true);
            messageHandler.setMessage("Error!");
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
                messageHandler.setStringRead(true);
                messageHandler.setMessage("Error!");
            }
        }


    }


    /**
     * It closes the socket and every stream.
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
        System.out.println("  End of the game.");
    }
}
package it.polimi.ingsw.client;

import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.server.Message;

import java.io.*;
import java.net.Socket;


public class ClientConnection implements Runnable{
    private String ip;
    private int port;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private Socket socket;
    private MessageHandler messageHandler;

    public ClientConnection(String ip, int port, MessageHandler messageHandler){
        this.ip = ip;
        this.port = port;
        this.messageHandler = messageHandler;
    }

    private boolean active = true; /////////////

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            System.out.println("Connection established");
            socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            while (active) {
                Object message;
                message = socketIn.readObject();

                if ( message instanceof String ) {
                    messageHandler.setStringRead(true);
                    messageHandler.setMessage((String) message);
                }
                else if ( message instanceof SerializableLiteGame ) {
                    messageHandler.setStringRead(false);
                    messageHandler.setLiteGameFromServer( (SerializableLiteGame) message);
                }
                else if (message instanceof Message) {
                    if ( message.equals(Message.PING)){
                        send(Message.PONG);
                    }
                    else if (message.equals(Message.CLOSE)) {
                        active = false;
                    }
                }
            }
        }
        catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public void closeConnection() throws IOException {
        setActive(false);
        socketIn.close();
        socketOut.close();
        socket.close();
    }

    public synchronized void send(Object message) {
        try {
            socketOut.reset();
            socketOut.writeObject(message);
            socketOut.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    public String readString(){
        String message = null;
        try {
            message = (String) socketIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }
}
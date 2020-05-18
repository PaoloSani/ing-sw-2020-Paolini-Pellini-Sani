package it.polimi.ingsw.client;

import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.server.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


public class ClientConnection implements Runnable{
    private String ip;
    private int port;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private MessageHandler messageHandler;
    private BlockingQueue<Object> messageInQueue;
    private BlockingQueue<Object> messageOutQueue;

    public ClientConnection(String ip, int port, MessageHandler messageHandler){
        this.ip = ip;
        this.port = port;
        this.messageHandler = messageHandler;
        messageInQueue = new LinkedBlockingDeque<>();
        messageOutQueue = new LinkedBlockingDeque<>();
    }

    private boolean active = true; /////////////

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    public synchronized void send(Object message) {
        messageOutQueue.add(message);
    }

    public void read(){
        try {
            while ( active ) {
                Object newMessage = in.readObject();
                if ( newMessage instanceof Message && Message.PING.equals(newMessage)){
                    send(Message.PONG);
                }
                else {
                    messageInQueue.add(newMessage);
                }
            }
        } catch (SocketTimeoutException s){
            active = false;
        } catch (IOException | ClassNotFoundException e) {
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
                while ( toSend != null ) {
                    out.reset();
                    out.writeObject(toSend);
                    out.flush();
                    toSend = messageOutQueue.poll();
                }

                if ( ( !messageHandler.isStringRead() && !messageHandler.isLGRead()) ) {
                    Object message = messageInQueue.poll();
                    if (message != null) {
                        if ( message instanceof String ) {
                            messageHandler.setStringRead(true);
                            messageHandler.setMessage((String) message);
                        } else if (message instanceof SerializableLiteGame) {
                            messageHandler.setLGRead(true);
                            messageHandler.setLiteGameFromServer((SerializableLiteGame) message);
                        } else if (message instanceof Message) {
                            if (message.equals(Message.CLOSE)) {
                                active = false;
                                read.interrupt();
                            }
                        }
                    }
                }
            }
        }
        catch ( IOException e) {
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
        in.close();
        out.close();
        socket.close();
    }

    public String readString(){
        String message = null;
        try {
            message = (String) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }
}
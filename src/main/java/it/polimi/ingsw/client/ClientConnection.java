package it.polimi.ingsw.client;

import it.polimi.ingsw.model.SerializableLiteGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientConnection {
    private String ip;
    private int port;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private Socket socket;


    public ClientConnection(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    private boolean active = true; /////////////

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }


    public void connect() throws IOException {
        socket = new Socket(ip, port);
        System.out.println("Connection established");
        socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new ObjectOutputStream(socket.getOutputStream());
    }


    public void closeConnection() throws IOException {
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

    public SerializableLiteGame readLiteGame(){
       SerializableLiteGame message = null;
        try {
            message = (SerializableLiteGame) socketIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }
}
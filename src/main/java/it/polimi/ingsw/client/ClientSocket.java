package it.polimi.ingsw.client;

import it.polimi.ingsw.model.LiteGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientSocket {
    private String ip;
    private int port;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private Socket socket;


    public ClientSocket(String ip, int port){
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

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        if(inputObject instanceof String){
                            System.out.println((String)inputObject);
                        } else if (inputObject instanceof LiteGame){
                            //liteGame = (LiteGame) inputObject;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (Exception e){ //serve ?
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        String inputLine = stdin.nextLine();
                        socketOut.println(inputLine);
                        socketOut.flush();
                    }
                }catch(Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public void connect() throws IOException {
        Socket socket = new Socket(ip, port);
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

    public LiteGame readLiteGame(){
        LiteGame message = null;
        try {
            message = (LiteGame) socketIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }
}
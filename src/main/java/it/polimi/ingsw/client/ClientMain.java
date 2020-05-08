package it.polimi.ingsw.client;

import java.io.IOException;

public class ClientMain {

    public static void main(String[] args){
        ClientSocket clientSocket = new ClientSocket("127.0.0.1", 12345);
        try {
            clientSocket.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println(clientSocket.readString());
        }
        OpeningMirror openingMirror = new OpeningMirror();
        openingMirror.userInterfaceSetup();
    }

}

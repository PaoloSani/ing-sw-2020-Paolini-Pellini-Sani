package it.polimi.ingsw.client;

import it.polimi.ingsw.CLI.OpeningMirror;

import java.io.IOException;

public class ClientMain {

    public static void main(String[] args){
        ClientSocket clientSocket = new ClientSocket("127.0.0.1", 12345);

        OpeningMirror openingMirror = new OpeningMirror();
        openingMirror.userInterfaceSetup();

        try {
            clientSocket.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println(clientSocket.readString());
        }
    }

}

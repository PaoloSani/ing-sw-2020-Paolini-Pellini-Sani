package it.polimi.ingsw.client;

import java.io.IOException;

public class ClientMain {

    public static void main(String[] args){
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345);

        OpeningMirror openingMirror = new OpeningMirror();
        openingMirror.userInterfaceSetup();

    }

}

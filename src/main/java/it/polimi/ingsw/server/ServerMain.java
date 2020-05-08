package it.polimi.ingsw.server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args){
        Server server;
        try {
             server = new Server();
             server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package it.polimi.ingsw.client;

import it.polimi.ingsw.server.Server;
import org.junit.Test;
import org.junit.Before;

import java.io.IOException;

public class ClientConnectionTest {

    ClientConnection clientConnection;
    Server server;

    @Before
    public void setUp() throws IOException {
        clientConnection = new ClientConnection("127.0.0.1", 12345);
        server = new Server();

    }

    @Test
    public void socketTest() throws IOException {
        server.run();
        clientConnection.connect();
        System.out.println(clientConnection.readString());

    }


}
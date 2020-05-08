package it.polimi.ingsw.client;

import it.polimi.ingsw.server.Server;
import org.junit.Test;
import org.junit.Before;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ClientSocketTest {

    ClientSocket clientSocket;
    Server server;

    @Before
    public void setUp() throws IOException {
        clientSocket = new ClientSocket("127.0.0.1", 12345);
        server = new Server();

    }

    @Test
    public void socketTest() throws IOException {
        server.run();
        clientSocket.connect();
        System.out.println(clientSocket.readString());

    }


}
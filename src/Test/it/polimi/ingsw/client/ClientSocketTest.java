package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.SocketClientConnection;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ClientSocketTest {

    ClientSocket clientSocket;
    Server server;

    @BeforeEach
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
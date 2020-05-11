package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.BackEnd;
import it.polimi.ingsw.virtualView.FrontEnd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int PORT = 4700;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private ExecutorService nowPlaying = Executors.newFixedThreadPool(128);

    private Map<String, ServerConnection> waitingConnection2Players = new HashMap<>();
    private Map<String, ServerConnection> waitingConnection3Players = new HashMap<>();

    private Map<Integer, List<ServerConnection>> playingConnection2Players = new HashMap<>();
    private Map<Integer, List<ServerConnection>> playingConnection3Players = new HashMap<>();
    private List<String> nicknames = new ArrayList<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.currMatch = 0;
    }

    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(newSocket, this);
                executor.submit(serverConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

    //currMatch si riferisce all'ultima partita che è stata creata
    private int currMatch;

    public int getCurrMatch() {
        return currMatch;
    }

    public synchronized void updateCurrMatch() { currMatch++; }

    public void resetCurrMatch() { currMatch = 0; }

  /*  public synchronized void deregisterConnection(Socket socket) {
        ClientConnection opponent = playingConnection.get(c);
        if(opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(c);
        playingConnection.remove(opponent);
        Iterator<String> iterator = waitingConnection.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingConnection.get(iterator.next())==c){
                iterator.remove();
            }
        }
    }*/

    public synchronized void lobby(String name, int numberOfPlayers, ServerConnection client){
        if (numberOfPlayers == 2) {
            waitingConnection2Players.put(name,client);
            if (waitingConnection2Players.size() == 2){
                updateCurrMatch();
                List<String> keys = new ArrayList<>(waitingConnection2Players.keySet());
                List<ServerConnection> list = new ArrayList<>();
                list.add(waitingConnection2Players.get(keys.get(0)));
                list.add(waitingConnection2Players.get(keys.get(1)));
                playingConnection2Players.put(currMatch, list);
                waitingConnection2Players.clear();
                startGame(currMatch);

            }
            else {
                client.send("Waiting other player to join the match");
            }
        }
        else if (numberOfPlayers == 3) {
            waitingConnection3Players.put(name,client);
            if (waitingConnection3Players.size() == 3){
                updateCurrMatch();
                List<String> keys = new ArrayList<>(waitingConnection3Players.keySet());
                List<ServerConnection> list = new ArrayList<>();
                list.add(waitingConnection3Players.get(keys.get(0)));
                list.add(waitingConnection3Players.get(keys.get(1)));
                list.add(waitingConnection3Players.get(keys.get(2)));
                playingConnection3Players.put(currMatch, list);
                waitingConnection3Players.clear();
                startGame(currMatch);
            }
        }


    }


    /*Wait for another player
    public synchronized void lobby(ClientConnection c, String name){
        waitingConnection.put(name, c);
        if (waitingConnection.size() == 2) {
            List<String> keys = new ArrayList<>(waitingConnection.keySet());
            ClientConnection c1 = waitingConnection.get(keys.get(0));
            ClientConnection c2 = waitingConnection.get(keys.get(1));
            Player player1 = new Player(keys.get(0), Cell.X);
            Player player2 = new Player(keys.get(0), Cell.O);
            View player1View = new RemoteView(player1, keys.get(1), c1);
            View player2View = new RemoteView(player2, keys.get(0), c2);
            Model model = new Model();
            Controller controller = new Controller(model);
            model.addObserver(player1View);
            model.addObserver(player2View);
            player1View.addObserver(controller);
            player2View.addObserver(controller);
            playingConnection.put(c1, c2);
            playingConnection.put(c2, c1);
            waitingConnection.clear();
            c1.asyncSend(model.getBoardCopy());
            c2.asyncSend(model.getBoardCopy());
            if(model.isPlayerTurn(player1)){
                c1.asyncSend(gameMessage.moveMessage);
                c2.asyncSend(gameMessage.waitMessage);
            } else {
                c2.asyncSend(gameMessage.moveMessage);
                c1.asyncSend(gameMessage.waitMessage);
            }

        }
    }

   */

    public boolean existingNickname(String nickname) {
        return nicknames.contains(nickname);
    }

    public void createMatch(int gameID , int numberOfPlayers, ServerConnection challenger) {
        List<ServerConnection> list = new ArrayList<>();
        list.add(challenger);
        if ( numberOfPlayers == 2){
            playingConnection2Players.put(gameID, list);
        }
        else if (numberOfPlayers == 3){
            playingConnection3Players.put(gameID, list);
        }
    }

    public synchronized boolean addPlayer(int gameID, ServerConnection player){
        if (playingConnection2Players.containsKey(gameID)) {
            List<ServerConnection> list = playingConnection2Players.get(gameID);
            if (list.size() < 2){             //TODO: Un terzo giocatore non può collegarsi, la sua connessione si chiude
                list.add(player);
                return true;
            }
        } else if (playingConnection3Players.containsKey(gameID)){
            List<ServerConnection> list = playingConnection3Players.get(gameID);
            if (list.size() < 3){             //Todo: Un quarto giocatore non può collegarsi, la sua connessione si chiude
                list.add(player);
                return true;
            }
        }
        return false;
    }

    public void addNickname(String name){
        nicknames.add(name);
    }

    //Fa partire la partita gameID
    public void startGame(int gameID) {
        FrontEnd frontEnd;
        BackEnd backEnd = new BackEnd();

        if (playingConnection2Players.containsKey(gameID)){
            List<ServerConnection> list = playingConnection2Players.get(gameID);
            frontEnd = new FrontEnd(this, list.get(0), list.get(1), gameID, backEnd);
        } else {
            List<ServerConnection> list = playingConnection3Players.get(gameID);
            frontEnd = new FrontEnd(this, list.get(0), list.get(1), list.get(2), gameID, backEnd);
        }

        nowPlaying.submit(frontEnd);
    }


    // TODO: controlla possibili miglioramenti per due giocatori
    // Controlla se si può avviare la partita gameID
    public boolean checkMatch(int gameID) {
        if ( playingConnection2Players.containsKey(gameID) && playingConnection2Players.get(gameID).size() == 2 )
            return true;
        else return ( playingConnection3Players.containsKey(gameID) && playingConnection3Players.get(gameID).size() == 3 );
    }

    public void endGame(int gameID) {
        if ( playingConnection2Players.containsKey(gameID) ){
            playingConnection2Players.remove(gameID);
        }
        else playingConnection3Players.remove(gameID);
    }

    public void removeNicname(String name) {
        nicknames.remove(name);
    }
}

/*
    public synchronized void deregisterConnection(ClientConnection c) {
        ClientConnection opponent = playingConnection.get(c);
        if(opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(c);
        playingConnection.remove(opponent);
        Iterator<String> iterator = waitingConnection.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingConnection.get(iterator.next())==c){
                iterator.remove();
            }
        }
    }
    //Wait for another player
    public synchronized void lobby(ClientConnection c, String name){
        waitingConnection.put(name, c);
        if (waitingConnection.size() == 2) {
            List<String> keys = new ArrayList<>(waitingConnection.keySet());
            ClientConnection c1 = waitingConnection.get(keys.get(0));
            ClientConnection c2 = waitingConnection.get(keys.get(1));
            Player player1 = new Player(keys.get(0), Cell.X);
            Player player2 = new Player(keys.get(0), Cell.O);
            View player1View = new RemoteView(player1, keys.get(1), c1);
            View player2View = new RemoteView(player2, keys.get(0), c2);
            Model model = new Model();
            Controller controller = new Controller(model);
            model.addObserver(player1View);
            model.addObserver(player2View);
            player1View.addObserver(controller);
            player2View.addObserver(controller);
            playingConnection.put(c1, c2);
            playingConnection.put(c2, c1);
            waitingConnection.clear();
            c1.asyncSend(model.getBoardCopy());
            c2.asyncSend(model.getBoardCopy());
            if(model.isPlayerTurn(player1)){
                c1.asyncSend(gameMessage.moveMessage);
                c2.asyncSend(gameMessage.waitMessage);
            } else {
                c2.asyncSend(gameMessage.moveMessage);
                c1.asyncSend(gameMessage.waitMessage);
            }

        }
    }
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }
    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }
}

}
*/
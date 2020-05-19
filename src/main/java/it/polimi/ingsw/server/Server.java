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
    public static final int PORT = 4702;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private ExecutorService nowPlaying = Executors.newFixedThreadPool(128);

    private List<ServerConnection> waitingConnection2Players = new ArrayList<>();
    private List<ServerConnection> waitingConnection3Players = new ArrayList<>();

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

    public synchronized void lobby(int numberOfPlayers, ServerConnection client){
        if (numberOfPlayers == 2) {
            waitingConnection2Players.add(client);
            if (waitingConnection2Players.size() == 2){
                updateCurrMatch();
                List<ServerConnection> list = new ArrayList<>();
                list.add(waitingConnection2Players.get(0));
                list.add(waitingConnection2Players.get(1));
                playingConnection2Players.put(currMatch, list);
                waitingConnection2Players.clear();
                client.send("Starting new game.");
                startGame(currMatch);
            }
            else {
                client.send("Waiting for an other player to join the match. You are the challenger.");
            }
        }
        else if (numberOfPlayers == 3) {
            waitingConnection3Players.add(client);
            if (waitingConnection3Players.size() == 3){
                updateCurrMatch();
                List<ServerConnection> list = new ArrayList<>();
                list.add(waitingConnection3Players.get(0));
                list.add(waitingConnection3Players.get(1));
                list.add(waitingConnection3Players.get(2));
                playingConnection3Players.put(currMatch, list);
                waitingConnection3Players.clear();
                client.send("Starting new game.");
                startGame(currMatch);
            }
            else {
                String message = "Waiting other players to join the match.";
                if ( waitingConnection3Players.size() == 1 ){
                    message = message.concat(" You are the challenger.");
                }
                client.send(message);
            }
        }
    }

    public synchronized boolean existingNickname(String nickname) {
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
            list.get(0).setFrontEnd(frontEnd);
            list.get(1).setFrontEnd(frontEnd);
        } else {
            List<ServerConnection> list = playingConnection3Players.get(gameID);
            frontEnd = new FrontEnd(this, list.get(0), list.get(1), list.get(2), gameID, backEnd);
            list.get(0).setFrontEnd(frontEnd);
            list.get(1).setFrontEnd(frontEnd);
            list.get(2).setFrontEnd(frontEnd);
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



    public void endGame(int gameID, ServerConnection closingPlayer) {
        if ( playingConnection2Players.containsKey(gameID) ){
            for ( ServerConnection s : playingConnection2Players.get(gameID) ){
                if ( closingPlayer != null ) {
                    s.send("Ending game: " + closingPlayer.getName() + " has left");
                }
                if ( s != closingPlayer ) {
                    s.send(Message.CLOSE);
                    s.setActive(false);
                }
                removeNickname(s.getName());
            }
            playingConnection2Players.remove(gameID);
        }
        else {
            for ( ServerConnection s : playingConnection3Players.get(gameID) ){
                if ( closingPlayer != null ) {
                    s.send("Ending game: " + closingPlayer.getName() + " has left");
                }
                if ( s != closingPlayer ) {
                    s.send(Message.CLOSE);
                    s.setActive(false);
                }
                removeNickname(s.getName());
            }
            playingConnection3Players.remove(gameID);
        }
    }

    public void removeFromWaitingList(ServerConnection toRemove){
        if ( waitingConnection2Players.contains(toRemove)){
            waitingConnection2Players.remove(toRemove);
        }
        else if ( waitingConnection3Players.contains(toRemove) ){
            waitingConnection3Players.remove(toRemove);
        }
    }

    public void removeNickname(String nameToRemove) {
        //Firstly, I must check the player is not waiting in a list. In that case I'll remove him
        nicknames.remove(nameToRemove);
    }
}

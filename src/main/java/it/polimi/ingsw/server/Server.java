package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.BackEnd;
import it.polimi.ingsw.util.Message;
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
import static java.lang.Thread.sleep;

/**
 *  The main server class
 */
public class Server {
    public final int PORT = 4702;
    private final ServerSocket serverSocket;

    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final ExecutorService nowPlaying = Executors.newFixedThreadPool(128);

    /**
     * Waiting list for new players
     */
    private final List<ServerConnection> waitingConnection2Players = new ArrayList<>();
    private final List<ServerConnection> waitingConnection3Players = new ArrayList<>();

    /**
     * Maps containing the players in a match and the match identifier
     */
    private final Map<Integer, List<ServerConnection>> playingConnection2Players = new HashMap<>();
    private final Map<Integer, List<ServerConnection>> playingConnection3Players = new HashMap<>();

    /**
     * list of the nicknames of the players now playing or waiting to play
     */
    private final List<String> nicknames = new ArrayList<>();
    /**
     * Gradually incremented, is the ID of the last match, which has been created
     */
    private int currMatch;

    /**
     * Is the constructor of the Server
     * @throws IOException if socket cannot be created
     */
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.currMatch = 0;
    }

    /**
     * Accepts new players connecting to the server and creates a connection to speak and deal with them
     */
    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(newSocket, this);
                executor.submit(serverConnection);
                printNicknames();
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

    /**
     * Adds a client to a corrisponding waiting list according to the number of players he wants to play with
     * @param numberOfPlayers : chosen by the client (2 or 3)
     * @param client : ServerConnection of the client
     */
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

    /**
     * Creates a new match
     * @param gameID: match identifier
     * @param numberOfPlayers: indicates the number of players of the match
     * @param challenger: ServerConnection of the client, which creates the new match
     */
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

    /**
     * Adds a player to an existing match
     * @param gameID: the identifier of the match
     * @param player: Server connection of the player
     * @return : return true if the client has been correctly added to the match
     */
    public synchronized boolean addPlayer(int gameID, ServerConnection player){
        if (playingConnection2Players.containsKey(gameID)) {
            List<ServerConnection> list = playingConnection2Players.get(gameID);
            if (list.size() < 2){
                list.add(player);
                return true;
            }
        } else if (playingConnection3Players.containsKey(gameID)){
            List<ServerConnection> list = playingConnection3Players.get(gameID);
            if (list.size() < 3){
                list.add(player);
                return true;
            }
        }
        return false;
    }


    public void addNickname(String name){
        nicknames.add(name);
    }

    public synchronized boolean existingNickname(String nickname) {
        return nicknames.contains(nickname);
    }
    public void removeNickname(String nameToRemove) {
        nicknames.remove(nameToRemove);
    }



    /**
     * Starts a match
     * @param gameID : match identifier of the match to start
     */
    public void startGame(int gameID) {
        /**
         * Every match has a corresponding FrontEnd and Backend, which deal with
         * turns scheduling and communicate with the client after the game has started
         */
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



    /**
     * Checks if a match has reached the number of players it needs to start
     * @param gameID : match to check
     * @return : true if the match corresponding to the gameID is ready to start
     */
    public boolean checkMatch(int gameID) {
        if ( playingConnection2Players.containsKey(gameID) && playingConnection2Players.get(gameID).size() == 2 )
            return true;
        else return ( playingConnection3Players.containsKey(gameID) && playingConnection3Players.get(gameID).size() == 3 );
    }

    /**
     * Closes a match and sends a closing message to all the clients of the match
     * @param gameID : match identifier
     * @param closingPlayer : null if there is a winner, else is the player who has left the game
     */
    public void endMatch(int gameID, ServerConnection closingPlayer) {
        if ( playingConnection2Players.containsKey(gameID) ){
            for ( ServerConnection s : playingConnection2Players.get(gameID) ){
                s.setGameID(-1);
                if ( closingPlayer != null ) {
                    s.send("Ending game: " + closingPlayer.getName() + " has left");
                }
                if ( s != closingPlayer ) {
                    s.send(Message.CLOSE);
                }
                removeNickname(s.getName());
            }
            playingConnection2Players.remove(gameID);
        }
        else {
            for ( ServerConnection s : playingConnection3Players.get(gameID) ){
                s.setGameID(-1);
                if ( closingPlayer != null ) {
                    s.send("Ending game: " + closingPlayer.getName() + " has left");
                }
                if ( s != closingPlayer ) {
                    s.send(Message.CLOSE);
                }
                removeNickname(s.getName());
            }
            playingConnection3Players.remove(gameID);
        }
    }

    /**
     * Removes a client from a waiting list if it ends its connection
     * @param toRemove : ServerConnection of the client toRemove
     */
    public void removeFromWaitingList(ServerConnection toRemove){
        if ( waitingConnection2Players.contains(toRemove)){
            waitingConnection2Players.remove(toRemove);
        }
        else if ( waitingConnection3Players.contains(toRemove) ){
            waitingConnection3Players.remove(toRemove);
        }
    }


    /**
     * Removes a player from a match, removes the nickname from the list of nicknames,
     * resets the gameID to -1, which stands for invalid match
     * @param gameID : match identifier
     * @param toRemove : ServerConnection of the client to remove
     */
    public void removePlayerFromMatch(int gameID, ServerConnection toRemove ){
        toRemove.setGameID(-1);
        if ( playingConnection2Players.containsKey(gameID) ){
            playingConnection2Players.get(gameID).remove(toRemove);
        }
        else if ( playingConnection3Players.containsKey(gameID) ){
            playingConnection3Players.get(gameID).remove(toRemove);
        }
        removeNickname(toRemove.getName());
    }

    /**
     * Every 5 seconds, prints the nicknames of the players connected to the server
     */
    public void printNicknames(){
        new Thread ( () ->{
            while( true ){
                try {
                    sleep(5000);
                    for ( String name : nicknames ){
                        System.out.print(name + "\t");
                    }
                    if (nicknames.size() > 0 ) System.out.println();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    /**
     * @return : the ID if the last match created
     */
    public int getCurrMatch() {
        return currMatch;
    }

    public synchronized void updateCurrMatch() { currMatch++; }
}

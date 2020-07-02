package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.BackEnd;
import it.polimi.ingsw.util.Message;
import it.polimi.ingsw.virtualView.FrontEnd;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Server is the main server class
 */
public class Server {
    public final int PORT;
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
        String filename = "/server-settings.txt";

        BufferedReader settings = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));

        String port = settings.readLine();
        port = settings.readLine();
        port = port.replaceAll("\\s+","");
        port = port.split(":")[1];
        this.PORT = Integer.parseInt(port);
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
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

    /**
     * Adds a client to a corrisponding waiting list according to the number of players he wants to play with
     * @param numberOfPlayers chosen by the client (2 or 3)
     * @param client ServerConnection of the client
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
     * @param gameID match identifier
     * @param numberOfPlayers indicates the number of players of the match
     * @param challenger ServerConnection of the client, which creates the new match
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

        System.out.println( challenger.getName() + " has created a new match with code: " + gameID +".\n");
    }

    /**
     * Adds a player to an existing match
     * @param gameID the identifier of the match
     * @param player Server connection of the player
     * @return return true if the client has been correctly added to the match
     */
    public synchronized boolean addPlayer(int gameID, ServerConnection player){
        if (playingConnection2Players.containsKey(gameID)) {
            List<ServerConnection> list = playingConnection2Players.get(gameID);
            if (list.size() < 2){
                list.add(player);
                System.out.println( player.getName() + " has joined the match with code: " + gameID + ".\n" );
                return true;
            }
        } else if (playingConnection3Players.containsKey(gameID)){
            List<ServerConnection> list = playingConnection3Players.get(gameID);
            if (list.size() < 3){
                list.add(player);
                System.out.println( player.getName() + " has joined the match with code: " + gameID + ".\n" );
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new nickname to the nicknames' list
     * @param name nickname to add
     */
    public void addNickname(String name){
        nicknames.add(name);
        System.out.println(name + " is online!\n");
    }

    /**
     * Checks if the nickname is already used on server
     * @param nickname nickname to check
     * @return true if the nickname is already used
     */
    public synchronized boolean existingNickname(String nickname) {
        return nicknames.contains(nickname);
    }

    /**
     * Removes the nickname from the nickname list
     * @param nameToRemove the name to remove
     */
    public void removeNickname(String nameToRemove) {
        if (nicknames.contains(nameToRemove)){
            nicknames.remove(nameToRemove);
            System.out.println(nameToRemove + " has left!\n");
        }
    }



    /**
     * Starts a match
     * @param gameID match identifier of the match to start
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
            System.out.print("A new match has started with code: " + gameID + ".\nPlayers: ");
            list.stream().map(x -> x.getName() + "\t").forEach(System.out::print);
            System.out.println("\n");
        } else {
            List<ServerConnection> list = playingConnection3Players.get(gameID);
            frontEnd = new FrontEnd(this, list.get(0), list.get(1), list.get(2), gameID, backEnd);
            list.get(0).setFrontEnd(frontEnd);
            list.get(1).setFrontEnd(frontEnd);
            list.get(2).setFrontEnd(frontEnd);
            System.out.print("A new match has started with code: " + gameID + ".\nPlayers: ");
            list.stream().map(x -> x.getName() + "\t").forEach(System.out::print);
            System.out.println("\n");
        }

        nowPlaying.submit(frontEnd);
    }



    /**
     * Checks if a match has reached the number of players it needs to start
     * @param gameID match to check
     * @return true if the match corresponding to the gameID is ready to start
     */
    public boolean checkMatch(int gameID) {
        if ( playingConnection2Players.containsKey(gameID) && playingConnection2Players.get(gameID).size() == 2 )
            return true;
        else return ( playingConnection3Players.containsKey(gameID) && playingConnection3Players.get(gameID).size() == 3 );
    }

    /**
     * Closes a match and sends a closing message to all the clients of the match
     * @param gameID match identifier
     * @param closingPlayer null if there is a winner, else is the player who has left the game
     */
    public void endMatch(int gameID, ServerConnection closingPlayer) {

        System.out.println("Match with code " + gameID + " has ended.\n");
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
        else if ( playingConnection3Players.containsKey(gameID) ) {
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
     * @param toRemove ServerConnection of the client toRemove
     */
    public void removeFromWaitingList(ServerConnection toRemove){
        if ( waitingConnection2Players.contains(toRemove)){
            waitingConnection2Players.remove(toRemove);
            removeNickname(toRemove.getName());
        }
        else if ( waitingConnection3Players.contains(toRemove) ){
            waitingConnection3Players.remove(toRemove);
            removeNickname(toRemove.getName());
        }
        else removeNickname(toRemove.getName());
    }


    /**
     * Removes a player from a match, removes the nickname from the list of nicknames,
     * resets the gameID to -1, which stands for invalid match
     * @param gameID match identifier
     * @param toRemove ServerConnection of the client to remove
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
     * @return the ID if the last match created
     */
    public int getCurrMatch() {
        return currMatch;
    }

    /**
     * Updates the currMatch ID
     */
    public synchronized void updateCurrMatch() { currMatch++; }
}

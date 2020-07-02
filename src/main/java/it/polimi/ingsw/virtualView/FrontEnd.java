package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.controller.BackEnd;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.LiteGame;
import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.ServerConnection;
import it.polimi.ingsw.util.Observer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FrontEnd is a virtual view, which manages the turns of the match and notifies the BackEnd
 */
public class FrontEnd implements Observer<LiteGame>,Runnable {
    private Server server;
    private BackEnd backEnd;
    private ServerConnection client1;
    private ServerConnection client2;
    private ServerConnection client3;
    private ServerConnection currClient;
    private ClientMessage clientMessage;
    private LiteGame liteGame;
    private final GameMessage gameMessage;
    private boolean updateModel = false;
    private boolean endOfTheGame = false;
    private boolean playerRemoved;
    private int gameID;

    /**
     * 2 players FrontEnd constructor
     * @param server the server of the game
     * @param client1 first client, which is the challenger, and will play as last
     * @param client2 second client
     * @param gameID match identifier
     * @param backEnd backEnd of the game
     */
    public FrontEnd(Server server, ServerConnection client1, ServerConnection client2, int gameID, BackEnd backEnd) {
        this.server = server;
        this.client1 = client1;
        this.client2 = client2;
        this.client3 = null;
        this.currClient = client2;      //Il primo giocatore a iniziare è il numero 2
        this.gameID = gameID;
        this.backEnd = backEnd;
        this.gameMessage = new GameMessage(this);
        this.playerRemoved = false;
    }

    /**
     * 3 players FrontEnd constructor
     * @param server the server of the game
     * @param client1 first client, which is the challenger, and will play as last
     * @param client2 second client
     * @param client3 third client
     * @param gameID match identifier
     * @param backEnd backend of the game
     */
    public FrontEnd(Server server, ServerConnection client1, ServerConnection client2, ServerConnection client3, int gameID, BackEnd backEnd) {
        this.server = server;
        this.client1 = client1;
        this.client2 = client2;
        this.client3 = client3;
        this.currClient = client2;      //Il primo giocatore a iniziare è il numero 2
        this.gameID = gameID;
        this.backEnd = backEnd;
        this.gameMessage = new GameMessage(this);
        this.playerRemoved = false;
    }


    /**
     * Executes the game flow, starting with the second client and finishing with the first
     */
    @Override
    public void run(){
        ServerConnection[] clients = new ServerConnection[]{client2, client3, client1};

        for( ServerConnection c : clients ) {
            if ( c != null ){
                if ( client3 == null ){
                    c.send("Game has started with code:" + gameID + ".\n" + "Current players are " +
                            client1.getName() + ", " + client2.getName());
                }
                else {
                    c.send("Game has started with code:" + gameID + ".\n" + "Current players are " +
                            client1.getName() + ", " + client2.getName() + ", " + client3.getName());
                }
                c.setGameID(gameID);
            }
        }

        String[] gods = readChallengerMessage();

        for (int i=0; i<2 && currClient != client1; i++){
            currClient.send(Arrays.toString(gods) );
            clientMessage = readClientMessage();
            if (i == 0) {
                gameMessage.setGod2(clientMessage.getGod());
                gameMessage.setName2(clientMessage.getName());
            } else if (i == 1) {
                gameMessage.setGod3(clientMessage.getGod());
                gameMessage.setName3(clientMessage.getName());
            }
            List<String> list = new ArrayList<String>(Arrays.asList(gods));
            list.remove( clientMessage.getGod().name());
            gods = list.toArray(new String[0]);
            currClient.send("Choice confirmed");
            updateCurrClient();
        }

        currClient.send(gods[0]);
        gameMessage.setGod1(God.valueOf(gods[0]));
        gameMessage.setName1(client1.getName());
        updateCurrClient();
        resetUpdate();
        gameMessage.notify(gameMessage);
        sendLiteGame();

        for( ServerConnection c : clients ) {
            resetUpdate();
            if ( c != null ) {
                while ( !updateModel) {
                    sendToCurrClient("Placing workers");
                    clientMessage = readClientMessage();
                    gameMessage.setSpace1(clientMessage.getSpace1());
                    gameMessage.setSpace2(clientMessage.getSpace2());
                    gameMessage.notify(gameMessage);
                    if (!updateModel) {
                        sendLiteGame();
                    }
                }
                updateCurrClient();
                liteGame.setPlayer(currClient.getName());
                sendLiteGame();
            }
        }


        while ( !endOfTheGame ) {
             sendToCurrClient("Next action");
             clientMessage = readClientMessage();

             if ( "Choose Worker".equals(clientMessage.getAction()) ) {
                 chooseWorker();
             }
             else if ( "Charon Switch".equals(clientMessage.getAction()) ) {
                 charonSwitch();
             }
             else if ( "Prometheus Build".equals(clientMessage.getAction()) ) {
                 build();
             }
             else if ( "Move".equals(clientMessage.getAction()) ) {
                 move();
             }
             else if ( "Build".equals(clientMessage.getAction()) ) {
                 build();
             }

             if ( "End".equals(clientMessage.getAction()) ) {
                 gameMessage.resetGameMessage();
                 updateCurrClient();
                 liteGame.setCurrWorker(5,5);
                 liteGame.setPlayer(currClient.getName());
                 sendLiteGame();
             }
             else {
                 sendLiteGame();
                 if ( !endOfTheGame && !playerRemoved ) {
                     if (!updateModel) {
                         currClient.send("Invalid action");
                     } else currClient.send("Action performed");
                 }
                 else playerRemoved = false;
             }
        }

        sendToCurrClient("You won the match.");
        server.endMatch(gameID, null);
    }

    /**
     * Sets the space chosen by the current client, where his worker is placed.
     * If the resulting field in the LiteGame is null, the client has lost the game,
     * because he couldn't choose a valid worker to play with.
     */
    public void chooseWorker(){
        resetUpdate();

        gameMessage.setSpace1(clientMessage.getSpace1());
        gameMessage.notify(gameMessage);

        if ( liteGame.getCurrWorker() == null ){
            removePlayerFromTheGame();
        }
    }

    /**
     * Sets a space with an opponent worker, which Charon wants to switch
     */
    private void charonSwitch() {
        resetUpdate();
        gameMessage.setSpace1(clientMessage.getSpace1());
        gameMessage.setCharonSwitching(true);
        gameMessage.notify(gameMessage);
    }

    /**
     * Sets a space in which the worker is going to move. If the client makes a winning move,
     * the field endOfTheGame is set to true
     */
    private void move() {
        resetUpdate();

        gameMessage.setSpace1(clientMessage.getSpace1());
        gameMessage.resetGameMessage();
        gameMessage.notify(gameMessage);


        if( liteGame.isWinner() ) endOfTheGame = true;
    }

    /**
     * Sets a space where the clients want to build and the corresponding level. In case the client is closed in a circle of
     * building he looses the game and is removed
     */
    private void build() {
        resetUpdate();

        gameMessage.setSpace1(clientMessage.getSpace1());
        gameMessage.setLevel(clientMessage.getLevelToBuild());
        gameMessage.notify(gameMessage);

        if ( liteGame.getCurrWorker() == null ){
            removePlayerFromTheGame();
        }
    }

    /**
     * Closes the connection with the current client and updates the backend.
     * If the next current player is the last one in the match he wins and is notified.
     * Alternatively the loosing player is removed from the game and has no more workers on the table.
     */
    private void removePlayerFromTheGame(){
        ServerConnection toRemove = currClient;

        currClient.send("You have been removed from the match!");
        updateCurrClient();
        server.removePlayerFromMatch(gameID, toRemove);

        if ( toRemove == client1 ){
            this.client1 = null;
        }
        else if ( toRemove == client2 ){
            this.client2 = null;
        }
        else {
            client3 = null;
        }
        gameMessage.notify(gameMessage);

        if ( (client1 == null && client2 == null) ||  (client2 == null && client3 == null) || (client1 == null && client3 == null) ){
            gameMessage.notify(gameMessage);
        }
        if ( liteGame.isWinner() ) endOfTheGame = true;
        playerRemoved = true;
    }

    /**
     * Updates the LiteGame if something has changed or if the initial LiteGame hasn't been set.
     * @param message message received from the observable instance
     */
    @Override
    public void update(LiteGame message) {
        if ( liteGame == null || !message.equalsLG(liteGame) ) {
            if ( liteGame == null ){
                liteGame = new LiteGame();
            }
            liteGame = message;
            updateModel = true;
        }

        else this.updateModel = false;
    }


    /**
     * Sends the LiteGame to every client in the match.
     */
    public void sendLiteGame(){
        SerializableLiteGame toSend = liteGame.makeSerializable();

        if ( client1 != null ) {
            client1.send(toSend);
        }
        if ( client2 != null ) {
            client2.send(toSend);
        }
        if ( client3 != null ) {
            client3.send(toSend);
        }
    }

    /**
     * Updates the current playing client.
     */
    public void updateCurrClient(){
        if ( ( ( client2 == currClient) && ( client3 != null ) ) || ( (client1 == currClient) && ( client2 == null ) ) ) {
            currClient = client3;
        } else if ( ( ( client3 == currClient) && ( client1 != null ) ) || ( ( client2 == currClient ) && ( client3 == null ) ) ) {
            currClient = client1;
        } else if ( ( ( client1 == currClient ) && ( client2 != null ) ) || ( ( client3 ==currClient) && ( client1 == null ) ) ) {
            currClient = client2;
        }
    }

    /**
     * Sends a message to the current client and a message of wait to the others.
     * In case the message signs the end of the match it adds the winning player.
     * @param message message to send to the client
     */
    public void sendToCurrClient ( String message ){
        currClient.send( message );
        ServerConnection[] clients = new ServerConnection[]{client1, client2, client3};
        for(ServerConnection c: clients){
            if(c != currClient && c != null )
                if ( !message.contains("You won") )
                    c.send("Wait for " + currClient.getName() + " to end his turn" );
                else c.send("You lost the match. " + currClient.getName() + " won the game.");
        }
    }

    /**
     * Reads a ChallengerMessage from the ServerConnection of the challenger (client1).
     * The thread is put in waiting mode, until a ChallengerMessage is received.
     * @return an array of strings, which is the challenger message.
     */
    public synchronized String[] readChallengerMessage() {
        while ( client1.getChallengerChoice() == null ){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return client1.getChallengerChoice();
    }

    /**
     * Reads a clientMessage from the ServerConnection.
     * @return the ClientMessage read.
     */
    public synchronized ClientMessage readClientMessage(){
        while ( !currClient.isUpdatingClientMessage() ) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        currClient.setUpdateClientMessage(false);
        return currClient.getClientMessage();
    }

    /**
     * Wakes up the thread waiting on a ClientMessage or a Challenger message
     */
    public synchronized void wakeUpFrontEnd(){
        notify();
    }

    ///////////////////////////////
    // Methods used in the tests //
    ///////////////////////////////


    public LiteGame getLiteGame() {
        return liteGame;
    }

    public boolean getUpdateModel() {
        return updateModel;
    }

    public BackEnd getBackEnd() {
        return backEnd;
    }

    public void setLiteGame(LiteGame cloneLG) {
        this.liteGame = cloneLG;
    }

    public FrontEnd(){
        backEnd = null;
        clientMessage = null;
        gameMessage = null;
        liteGame = null;
        client1 = null;
        client2 = null;
        client3 = null;
        currClient = null;
    }

    public void resetUpdate() { this.updateModel = false; }

    public void setBackEnd(BackEnd backEnd) {
        this.backEnd = backEnd;
        this.liteGame = backEnd.getGame().getLiteGame().cloneLG();
    }

}

package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.controller.BackEnd;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.LiteGame;
import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.server.ServerConnection;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrontEnd implements Observer<LiteGame>,Runnable {

    private BackEnd backEnd;
    private ClientMessage clientMessage;
    private GameMessage gameMessage;
    private LiteGame liteGame;
    private boolean update = false;
    private ServerConnection client1;
    private ServerConnection client2;
    private ServerConnection client3;
    private ServerConnection currClient;
    private boolean endOfTheGame = false;
    private boolean removedPlayer = false;


    private int gameID;

    public FrontEnd(ServerConnection client1, ServerConnection client2, int gameID, BackEnd backEnd) {
        this.client1 = client1;
        this.client2 = client2;
        this.client3 = null;
        this.currClient = client2;      //Il primo giocatore a iniziare è il numero 2
        this.gameID = gameID;
        this.backEnd = backEnd;
        this.gameMessage = new GameMessage(this);
    }

    public FrontEnd(ServerConnection client1, ServerConnection client2, ServerConnection client3, int gameID, BackEnd backEnd) {
        this.client1 = client1;
        this.client2 = client2;
        this.client3 = client3;
        this.currClient = client2;      //Il primo giocatore a iniziare è il numero 2
        this.gameID = gameID;
        this.backEnd = backEnd;
        this.gameMessage = new GameMessage(this);
    }

    @Override
    public void run(){

        //SCELTA DELLE CARTE
        //TODO: informa i client dei giocatori presenti nel gioco
        ServerConnection[] clients = new ServerConnection[]{client2, client3, client1};

        //I giocatori settano a turno le posizioni dei loro Workers
        for( ServerConnection c : clients ) {
            if ( c != null ){
                if ( client3 == null ){
                    c.send("Game has started with code " + String.valueOf(gameID) + "\n" + "Current players are " +
                            client1.getName() + ", " + client2.getName());
                }
                else {
                    c.send("Game has started with code " + String.valueOf(gameID) + "\n" + "Current players are " +
                            client1.getName() + ", " + client2.getName() + ", " + client3.getName());
                }
            }
        }

        String[] gods = client1.readChallengerMessage();

        //client 2 e 3 se c'è scelgono le divinità
        for (int i=0; i<2 && currClient != client1; i++){
            currClient.asyncSend(Arrays.toString(gods) );
            clientMessage = currClient.readClientMessage(); // il giocatore due ha scelto la prima divinità
            if (i == 0) {
                gameMessage.setGod2(clientMessage.getGod());
                gameMessage.setName2(clientMessage.getName());
            } else if (i == 1) {
                gameMessage.setGod3(clientMessage.getGod());
                gameMessage.setName3(clientMessage.getName());
            }
            List<String> list = new ArrayList<String>(Arrays.asList(gods));
            list.remove( clientMessage.getGod().name());  //tutto maiuscolo
            gods = list.toArray(new String[0]);
            currClient.asyncSend("Choice confirmed");
            updateCurrClient();
        }

        currClient.send(gods[0]);  //challenger ha la sua carta
        gameMessage.setGod1(God.valueOf(gods[0]));
        gameMessage.setName1(client1.getName());
        updateCurrClient();  //il prossimo turno è del giocatore 2
        resetUpdate();
        gameMessage.notify(gameMessage);
        sendLiteGame();

        //I giocatori settano a turno le posizioni dei loro Workers
        for( ServerConnection c : clients ) {
            resetUpdate();

            if ( c != null ) {
                while ( !update ) {
                    sendToCurrClient("Placing workers");
                    clientMessage = currClient.readClientMessage();
                    gameMessage.setSpace1(clientMessage.getSpace1());
                    gameMessage.setSpace2(clientMessage.getSpace2());
                    //la chiamata di notify termina nel momento in cui viene eseguita completamente la funzione update della classe FrontEnd
                    gameMessage.notify(gameMessage);
                    sendLiteGame();
                }
                updateCurrClient();
            }

        }

        while ( !endOfTheGame) {
            //chiedo al client di eseguire una mossa
             sendToCurrClient("Next action");
             clientMessage = currClient.readClientMessage();

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
             else if ( "End".equals(clientMessage.getAction()) ) {
                 gameMessage.resetGameMessage();
                 updateCurrClient();
                 sendLiteGame();
             }
             else currClient.asyncSend("Invalid action");
        }


        //TODO: sistemare il client che interrompe la connessione e la partita viene interrotta da tutti, comprimere codice

        //TODO: sistemare i messaggi
        sendToCurrClient("You won the match");
        for ( ServerConnection s : clients ){
            s.closeConnection();
        }
    }

    private void read(){
        //non scade il timeout
        clientMessage = currClient.readClientMessage();
        //action non dev'essere exit
        closeMatch();
    }

    public void closeMatch(){

    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }


    public void chooseWorker(){
        resetUpdate();

        gameMessage.setSpace1(clientMessage.getSpace1());
        gameMessage.notify(gameMessage);

        if ( liteGame.getCurrWorker() == null ){
            removePlayerFromTheGame();
        }
        else {
            sendLiteGame();
        }
    }


    private void charonSwitch() {
        resetUpdate();
        gameMessage.setSpace1(clientMessage.getSpace1());
        gameMessage.setCharonSwitching(true);
        gameMessage.notify(gameMessage);

        sendLiteGame();
    }

    private void move() {
        resetUpdate();

        gameMessage.setSpace1(clientMessage.getSpace1());
        gameMessage.resetGameMessage();
        gameMessage.notify(gameMessage);


        if( liteGame.isWinner() ) endOfTheGame = true;
        sendLiteGame();
    }

    private void build() {
        resetUpdate();

        gameMessage.setSpace1(clientMessage.getSpace1());
        gameMessage.setLevel(clientMessage.getLevelToBuild());
        gameMessage.notify(gameMessage);

        if ( liteGame.getCurrWorker() == null ){
            removePlayerFromTheGame();
        }
        else sendLiteGame();
    }

    //chiude la connessione del giocatore corrente e inizia il turno con il giocatore successivo
    //se è il caso di due giocatori conclude la partita e notifica il giocatore vincente
    private void removePlayerFromTheGame(){
        currClient.asyncSend("You have lost the match!");
        ServerConnection toRemove = currClient;
        updateCurrClient();
        toRemove.closeConnection();
        toRemove = null;

        // il backEnd esegue la execute di RemovePlayerState
        gameMessage.notify(gameMessage);
        sendLiteGame(); //mando la tabella di gioco ai giocatori rimasti

        //Se il giocare era l'ultimo in gioco il backEnd lo scrive nel LiteGame
        gameMessage.notify(gameMessage);

    }


    public ServerConnection getClient1() {
        return client1;
    }

    public ServerConnection getClient2() {
        return client2;
    }

    public ServerConnection getClient3() {
        return client3;
    }


    @Override
    public void update(LiteGame message) {
        //update riceve litegame
        // TODO: se il messaggio è uguale al precedente o se liteGame è vuoto, ok

        if ( liteGame == null || !message.equalsLG(liteGame) ) {
            if ( liteGame == null ){
                liteGame = new LiteGame();
            }
            liteGame = message;
            update = true;
        }

        else resetUpdate();

    }

    public void resetUpdate() { this.update = false; }

    public void sendLiteGame(){
        SerializableLiteGame toSend = liteGame.makeSerializable();

        if ( client1 != null ) {
            client1.asyncSend(toSend);  //Invio sempre tabella di gioco a tutti i giocatori
        }
        if ( client2 != null ) {
            client2.asyncSend(toSend);
        }
        if ( client3 != null ) {
            client3.asyncSend(toSend);
        }
    }

    public void setBackEnd(BackEnd backEnd) {
        this.backEnd = backEnd;
        this.liteGame = backEnd.getGame().getLiteGame().cloneLG();
    }


    public void updateCurrClient(){
        if ( ( ( client2 == currClient) && ( client3 != null ) ) || ( (client1 == currClient) && ( client2 == null ) ) ) {
            currClient = client3;
        } else if ( ( ( client3 == currClient) && ( client1 != null ) ) || ( ( client2 == currClient ) && ( client3 == null ) ) ) {
            currClient = client1;
        } else if ( ( ( client1 == currClient ) && ( client2 != null ) ) || ( ( client3 ==currClient) && ( client1 == null ) ) ) {
            currClient = client2;
        }
    }

    public void sendToCurrClient ( String message ){
        currClient.asyncSend( message );
        ServerConnection[] clients = new ServerConnection[]{client1, client2, client3};
        for(ServerConnection c: clients){
            if(c != currClient && c != null )
                if ( !message.contains("You won") )
                    c.asyncSend("Wait for " + currClient.getName() + " to end his turn" );
                else c.asyncSend("You lost the match");
        }
    }


    /////////////////////
    // Metodi per test //
    /////////////////////


    //mi serve per i test
    public LiteGame getLiteGame() {
        return liteGame;
    }

    public boolean getUpdate() {
        return update;
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
}

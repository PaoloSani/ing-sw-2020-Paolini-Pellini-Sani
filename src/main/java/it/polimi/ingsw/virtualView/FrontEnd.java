package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.controller.BackEnd;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.LiteGame;
import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FrontEnd implements Observer<LiteGame> {

    private BackEnd backEnd;
    private ClientMessage clientMessage;
    private GameMessage gameMessage;
    private LiteGame liteGame;
    private boolean update = false;
    private SocketClientConnection client1;
    private SocketClientConnection client2;
    private SocketClientConnection client3;
    private SocketClientConnection currClient;
    private boolean endOfTheGame = false;
    private boolean removedPlayer = false;


    private int gameID;

    public FrontEnd(SocketClientConnection client1, SocketClientConnection client2, int gameID) {
        this.client1 = client1;
        this.client2 = client2;
        this.client3 = null;
        this.currClient = client2;      //Il primo giocatore a iniziare è il numero 2
        this.gameID = gameID;
        this.gameMessage = new GameMessage(this);
    }

    public FrontEnd(SocketClientConnection client1, SocketClientConnection client2, SocketClientConnection client3, int gameID) {
        this.client1 = client1;
        this.client2 = client2;
        this.client3 = client3;
        this.currClient = client2;      //Il primo giocatore a iniziare è il numero 2
        this.gameID = gameID;
        this.gameMessage = new GameMessage(this);
    }

    public void run(){

        //SCELTA DELLE CARTE
        //TODO: informa i client dei giocatori presenti nel gioco
        client1.asyncSend("Choose cards of the game");
        client2.asyncSend("Wait! Challenger is choosing cards!");
        if(client3 != null) {
            client3.asyncSend("Wait! Challenger is choosing cards!");
        }
        String[] gods = client1.readChallengerMessage();

        //client 2 e 3 se c'è scelgono le divinità
        for (int i=0; i<2 && currClient != client1; i++){
            sendToCurrClient("Choose your God! Available cards: " + Arrays.toString(gods));
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
            updateCurrClient();
        }

        currClient.send("Your god is " + gods[0]);  //challenger ha la sua carta
        gameMessage.setGod1(God.valueOf(gods[0]));
        gameMessage.setName1(client1.getName());
        updateCurrClient();  //il prossimo turno è del giocatore 2
        resetUpdate();
        gameMessage.notify(gameMessage);
        sendLiteGame();

        SocketClientConnection[] clients = new SocketClientConnection[]{client2, client3, client1};

        //I giocatori settano a turno le posizioni dei loro Workers
        for( SocketClientConnection c : clients ) {
            resetUpdate();

            if ( c != null ) {
                while ( !update ) {
                    sendToCurrClient("Placing workers");
                    clientMessage = currClient.readClientMessage();
                    gameMessage.setSpace1(clientMessage.getSpace1());
                    gameMessage.setSpace2(clientMessage.getSpace2());
                    //la chiamata di notify termina nel momento in cui viene eseguita completamente la funzione update della classe FrontEnd
                    gameMessage.notify(gameMessage);
                }
                sendLiteGame();
            }

            updateCurrClient();
        }



        //TODO: sistemare il client che interrompe la connessione e la partita viene interrotta da tutti, comprimere codice
        while ( !endOfTheGame){
            // chiedo la prossima mossa da fare
            //if ( timeout ) chiudo la partita
            /*switch (action)
                    chooseWorker
                    switching
                    prometheusBuild
                    Move -> metto un contatore per verificare che sia fatta sempre almeno una volta
                    build -> contatore per verificare che sia fatta almeno una volta ->faccio updateClient
                    end -> chiude il turno
                    exit -> chiude la partita per tutti i giocatori
            */

            removedPlayer = false;
            //all'inizio sceglie il worker con cui giocare
            chooseWorker();

            //rifaccio il controllo perhé chooseWorker() potrebbe avere modificato questo attributo
            if ( !endOfTheGame ) {
                boolean tryMove;
                // delego alla client le opzioni di costruire o muoversi, prima di fare la normale mossa, tramite poteri speciali
                sendToCurrClient("Make your move");
                clientMessage = currClient.readClientMessage();

                if (clientMessage.getAction().equals("Switching")) {
                    tryMove = charonSwitch();
                    if (tryMove) {        //il client voleva fare la move
                        sendToCurrClient("Make your move");
                        clientMessage = currClient.readClientMessage();
                    }

                } else if (clientMessage.getAction().equals("Prometheus moving")) {
                    tryMove = prometheusBuild();
                    if (tryMove) {        //il client voleva fare la move
                        sendToCurrClient("Make your move");
                        clientMessage = currClient.readClientMessage();
                    }
                }

                move();
                if(!endOfTheGame){
                    sendToCurrClient("Make your move");
                    clientMessage = currClient.readClientMessage();

                    if ( clientMessage.getAction().equals("Artemis moving") ){
                        tryMove = move();
                        if (tryMove && !endOfTheGame) {        //il client voleva fare la move
                            sendToCurrClient("Make your build");
                            clientMessage = currClient.readClientMessage();
                        }
                    }

                    while( clientMessage.getAction().equals("Triton moving" ) && !endOfTheGame ){
                        tryMove = move();
                        if ( tryMove && !endOfTheGame ) {        //il client voleva fare la move
                                sendToCurrClient("Make your move");
                                clientMessage = currClient.readClientMessage();
                        }
                    }

                    if( !endOfTheGame ){
                        build();
                        if ( !removedPlayer && !endOfTheGame ){
                            sendToCurrClient("Make your move");
                            clientMessage = currClient.readClientMessage();

                            //End mi serve perché Poseidone, Demetra o Efesto potrebbero non volere usare il loro potere speciale. Se il client non è uno di questi tre
                            // il messaggio di end viene generato automaticamente
                            while ( !clientMessage.getAction().equals("End") ){
                                if ( build() ) {
                                    sendToCurrClient("Make your build");
                                    clientMessage = currClient.readClientMessage();
                                }
                            }

                            //fondamentale qui, perché se il cambio turno porta da poseidone a demetra, per esempio, il backend riparte da ChooseWorkerState
                            gameMessage.resetGameMessage();
                            updateCurrClient();
                        }
                    }
                }
            }

        }

        //TODO: sistemare i messaggi
        sendToCurrClient("You won the match");
        for ( SocketClientConnection s : clients ){
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

    public void placeWorkers(int x1, int y1, int x2, int y2){
        //
        //players.notify
    }
    //metodo che sceglie il worker

    public void chooseWorker(){
        resetUpdate();
        while ( !update ) {
            sendToCurrClient("Choose Worker");
            clientMessage = currClient.readClientMessage();
            gameMessage.setSpace1(clientMessage.getSpace1());
            gameMessage.notify(gameMessage);
        }

        if ( liteGame.getCurrWorker() == null ){
            currClient.asyncSend("You have lost the match!");
            SocketClientConnection toRemove = currClient;
            updateCurrClient();
            toRemove.closeConnection();
            toRemove = null;

            // il backEnd esegue la execute di RemovePlayerState
            gameMessage.notify(gameMessage);
            sendLiteGame(); //mando la tabella di gioco ai giocatori rimasti

            //Se il giocare era l'ultimo in gioco il backEnd lo scrive nel LiteGame
            gameMessage.notify(gameMessage);
            if ( liteGame.isWinner() ){
                currClient.asyncSend("You won the match!\n");
                currClient.closeConnection();
                currClient = null;
                endOfTheGame = true; //booleano per uscire dal metodo run
            }
            else chooseWorker();

        }
        else {
            sendLiteGame();
        }
    }


    //il metodo è booleano perché se ritorna VERO allora ha eseguito correttamente la mossa, altrimenti FALSO indica che
    // il client ha sbagliato mossa e non voleva una Switch ma direttamente una move
    //questa scelta è per non bloccare il gioco su un client che invoca una switch ma non ha nessuna cella valida in cui farla
    private boolean charonSwitch() {
        boolean result = false;
        resetUpdate();
        while ( !update && !clientMessage.getAction().equals("Move") ) {
            gameMessage.setSpace1(clientMessage.getSpace1());
            gameMessage.setCharonSwitching(true);
            gameMessage.notify(gameMessage);

            if ( !update ){
                currClient.asyncSend("Invalid move! Repeat your move");
                clientMessage = currClient.readClientMessage();
            }
            else {
                result = true;
            }
        }
        sendLiteGame();
        return result;
    }

    //il metodo è booleano perché se ritorna VERO allora ha eseguito correttamente la mossa, altrimenti FALSO indica che
    // il client ha sbagliato mossa e non voleva una Build ma direttamente una move
    //questa scelta è per non bloccare il gioco su un client che invoca una build ma non ha nessuna cella valida in cui farla
    private boolean prometheusBuild() {
        boolean result = false;
        resetUpdate();
        while ( !update && !clientMessage.getAction().equals("Move") ) {
            gameMessage.setSpace1(clientMessage.getSpace1());
            gameMessage.setLevel(clientMessage.getLevelToBuild());
            gameMessage.notify(gameMessage);

            if ( !update ){
                currClient.asyncSend("Invalid move!");
                clientMessage = currClient.readClientMessage();
            }
            else {
                result = true;
            }
        }
        sendLiteGame();
        return result;
    }

    private boolean move() {
        boolean result = false;
        resetUpdate();
        while ( !update && ! clientMessage.getAction().equals("Build") ) {
            gameMessage.setSpace1(clientMessage.getSpace1());
            gameMessage.resetGameMessage();
            gameMessage.notify(gameMessage);

            if ( !update ){
                currClient.asyncSend("Invalid move! Repeat your move");
                clientMessage = currClient.readClientMessage();
            }
            else{
                result = true;
                if(liteGame.isWinner()) endOfTheGame= true;
            }
        }
        sendLiteGame();
        return result;
    }

    private boolean build() {
        boolean result = false;
        resetUpdate();
        while ( !update && ! clientMessage.getAction().equals("End") ) {
            gameMessage.setSpace1(clientMessage.getSpace1());
            gameMessage.setLevel(clientMessage.getLevelToBuild());
            gameMessage.notify(gameMessage);

            if ( !update ){
                currClient.asyncSend("Invalid move! Repeat your build");
                clientMessage = currClient.readClientMessage();
            }
            else if ( liteGame.getCurrWorker() == null ){
                currClient.asyncSend("You have lost the match!");
                SocketClientConnection toRemove = currClient;
                updateCurrClient();
                toRemove.closeConnection();
                toRemove = null;

                // il backEnd esegue la execute di RemovePlayerState
                gameMessage.notify(gameMessage);
                sendLiteGame(); //mando la tabella di gioco ai giocatori rimasti

                //Se il giocare era l'ultimo in gioco il backEnd lo scrive nel LiteGame
                gameMessage.notify(gameMessage);
                if ( liteGame.isWinner() ){
                    currClient.asyncSend("You won the match!\n");
                    currClient.closeConnection();
                    currClient = null;
                    endOfTheGame = true; //booleano per uscire dal metodo run
                }
                else {
                    removedPlayer = true;
                    result = true;
                }

            }
            else result = true;
        }
        sendLiteGame();
        return result;
    }


    //metodi per la connessione

    public void setConnection() {
    }

    public SocketClientConnection getClient1() {
        return client1;
    }

    public SocketClientConnection getClient2() {
        return client2;
    }

    public SocketClientConnection getClient3() {
        return client3;
    }

//TODO: se il model ritorna currWorker == null significa che il giocatore corrente ha perso. La virtualView si occupa di informare il client
    //TODO: e manda un update al controller che rimuove il giocatore dal gioco proseguendo con due giocatori o dichiarando il vincitore

    @Override
    public void update(LiteGame message) {
        //update riceve litegame
        // TODO: se il messaggio è uguale al precedente o se liteGame è vuoto, ok

        if ( liteGame == null || !message.equalsLG(liteGame) ) {
            liteGame = message;
            update = true;
        }

        else resetUpdate();

    }

    public void sendLiteGame(){
        if ( client1 != null ) {
            client1.asyncSend(liteGame);  //Invio sempre tabella di gioco a tutti i giocatori
        }
        if ( client2 != null ) {
            client2.asyncSend(liteGame);
        }
        if ( client3 != null ) {
            client3.asyncSend(liteGame);
        }
    }

    public void setBackEnd(BackEnd backEnd) {
        this.backEnd = backEnd;
        this.liteGame = backEnd.getGame().getLiteGame().cloneLG();
    }

    public boolean getUpdate() {
        return update;
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
        currClient.asyncSend("Execute your " + message);
        SocketClientConnection[] clients = new SocketClientConnection[]{client1, client2, client3};
        for(SocketClientConnection c: clients){
            if(c != currClient && c != null) c.asyncSend("Wait for " + currClient.getName() + " to execute his " + message);
        }
    }


    //TODO da togliere magari poi

    public void resetUpdate() { this.update = false; }


    /////////////////////
    // Metodi per test //
    /////////////////////


    //mi serve per i test
    public LiteGame getLiteGame() {
        return liteGame;
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

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
        client1.asyncSend("Choose cards of the game");
        client2.asyncSend("Wait! Challenger is choosing cards!");
        if(client3 == null) client3.asyncSend("Wait! Challenger is choosing cards!");
        String[] gods = client1.readChallengerMessage();
        //client 2 e 3 se c'è scelgono le divinità
        for (int i=0; i<2 && currClient!=client1; i++){
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
        gameMessage.notify(gameMessage);







        //riceve il challengerMessage -> capisco quanti giocatori sono in gioco
        //conta la connessione di numOfPlayer client diversi
        //ad ogni connessione chiede di inserire il nickname e poi notifica gli altri giocatori connessi
        //alla connessione del secondo client il frontend attiva un timer,
        //se scaduto quel timer il giocatore 3 non è connesso la partita automaticamente è di due giocatori

        //il frontend notifica il challenger che la partita avrà NUM giocatori
        // il challenger sceglie NUM carte e invia il messaggio al frontEnd
        //il frontEnd fa scegliere ad ogni client la sua carta

        //SETPLAYERSSTATE
        //ricevute tutte le divinità il frontEnd effettua la notify della classe GameMessage

        //PLACEWORKERSTATE
        //A turno ogni client invia la posizione dei suoi giocatori

        //CHOOSEWORKERSTATE
        //Il client sceglie il giocatore

        //A seconda della divinità il client può scegliere mosse diverse da fare -> la UI del client mostra al client che mosse può fare correntemente

        //MOVE

        //BUILD

        //Caso vincita -> il client viene notificato e il gioco termina

        //Caso sconfitta -> il client viene notificato, il frontEnd manda un messaggio al backend che rimuove effettivamente il client
        // dal gioco e produce una nuova tabella senza i worker del client che ha perso
        // se la partita era di due giocatori viene notificato il client che ha vinto

        //EXIT
        //TODO: il caso in cui un giocatore lasci intenzionalmente la partita è da gestire?

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
        //
        //players.notify
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

        if ( liteGame == null ) {
            liteGame = message;
            update = true;

        }
        else if ( message.equalsLG(liteGame) ) resetUpdate();
        else {
            liteGame = message;
            update = true;
        }
        client1.asyncSend(message);  //Invio sempre tabella di gioco a tutti i giocatori
        client2.asyncSend(message);
        client3.asyncSend(message);
    }

    public void setBackEnd(BackEnd backEnd) {
        this.backEnd = backEnd;
        this.liteGame = backEnd.getGame().getLiteGame().cloneLG();
    }

    public boolean getUpdate() {
        return update;
    }

    public void updateCurrClient(){
        if ( ( ( client2 == currClient) && (client3 != null ) ) || ( (client1 == currClient) && ( client2 == null ) ) ) {
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
}

package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.controller.BackEnd;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.LiteGame;
import it.polimi.ingsw.util.Observer;

public class FrontEnd implements Observer<LiteGame> {
    BackEnd backEnd;

    private GameMessage gameMessage;
    private LiteGame liteGame;
    private boolean update = false;

    public void run(){
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


    //metodo che inizializza players
    public void settingPlayers(String name1, String name2, String name3, God god1, God god2, God god3 ){
        //caso due o tre giocatori
        //players.notify
        gameMessage.setName1(name1);
        gameMessage.setName2(name2);
        gameMessage.setName3(name3);
        gameMessage.setGod1(god1);
        gameMessage.setGod2(god2);
        gameMessage.setGod3(god3);

        gameMessage.notify();

        while ( !update ){

        }

        //scrivo ai 2-3 client
    }
    //metodo che posiziona i workers

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
    }

    public void setBackEnd(BackEnd backEnd) {
        this.backEnd = backEnd;
        this.liteGame = backEnd.getGame().getLiteGame().cloneLG();
    }

    public boolean getUpdate() {
        return update;
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

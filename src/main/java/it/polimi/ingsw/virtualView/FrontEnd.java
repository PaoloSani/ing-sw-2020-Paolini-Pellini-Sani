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


    public BackEnd getBackEnd() {
        return backEnd;
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

    public void setLiteGame(LiteGame cloneLG) {
        this.liteGame = cloneLG;
    }
}

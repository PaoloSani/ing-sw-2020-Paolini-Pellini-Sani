package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.controller.BackEnd;
import it.polimi.ingsw.model.LiteGame;
import it.polimi.ingsw.util.Observer;

public class FrontEnd implements Observer<LiteGame> {
    public BackEnd getBackEnd() {
        return backEnd;
    }

    BackEnd backEnd;
    private PlayersInTheGame players;




    //metodo che inizializza players
    public void settingPlayers(){
        //caso due o tre giocatori
        //players.notify
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



    @Override
    public void update(LiteGame message) {
        //update riceve litegame

    }

}

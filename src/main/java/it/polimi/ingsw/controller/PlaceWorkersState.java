package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;

//TODO: Riccardo
public class PlaceWorkersState implements GameState {
    private BackEnd backEnd;
    private Space space1;
    private Space space2;

    public PlaceWorkersState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }


    @Override
    public boolean execute() {
        space1 = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
        if( space1 == null ) return false;

        space2 = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
        if( space2 == null ) return false;

        backEnd.getCurrPlayer().getWorker1().setSpace(space1);
        backEnd.getCurrPlayer().getWorker2().setSpace(space2);

        //A questo punto il model modifica il liteGame
        //notify del LiteGame

        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());   //Notifico la VView
        return true;
    }


    @Override
    public void reset(){
        space1 = null;
        space2 = null;
    }


    //update: il currPlayer del Server ha scelto dove piazzare i suoi giocatori
    // lancio execute che agisce sul model


}

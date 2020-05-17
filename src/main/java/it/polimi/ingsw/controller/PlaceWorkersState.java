package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;


public class PlaceWorkersState implements GameState {
    private BackEnd backEnd;
    private Space space1;
    private Space space2;

    public PlaceWorkersState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    //TODO: da correggere, poiché non avrò mai null in una delle due celle (nullPointerException)
    //TODO Sistemare caso tre giocatore durante il placing workers, se il terzo client mette workers in cella sbagliata poi si chiude
    @Override
    public boolean execute() {
        boolean result = true;
        space1 = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
        if( ( space1 == null ) || ( space1.getWorker() != null ) ) result = false;

        if ( result ) {
            space2 = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace2()[0], backEnd.getGameMessage().getSpace2()[1]);
            if ( ( space2 == null ) || ( space2.getWorker() != null ) || ( space2 == space1)) result = false;

            if ( result ) {
                backEnd.getCurrPlayer().getWorker1().setSpace(space1);
                backEnd.getCurrPlayer().getWorker2().setSpace(space2);
            }
            //A questo punto il model modifica il liteGame
            //notify del LiteGame
        }

        backEnd.getGame().refreshLiteGame();
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());   //Notifico la VView
        return result;
    }


    @Override
    public void reset(){
        space1 = null;
        space2 = null;
    }


    /////////////////////
    // Metodi per Test //
    /////////////////////

    //update: il currPlayer del Server ha scelto dove piazzare i suoi giocatori
    // lancio execute che agisce sul model


}

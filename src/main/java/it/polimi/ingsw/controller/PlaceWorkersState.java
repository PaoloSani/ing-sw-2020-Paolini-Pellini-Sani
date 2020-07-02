package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;

/**
 * Class that represents the state of the FSM in which the current player places his workers
 */

public class PlaceWorkersState implements GameState {
    private BackEnd backEnd;
    private Space space1;
    private Space space2;

    public PlaceWorkersState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    /**
     * @return true in case the execute was successful, meaning that the method has placed the workers correctly in the game table.
     */
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

    /**
     * Sets the counters back to 0 and the boolean constraints to false
     */
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

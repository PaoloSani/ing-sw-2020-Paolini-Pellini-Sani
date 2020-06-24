package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;

/**
 * A GameState made for Prometheus in order to make a build and then move his worker without moving up
 */
public class PrometheusBuildState implements GameState {
    private BackEnd backEnd;
    private Space toBuild;
    private int level;

    /**
     * The constructor of the class
     * @param backEnd: his reference BackEnd
     */
    public PrometheusBuildState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    /**
     * builds in a space if the action is valid
     * @return true if the action was correctly performed.
     */
    @Override
    public boolean execute() {
        boolean result = true;
        toBuild = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
        if( toBuild == null ) result = false;

        if ( result ) {
            level = backEnd.getGameMessage().getLevel();


            if (!backEnd.getCurrPlayer().buildSpace(backEnd.getCurrWorker(), toBuild, level))
                result = false;

        }

        backEnd.getGame().refreshLiteGame();
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());
        return result;
    }


    /**
     * Sets the counters back to 0 and the boolean constraints to false
     */
    @Override
    public void reset(){
        toBuild = null;
        level = 0;
    }
}

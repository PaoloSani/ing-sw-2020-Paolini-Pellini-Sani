package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;

/**
 * Class that represents the status of the FSM in which used for the special power of Charon,
 * the current worker exchanges his position with the opponent's position.
 */
public class CharonSwitchState implements GameState {
    private BackEnd backEnd;
    private Space spaceToSwitch;

    public CharonSwitchState(BackEnd backEnd) {
        this.backEnd = backEnd;
        spaceToSwitch = null;
    }



    //chiamata a charonPower in game

    /**
     * @return The method returns true if the swicht has been executed correctly
     */
    @Override
    public boolean execute() {
        boolean result = true;
        //update: riceve una cella in cui è contenuto il worker da switchare
        //execute: esegue lo switch
        spaceToSwitch = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
        if( spaceToSwitch == null ) result = false;


           if( !backEnd.getGame().charonPower(backEnd.getCurrWorker(), spaceToSwitch.getWorker()) && result )
            result = false;

        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());   //Notifico la VView

        return result;
    }

    /**
     * Sets the counters back to 0 and the boolean constraints to false
     */
    @Override
    public void reset() {
        spaceToSwitch = null;
    }


}

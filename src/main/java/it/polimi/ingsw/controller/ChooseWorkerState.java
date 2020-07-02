package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.util.GameState;


/**
 * ChooseWorkerState is the class that represents the status of the FSM in which the current player chooses the worker to play with
 */

public class ChooseWorkerState implements GameState {
    private BackEnd backEnd;
    private Worker chosenWorker;
    private Worker otherWorker;

    /**
     * Class constructor
     * @param backEnd: reference backEnd
     */
    public ChooseWorkerState(BackEnd backEnd) {
        this.backEnd = backEnd;
        chosenWorker = null;
        otherWorker = null;
    }

    /**
     * @return true in case the execute was successful, meaning that the method has chosen the worker correctly in the game table.
     */
    @Override
    public boolean execute() {
        boolean result = true;
        boolean blockedByHypnus = false;

        chosenWorker = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]).getWorker();
        if ( chosenWorker == null || chosenWorker.getPlayer() != backEnd.getCurrPlayer() ) {
            result = false;
            backEnd.getGame().getLiteGame().setCurrWorker(5,5);
        }
        else {
            otherWorker = backEnd.getCurrPlayer().getOtherWorker(chosenWorker);
        }

        if ( result ) {

            //At first I must check if the choice is forced by the presence of Hypnus
            if ( backEnd.getCurrPlayer().getGod() != God.HYPNUS                                 &&
                    backEnd.getGame().getConstraint().hypnusBlocks()                            &&
                    chosenWorker.getSpace().getHeight() > otherWorker.getSpace().getHeight()    )
            {
                    blockedByHypnus = true;
            }

            if ( !blockedByHypnus && backEnd.getGame().isFreeToMove(chosenWorker) ) {
                backEnd.setCurrWorker(chosenWorker);
                backEnd.getGame().setCurrWorker(chosenWorker);
            }
            else {
                if ( backEnd.getGame().isFreeToMove(otherWorker) ) {
                    backEnd.setCurrWorker(otherWorker);
                    backEnd.getGame().setCurrWorker(otherWorker);
                } else {
                    backEnd.getGame().setCurrWorker(null);
                    backEnd.setToRemove(backEnd.getCurrPlayer());
                }
            }
        }
        backEnd.getGame().refreshLiteGame();
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());
        return result;
    }

    /**
     * Sets the counters back to 0 and the boolean constraints to false
     */
    @Override
    public void reset() {
        otherWorker = null;
        chosenWorker = null;
    }

}

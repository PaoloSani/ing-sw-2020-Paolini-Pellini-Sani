package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;

/**
 * A GameState which follows the PrometheusBuildState in order to make Prometheus use his power
 */
public class PrometheusMoveState implements GameState {
    private BackEnd backEnd;
    private int[] toMove = new int[]{0,-1};

    public PrometheusMoveState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    /**
     * performs the move and check if the player is not moving up
     * @return true if the action was correctly performed
     */
    @Override
    public boolean execute() {
        boolean result = true;
        toMove = backEnd.getGameMessage().getSpace1();
        Space nextSpace = null;
        nextSpace = backEnd.getGame().getSpace(toMove[0], toMove[1]);

        if ( nextSpace == null ) result = false;

        if ( result && (nextSpace.getHeight() - backEnd.getCurrWorker().getSpace().getHeight() <= 0)) {
                result = backEnd.getCurrPlayer().moveWorker(backEnd.getCurrWorker(), nextSpace);
        }
        else result = false;

        backEnd.getGame().setCurrWorker(backEnd.getCurrWorker());
        backEnd.getGame().refreshLiteGame();
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());
        return result;
    }

    /**
     * Sets the counters back to 0 and the boolean constraints to false
     */
    @Override
    public void reset() {
        toMove[0] = -1;
    }
}

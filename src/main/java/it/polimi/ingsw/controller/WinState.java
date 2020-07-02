package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;

/**
 * A GameState that ends the match
 */
public class WinState implements GameState {
    BackEnd backEnd;


    public WinState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    /**
     * Sets the model, so that the remote view is notified that the current player won the match
     * @return always true, because the action can always be performed
     */
    @Override
    public boolean execute() {
        backEnd.getGame().setWinner(true);
        backEnd.getGame().refreshLiteGame();
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());
        return true;
    }

    @Override
    public void reset() {}

}

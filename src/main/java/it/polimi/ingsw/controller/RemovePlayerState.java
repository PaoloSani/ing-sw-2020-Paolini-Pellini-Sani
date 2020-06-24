package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;

/**
 * A GameState which performs the remove of a player from the game
 */
public class RemovePlayerState implements GameState {
    private BackEnd backEnd;

    public RemovePlayerState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    /**
     * removes the player from the game and updates the set of players in the match.
     * @return always true, because the action can always be performed
     */
    @Override
    public boolean execute() {
        backEnd.getToRemove().getWorker1().getSpace().setWorker(null);
        backEnd.getToRemove().getWorker1().setSpace(null);
        backEnd.getToRemove().getWorker2().getSpace().setWorker(null);
        backEnd.getToRemove().getWorker2().setSpace(null);

        if ( backEnd.getChallenger() == backEnd.getToRemove() )
            backEnd.setChallenger(null);
        else if ( backEnd.getPlayer2() == backEnd.getToRemove() )
            backEnd.setPlayer2(null);
        else backEnd.setPlayer3(null);

        backEnd.getGame().refreshLiteGame();
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());
        return true;
    }

    /**
     * Sets the counters back to 0 and the boolean constraints to false
     */
    @Override
    public void reset() {
        backEnd.setToRemove(null);
    }

}

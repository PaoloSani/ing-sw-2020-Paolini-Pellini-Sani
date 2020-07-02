package it.polimi.ingsw.util;

/**
 * GameState is an interface for a state of the controller
 */
public interface GameState  {
    /**
     * Executes something on the model
     * @return true if the execution was correct
     */
    boolean execute();

    /**
     * Resets the values of the GameState
     */
    void reset();

}

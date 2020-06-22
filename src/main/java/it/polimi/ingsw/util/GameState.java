package it.polimi.ingsw.util;

/**
 * an interface for a state of the controller
 */
public interface GameState  {
    /**
     * executes something on the model
     * @return true if the execution was correct
     */
    boolean execute();

    /**
     * resets the values of the GameState
     */
    void reset();

}

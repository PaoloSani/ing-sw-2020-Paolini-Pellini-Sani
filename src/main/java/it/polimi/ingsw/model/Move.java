package it.polimi.ingsw.model;

/**
 * Move is a strategy pattern interface for the move method
 */
public interface Move {
    /**
     * Interface method that will be implemented by the various classes
     * @param worker Worker performing the move
     * @param space Space where you want to move
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean execute(Worker worker, Space space);
}

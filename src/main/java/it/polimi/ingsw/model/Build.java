package it.polimi.ingsw.model;

/**
 * Pattern strategy interface for the build method
 */

public interface Build {
    /**
     * Interface method that will be implemented by the various classes
     * @param worker Worker performing the build
     * @param space Space where you want to build
     * @param level Level you want to build
     * @return This method returns true if the move has been successful, false otherwise
     */
    boolean execute(Worker worker, Space space, int level);
}

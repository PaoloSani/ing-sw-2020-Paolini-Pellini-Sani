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

/*
    Nella build mancano dei controlli per verificare che la cella passata sia effettivamente nelle 5x5
                space.getX() > 4 && space.getX() < 0                           ||
                space.getY() > 4 && space.getY() < 0                           ||     //space non appartenente alla tabella
*/
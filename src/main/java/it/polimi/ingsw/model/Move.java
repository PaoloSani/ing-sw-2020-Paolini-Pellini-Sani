package it.polimi.ingsw.model;

// Interfaccia per il pattern stategy per la Move.

public interface Move {
    public boolean execute(Worker worker, Space space) throws IllegalSpaceException;
}

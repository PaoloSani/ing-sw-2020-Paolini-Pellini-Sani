package it.polimi.ingsw.model;

public interface Move {
    public boolean execute(Worker worker, Space space) throws IllegalSpaceException;
}

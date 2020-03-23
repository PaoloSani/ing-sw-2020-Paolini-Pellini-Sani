package it.polimi.ingsw.model;

public interface Build {
    public void execute( Worker worker, Space space, int level ) throws IllegalSpaceException;
}

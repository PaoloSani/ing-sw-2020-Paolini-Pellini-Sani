package it.polimi.ingsw.model;

public interface Build {
    void execute(Worker worker, Space space, int level);
}

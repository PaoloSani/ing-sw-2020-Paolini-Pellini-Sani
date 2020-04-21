package it.polimi.ingsw.util;

public interface GameState  {
    // void changeState(GameState nextState);
    boolean execute();


    void reset();

}

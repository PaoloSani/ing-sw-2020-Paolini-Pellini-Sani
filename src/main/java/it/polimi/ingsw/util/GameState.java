package it.polimi.ingsw.util;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.PlayersInTheGame;
import jdk.internal.access.JavaxCryptoSealedObjectAccess;

public interface GameState  {
    // void changeState(GameState nextState);
    boolean execute() throws IllegalSpaceException;


    void reset();

}

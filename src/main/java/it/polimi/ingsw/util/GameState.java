package it.polimi.ingsw.util;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.PlayersInTheGame;
import jdk.internal.access.JavaxCryptoSealedObjectAccess;

public interface GameState  {
    // void changeState(GameState nextState);
    void execute();


    void reset();

}

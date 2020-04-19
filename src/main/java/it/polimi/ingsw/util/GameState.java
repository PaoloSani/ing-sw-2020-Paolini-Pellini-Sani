package it.polimi.ingsw.util;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.PlayersInTheGame;
import jdk.internal.access.JavaxCryptoSealedObjectAccess;

//TODO: Meglio aggiornare il tipo di Observable T in Observer<T>, creiamo una super classe di PlayersIntheGame?
public interface GameState  {
    // void changeState(GameState nextState);
    boolean execute() throws IllegalSpaceException;

    //TODO:
    // void reset();

}

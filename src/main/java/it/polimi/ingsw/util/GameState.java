package it.polimi.ingsw.util;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

//TODO: Meglio aggiorarnare il tipo di Observable T in Observer<T>, creiamo una super classe di PlayersIntheGame?
public interface GameState extends Observer<PlayersInTheGame> {
    void changeState(GameState nextState);
    void execute();

    @Override
    void update(PlayersInTheGame message);
}

package it.polimi.ingsw.util;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public interface GameState extends Observer<PlayersInTheGame> {
    public void changeState(GameState nextState);
    public void execute();

    @Override
    void update(PlayersInTheGame message);
}

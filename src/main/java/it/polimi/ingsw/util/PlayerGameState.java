package it.polimi.ingsw.util;


import it.polimi.ingsw.model.Player;

public interface PlayerGameState extends GameState {
    public void execute(Player player);
}

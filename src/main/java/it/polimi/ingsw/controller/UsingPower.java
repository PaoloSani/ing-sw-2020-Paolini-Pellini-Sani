package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class UsingPower implements GameState {
    private Server server;

    public UsingPower(Server server) {
        this.server = server;
    }

    @Override
    public void changeState(GameState nextState) {

    }

    @Override
    public void execute() {

    }

    @Override
    public void update(PlayersInTheGame message) {

    }
}

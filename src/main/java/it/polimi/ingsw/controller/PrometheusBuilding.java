package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class PrometheusBuilding implements GameState {
    Server server;

    public PrometheusBuilding(Server server) {
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

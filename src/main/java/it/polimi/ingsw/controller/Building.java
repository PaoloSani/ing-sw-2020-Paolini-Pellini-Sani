package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class Building implements GameState {
    Server server;

    public Building(Server server) {
        this.server = server;
    }


    @Override
    public void execute() {

    }

    @Override
    public void update(PlayersInTheGame message) {

    }

    @Override
    public void changeState(GameState state) {
        server.setCurrState( state );
    }
}

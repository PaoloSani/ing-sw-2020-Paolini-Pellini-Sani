package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class Winning implements GameState {
    Server server;



    public Winning(Server server) {
        this.server = server;
    }


    @Override
    public void execute() {

    }

    @Override
    public void update(PlayersInTheGame message) {

    }

    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }


}

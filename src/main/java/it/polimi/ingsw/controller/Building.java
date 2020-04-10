package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;

public class Building implements GameState {
    Server server;
    @Override
    public void execute() {

    }

    @Override
    public void changeState() {
        server.setCurrState( new ChoosingWorker() );
    }
}

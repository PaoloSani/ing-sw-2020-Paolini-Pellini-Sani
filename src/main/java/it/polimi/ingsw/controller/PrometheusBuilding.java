package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class PrometheusBuilding implements GameState {
    private Server server;
    private Space toBuild;
    private int level;

    public PrometheusBuilding(Server server) {
        this.server = server;
    }

    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }

    @Override
    public void execute() {
        try {
            server.getCurrPlayer().buildSpace(server.getCurrWorker(), toBuild, level);
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
        }

        changeState(server.prometheusMoving);

    }


    //update: riceve una cella in cui è contenuta la posizione in cui costruire
    //execute: esegue la costruzione
    //changeState: porta in prometheusMoving
    @Override
    public void update(PlayersInTheGame message) {

    }
}

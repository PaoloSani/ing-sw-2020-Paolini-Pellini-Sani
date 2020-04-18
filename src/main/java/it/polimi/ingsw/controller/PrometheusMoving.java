package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class PrometheusMoving implements GameState {
    private Server server;
    private Space nextSpace;
    private boolean MoveUp;

    public PrometheusMoving(Server server) {
        this.server = server;
        MoveUp = false;
    }

    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }

    @Override
    public void execute() {
        if ( nextSpace.getHeight() - server.getCurrWorker().getSpace().getHeight() > 0 )
            MoveUp = true;

        if ( !MoveUp ) {
            try {
                server.getCurrPlayer().moveWorker(server.getCurrWorker(), nextSpace);
            } catch (IllegalSpaceException e) {
                e.printStackTrace();
            }
            changeState(server.building);
        }

        MoveUp = false;
    }


    //update: riceve una cella in cui è contenuta la posizione in cui muoversi
    //execute: esegue la mossa
    //changeState: porta in building
    @Override
    public void update(PlayersInTheGame message) {

    }
}

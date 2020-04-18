package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class PlacingWorkers implements GameState {
    private Server server;
    private Space space1;
    private Space space2;

    public PlacingWorkers(Server server) {
        this.server = server;
    }

    @Override
    public void changeState(GameState nextState) {
        if ( server.getCurrPlayer() == server.getChallenger() ){
            server.updateCurrPlayer();
            server.setCurrState(nextState);
        }
        else{
            server.updateCurrPlayer();
        }

    }

    @Override
    public void execute() {
        server.getCurrPlayer().getWorker1().setSpace(space1);
        server.getCurrPlayer().getWorker2().setSpace(space2);
        changeState(server.choosingWorkers);
    }

    //update: il currPlayer del Server ha scelto dove piazzare i suoi giocatori
    // lancio execute che agisce sul model
    // lancio changeState()
    @Override
    public void update(PlayersInTheGame message) {

    }


}

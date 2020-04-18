package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class UsingPower implements GameState {
    private Server server;
    private Space space;
    private int level;

    public UsingPower(Server server) {
        this.server = server;
    }

    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }

    @Override
    public void execute() {
        if ( server.getCurrPlayer().getGod() == God.CHARON ){
            if ( space.getWorker() != null ){
                changeState(server.charonSwitching);
            }
            else changeState(server.moving);
        }
        else {  //altrimenti è Prometeo
            if ( level == 0 ) changeState(server.moving);
            else changeState(server.prometheusBuilding);
        }

    }

    //update: ricevo una cella
    //se il giocatore è caronte o la cella contiene un worker e quindi change state setta charonSwitching altrimenti moving
    //se il giocatore è prometeo o chiama una build e quindi change state porta in PrometheusBuilding altrimenti Moving
    @Override
    public void update(PlayersInTheGame message) {

    }
}

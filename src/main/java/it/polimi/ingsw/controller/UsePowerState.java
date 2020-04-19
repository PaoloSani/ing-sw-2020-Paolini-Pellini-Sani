package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class UsePowerState implements GameState {
    private BackEnd backEnd;
    private Space space;
    private int level;

    public UsePowerState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public void changeState(GameState nextState) {
        backEnd.setCurrState(nextState);
    }

    @Override
    public void execute() {
        if ( backEnd.getCurrPlayer().getGod() == God.CHARON ){
            if ( space.getWorker() != null ){
                changeState(backEnd.charonSwitchState);
            }
            else changeState(backEnd.moveState);
        }
        else {  //altrimenti è Prometeo
            if ( level == 0 ) changeState(backEnd.moveState);
            else changeState(backEnd.prometheusBuildState);
        }

    }

    //update: ricevo una cella
    //se il giocatore è caronte o la cella contiene un worker e quindi change state setta charonSwitching altrimenti moving
    //se il giocatore è prometeo o chiama una build e quindi change state porta in PrometheusBuilding altrimenti Moving

}

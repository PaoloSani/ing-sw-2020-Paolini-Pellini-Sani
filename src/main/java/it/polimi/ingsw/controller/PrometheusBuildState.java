package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class PrometheusBuildState implements GameState {
    private BackEnd backEnd;
    private Space toBuild;
    private int level;

    public PrometheusBuildState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public void changeState(GameState nextState) {
        backEnd.setCurrState(nextState);
    }

    @Override
    public void execute() {
        try {
            backEnd.getCurrPlayer().buildSpace(backEnd.getCurrWorker(), toBuild, level);
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
        }

        changeState(backEnd.prometheusMoveState);

    }


    //update: riceve una cella in cui è contenuta la posizione in cui costruire
    //execute: esegue la costruzione
    //changeState: porta in prometheusMoving
    @Override
    public void update(PlayersInTheGame message) {

    }
}

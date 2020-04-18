package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class PrometheusMoveState implements GameState {
    private BackEnd backEnd;
    private Space nextSpace;
    private boolean MoveUp;

    public PrometheusMoveState(BackEnd backEnd) {
        this.backEnd = backEnd;
        MoveUp = false;
    }

    @Override
    public void changeState(GameState nextState) {
        backEnd.setCurrState(nextState);
    }

    @Override
    public void execute() {
        if ( nextSpace.getHeight() - backEnd.getCurrWorker().getSpace().getHeight() > 0 )
            MoveUp = true;

        if ( !MoveUp ) {
            try {
                backEnd.getCurrPlayer().moveWorker(backEnd.getCurrWorker(), nextSpace);
            } catch (IllegalSpaceException e) {
                e.printStackTrace();
            }
            changeState(backEnd.building);
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

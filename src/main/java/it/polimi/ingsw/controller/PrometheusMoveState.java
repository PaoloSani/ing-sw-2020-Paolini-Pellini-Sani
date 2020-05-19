package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;

public class PrometheusMoveState implements GameState {
    private BackEnd backEnd;
    private int[] toMove = new int[]{0,-1};

    public PrometheusMoveState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public boolean execute() {
        boolean result = true;
        toMove = backEnd.getGameMessage().getSpace1();
        Space nextSpace = null;
        nextSpace = backEnd.getGame().getSpace(toMove[0], toMove[1]);

        if ( nextSpace == null ) result = false;

        if ( result && (nextSpace.getHeight() - backEnd.getCurrWorker().getSpace().getHeight() <= 0)) { //non sto salendo posso muovermi
                result = backEnd.getCurrPlayer().moveWorker(backEnd.getCurrWorker(), nextSpace);
        }
        else result = false;

        backEnd.getGame().setCurrWorker(backEnd.getCurrWorker());
        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());   //Notifico la VView
        return result;
    }

    @Override
    public void reset() {
        toMove[0] = -1;
    }


    //update: riceve una cella in cui è contenuta la posizione in cui muoversi
    //execute: esegue la mossa
    //changeState: porta in building

}

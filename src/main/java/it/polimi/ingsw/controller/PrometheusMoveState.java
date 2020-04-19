package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class PrometheusMoveState implements GameState {
    private BackEnd backEnd;
    private int[] toMove = new int[]{0,-1};

    public PrometheusMoveState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public boolean execute() {
        boolean result = false;
        toMove = backEnd.getGameMessage().getSpace1();
        Space nextSpace = null;
        try {
            nextSpace = backEnd.getGame().getSpace(toMove[0], toMove[1]);
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
            return false;
        }
        if ( nextSpace.getHeight() - backEnd.getCurrWorker().getSpace().getHeight() <= 0 ) { //non sto salendo posso muovermi
            try {
                backEnd.getCurrPlayer().moveWorker(backEnd.getCurrWorker(), nextSpace);
            } catch (IllegalSpaceException e) {
                e.printStackTrace();
                return false;
            }
            result = true;
        }
        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify();   //Notifico la VView
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

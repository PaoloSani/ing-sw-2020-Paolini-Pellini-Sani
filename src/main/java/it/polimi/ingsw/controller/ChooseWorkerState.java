package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.util.Observer;
import it.polimi.ingsw.virtualView.SpaceMessage;

public class ChooseWorkerState implements GameState {
    private BackEnd backEnd;
    private Worker chosenWorker;
    private Worker otherWorker;

    public ChooseWorkerState(BackEnd backEnd) {
        this.backEnd = backEnd;
        chosenWorker = null;
        otherWorker = null;
    }

    @Override
    public void execute() {
        //all'inizio: riceve la cella che contiene il worker scelto
        //execute: controlla che il worker si posso muovere, altrimenti ritorna l'altro worker
        //Nel litegame c'è un attributo currentworker
        chosenWorker = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]).getWorker();

        if ( backEnd.getGame().isFreeToMove(chosenWorker) ){
            //Modifico attributo currentworker nel litegame con l'attuale

        }
        else {
            otherWorker = backEnd.getCurrPlayer().getOtherWorker(chosenWorker);
            if ( backEnd.getGame().isFreeToMove( otherWorker ) ){
                backEnd.setCurrWorker(otherWorker);
                //Modifico attributo currentworker nel litegame con l'altro worker

            }

        }

    }

}

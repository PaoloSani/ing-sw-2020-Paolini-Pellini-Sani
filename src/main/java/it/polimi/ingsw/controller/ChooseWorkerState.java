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
    }

    @Override
    public void execute() {
        //Nel litegame c'è un attributo currentworker


        if ( backEnd.getGame().isFreeToMove(chosenWorker) ){
            changeState(nextState()); //Modifico attributo currentworker nel litegame con l'attuale

        }
        else {
            otherWorker = backEnd.getCurrPlayer().getOtherWorker(chosenWorker);
            if ( backEnd.getGame().isFreeToMove( otherWorker ) ){
                backEnd.setCurrWorker(otherWorker);
                changeState(nextState());//Modifico attributo currentworker nel litegame con l'altro worker

            }
            else changeState(backEnd.removePlayerState);
        }

    }

    private GameState nextState(){
        if ( backEnd.getCurrPlayer().getGod() == God.CHARON || backEnd.getCurrPlayer().getGod() == God.PROMETHEUS ){
            return backEnd.usePowerState;
        }
        return backEnd.moveState;
    }

    @Override
    public void changeState(GameState nextState) {
        backEnd.setCurrState(nextState);
    }


    }

    //update: riceve la cella che contiene il worker scelto
    //execute: controlla che il worker si posso muovere, altrimenti ritorna l'altro worker
    //controlla che l'altro worker si possa muovere altrimenti la FSM si sposta su removingPlayer
    //change state si muove su vari stati a seconda del caso che
}

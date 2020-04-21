package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.util.GameState;


//TODO: Paolo
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
    public boolean execute() {
        //all'inizio: riceve la cella che contiene il worker scelto
        //execute: controlla che il worker si posso muovere, altrimenti ritorna l'altro worker
        //Nel litegame c'è un attributo currentworker

            chosenWorker = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]).getWorker();
            if ( chosenWorker == null ) return false;


        if ( backEnd.getGame().isFreeToMove(chosenWorker) ){
            //Modifico attributo currentworker nel backend
            backEnd.setCurrWorker(chosenWorker);
            //lo scrivo nel litegame
            backEnd.getGame().setCurrWorker(chosenWorker);
        }
        else {
            otherWorker = backEnd.getCurrPlayer().getOtherWorker(chosenWorker);
            if ( backEnd.getGame().isFreeToMove( otherWorker ) ){
                //lo scrivo nel backend
                backEnd.setCurrWorker(otherWorker);
                //Modifico attributo currentworker nel litegame
                backEnd.getGame().setCurrWorker(otherWorker);
            }
            else {
                //nel litegame scrivo che il currWorker è nullo, cioè il currPlayer ha perso
                backEnd.getGame().setCurrWorker(null);
                backEnd.setToRemove(backEnd.getCurrPlayer());
            }
        }
        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify();   //Notifico la VView
        return true;
    }

    @Override
    public void reset() {
        otherWorker = null;
        chosenWorker = null;
    }

}

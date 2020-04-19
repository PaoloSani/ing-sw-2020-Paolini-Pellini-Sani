package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.util.GameState;

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
        try {
            chosenWorker = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]).getWorker();
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
            return false;
        }

        if ( backEnd.getGame().isFreeToMove(chosenWorker) ){
            //Modifico attributo currentworker nel backend e nel litegame con l'attuale
            backEnd.setCurrWorker(chosenWorker);
            //lo scrivo nel litegame
        }
        else {
            otherWorker = backEnd.getCurrPlayer().getOtherWorker(chosenWorker);
            if ( backEnd.getGame().isFreeToMove( otherWorker ) ){
                backEnd.setCurrWorker(otherWorker);
                //Modifico attributo currentworker nel litegame con l'altro worker
            }
            else {
                //nel litegame scrivo che il currWorker è nullo, cioè il currPlayer è null
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

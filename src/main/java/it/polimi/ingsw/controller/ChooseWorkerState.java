package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.util.GameState;


/**
 * Class that represents the status of the FSM in which the current player chooses the worker to play with
 */

public class ChooseWorkerState implements GameState {
    private BackEnd backEnd;
    private Worker chosenWorker;
    private Worker otherWorker;

    public ChooseWorkerState(BackEnd backEnd) {
        this.backEnd = backEnd;
        chosenWorker = null;
        otherWorker = null;
    }

    /**
     * @return true in case the execute was successful, meaning that the method has chosen the worker correctly in the game table.
     */
    @Override
    public boolean execute() {
        boolean result = true;
        boolean blockedByHypnus = false;
        //all'inizio: riceve la cella che contiene il worker scelto
        //execute: controlla che il worker si posso muovere, altrimenti ritorna l'altro worker
        //Nel litegame c'è un attributo currentworker

            chosenWorker = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]).getWorker();
            if ( chosenWorker == null || chosenWorker.getPlayer() != backEnd.getCurrPlayer() ) {
                result = false;
                backEnd.getGame().getLiteGame().setCurrWorker(5,5);
            }
            else {
                otherWorker = backEnd.getCurrPlayer().getOtherWorker(chosenWorker);
            }

        if ( result ) {
            if ( backEnd.getCurrPlayer().getGod() != God.HYPNUS                                 &&  // vera solo se la divinità corrente non è hypnus
                    backEnd.getGame().getConstraint().hypnusBlocks()                            &&  // vera se hypnus è presente in gioco
                    chosenWorker.getSpace().getHeight() > otherWorker.getSpace().getHeight()    ) { // vera se il client non ha passato un worker valido
                    blockedByHypnus = true;

            }

            if ( !blockedByHypnus && backEnd.getGame().isFreeToMove(chosenWorker) ) {
                //Modifico attributo currentworker nel backend
                backEnd.setCurrWorker(chosenWorker);
                //lo scrivo nel litegame
                backEnd.getGame().setCurrWorker(chosenWorker);
            } else {
                if ( backEnd.getGame().isFreeToMove(otherWorker) ) {
                    //lo scrivo nel backend
                    backEnd.setCurrWorker(otherWorker);
                    //Modifico attributo currentworker nel litegame
                    backEnd.getGame().setCurrWorker(otherWorker);
                } else {
                    //nel litegame scrivo che il currWorker è nullo, cioè il currPlayer ha perso
                    backEnd.getGame().setCurrWorker(null);
                    backEnd.setToRemove(backEnd.getCurrPlayer());
                }
            }
        }
        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());   //Notifico la VView
        return result;
    }

    /**
     * Sets the counters back to 0 and the boolean constraints to false
     */
    @Override
    public void reset() {
        otherWorker = null;
        chosenWorker = null;
    }

}

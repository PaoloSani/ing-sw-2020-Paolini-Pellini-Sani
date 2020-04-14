package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.util.GameState;

public class ChoosingWorker implements GameState {
    private Server server;
    private Worker chosenWorker;
    private Worker otherWorker;

    @Override
    public void execute() {

        if ( server.getGame().isFreeToMove(chosenWorker) ){
            changeState();
        }
        else {
            otherWorker = server.getCurrPlayer().getOtherWorker(chosenWorker));
            if ( server.getGame().isFreeToMove( otherWorker ) )
                server.setCurrWorker(otherWorker);

            else
                changeState();
        }

    }

    private void nextState(){
        if ( )
    }

    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }

    //update: riceve la cella che contiene il worker scelto
    //execute: controlla che il worker si posso muovere, altrimenti ritorna l'altro worker
    // controlla che l'altro worker si possa muovere altrimenti la FSM si sposta su removingPlayer
    //change state si muove su
}

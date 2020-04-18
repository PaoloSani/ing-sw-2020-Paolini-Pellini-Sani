package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class ChoosingWorker implements GameState {
    private Server server;
    private Worker chosenWorker;
    private Worker otherWorker;

    public ChoosingWorker(Server server) {
        this.server = server;
    }

    @Override
    public void execute() {

        if ( server.getGame().isFreeToMove(chosenWorker) ){
            changeState(nextState());
        }
        else {
            otherWorker = server.getCurrPlayer().getOtherWorker(chosenWorker);
            if ( server.getGame().isFreeToMove( otherWorker ) ){
                server.setCurrWorker(otherWorker);
                changeState(nextState());
            }
            else changeState(server.removingPlayer);
        }

    }

    @Override
    public void update(PlayersInTheGame message) {

    }

    private GameState nextState(){
        if ( server.getCurrPlayer().getGod() == God.CHARON || server.getCurrPlayer().getGod() == God.PROMETHEUS ){
            return server.usingPower;
        }
        return server.moving;
    }

    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }

    //update: riceve la cella che contiene il worker scelto
    //execute: controlla che il worker si posso muovere, altrimenti ritorna l'altro worker
    //controlla che l'altro worker si possa muovere altrimenti la FSM si sposta su removingPlayer
    //change state si muove su vari stati a seconda del caso che
}

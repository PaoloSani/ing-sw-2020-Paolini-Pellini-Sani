package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

//TODO: sistemare le condizioni per counterArtemis e per l
public class MoveState implements GameState {
    private BackEnd backEnd;
    private Space nextSpace;
    int counterArtemis;
    private Space lastSpaceArtemis;
    private boolean returnBack;

    public MoveState(BackEnd backEnd) {
        this.backEnd = backEnd;
        counterArtemis = 0;
        lastSpaceArtemis = null;
        returnBack = false;
    }


    @Override
    public void changeState(GameState nextState) {
        backEnd.setCurrState(nextState);
    }

    @Override
    public void execute() {
        if (backEnd.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 0) {
            lastSpaceArtemis = backEnd.getCurrWorker().getSpace();
            counterArtemis++;
        }

        if (backEnd.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 1) {
            if (lastSpaceArtemis == nextSpace   )
                returnBack = true;
            else counterArtemis++;
        }

        if (!returnBack)
        {
            try {
                backEnd.getCurrPlayer().moveWorker(backEnd.getCurrWorker(), nextSpace);
            } catch (IllegalSpaceException e) {
                e.printStackTrace();
            }
        }

        //TODO: devo andare in building solo se non ha lanciato l'eccezione -> ritorno true se il metodo ha effettuato la mossa, gestisco la vittoria con un messaggio model->view
        //TODO: come dico che tritone ha smesso di muoversi?
        if (    (backEnd.getCurrPlayer().getGod() != God.TRITON && backEnd.getCurrPlayer().getGod() != God.ARTEMIS) ||
                (backEnd.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 2)                           ){
            resetMoving();
            changeState(backEnd.buildState);
        }

        //caso in cui Tritone esce dal perimetro
        if ( backEnd.getCurrPlayer().getGod() == God.TRITON &&   //cella fuori dal perimetro
            nextSpace.getX() > 0 && nextSpace.getX() < 4 &&
            nextSpace.getY() > 0 && nextSpace.getY() < 4 ){
            resetMoving();
            changeState(backEnd.buildState);
        }

        returnBack = false;

        //continuo a potermi muovere perché o sono artemide o sono tritone
    }

    public void resetMoving(){
        counterArtemis = 0;
        lastSpaceArtemis = null;
        returnBack = false;
    }

    //update: riceve una cella in cui è contenuto la cella dove andare
    //execute: esegue la move
    //changeState: porta in building




}

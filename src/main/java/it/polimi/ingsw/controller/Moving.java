package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class Moving implements GameState {
    private Server server;
    private Space nextSpace;
    private int counterArtemis;
    private Space lastSpaceArtemis;
    private boolean returnBack;

    public Moving(Server server) {
        this.server = server;
        counterArtemis = 0;
        lastSpaceArtemis = null;
        returnBack = false;
    }


    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }

    @Override
    public void execute() {
        if (server.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 0) {
            lastSpaceArtemis = server.getCurrWorker().getSpace();
            counterArtemis++;
        }

        if (server.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 1) {
            if (lastSpaceArtemis == nextSpace   )
                returnBack = true;
            else counterArtemis++;
        }

        if (!returnBack)
        {
            try {
                server.getCurrPlayer().moveWorker(server.getCurrWorker(), nextSpace);
            } catch (IllegalSpaceException e) {
                e.printStackTrace();
            }
        }

        //TODO: devo andare in building solo se non ha lanciato l'eccezione -> ritorno true se il metodo ha effettuato la mossa, gestisco la vittoria con un messaggio model->view
        //TODO: come dico che tritone ha smesso di muoversi?
        if (    (server.getCurrPlayer().getGod() != God.TRITON && server.getCurrPlayer().getGod() != God.ARTEMIS) ||
                (server.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 2)                           ){
            resetMoving();
            changeState(server.building);
        }

        //caso in cui Tritone esce dal perimetro
        if ( server.getCurrPlayer().getGod() == God.TRITON &&   //cella fuori dal perimetro
            nextSpace.getX() > 0 && nextSpace.getX() < 4 &&
            nextSpace.getY() > 0 && nextSpace.getY() < 4 ){
            resetMoving();
            changeState(server.building);
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
    @Override
    public void update(PlayersInTheGame message) {

    }



}

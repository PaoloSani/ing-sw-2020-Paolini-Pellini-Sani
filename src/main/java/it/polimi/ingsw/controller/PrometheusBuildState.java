package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class PrometheusBuildState implements GameState {
    private BackEnd backEnd;
    private Space toBuild;
    private int level;

    public PrometheusBuildState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public void execute() {

        try {
            toBuild = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
        }

        level = backEnd.getGameMessage().getLevel();

        try {
            backEnd.getCurrPlayer().buildSpace(backEnd.getCurrWorker(), toBuild, level);
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
        }

        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify();   //Notifico la VView

    }



    @Override
    public void reset(){
        toBuild = null;
        level = 0;
    }


    //update: riceve una cella in cui è contenuta la posizione in cui costruire
    //execute: esegue la costruzione
    //changeState: porta in prometheusMoving

}

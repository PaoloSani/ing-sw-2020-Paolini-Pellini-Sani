package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;

//TODO: Riccardo
public class PrometheusBuildState implements GameState {
    private BackEnd backEnd;
    private Space toBuild;
    private int level;

    public PrometheusBuildState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public boolean execute() {

        toBuild = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
        if( toBuild == null ) return false;

        level = backEnd.getGameMessage().getLevel();


        if (! backEnd.getCurrPlayer().buildSpace(backEnd.getCurrWorker(), toBuild, level))
           return false;


        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());   //Notifico la VView
        return true;
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

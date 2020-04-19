package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class PrometheusBuildState implements GameState {
    private BackEnd backEnd;
    private int level;

    public PrometheusBuildState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public boolean execute(){
        int[] toBuild;
        toBuild = backEnd.getGameMessage().getSpace1(); //Prendo la cella dove devo costruire(cordinate)
        level = backEnd.getGameMessage().getLevel(); //Prendo il pezzo che devo costruire
        Space space = null; //Converto to build in una space
        try {
            space = backEnd.getGame().getSpace(toBuild[0], toBuild[1]);
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
        }
        backEnd.getGameMessage().setPrometheusMoving(false); //Non devo salire di livello nel prossimo turno
        try {
            backEnd.getCurrPlayer().buildSpace(backEnd.getCurrWorker(), space, level);
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
            //TODO notify e modifica del Lite game nel model
            //backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
            //backEnd.getGame().getLiteGame().notify();   //Notifico la VView

            //reset();

        }
    }
}
    /*
    @Override
    public void reset(){
        toBuild = null;
        level = 0;
    }
    */

    //update: riceve una cella in cui è contenuta la posizione in cui costruire
    //execute: esegue la costruzione
    //changeState: porta in prometheusMoving



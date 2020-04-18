package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.util.Observer;
import it.polimi.ingsw.virtualView.SpaceMessage;

public class PlaceWorkersState implements GameState, Observer<SpaceMessage> {
    private BackEnd backEnd;
    private Space space1;
    private Space space2;

    public PlaceWorkersState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public void changeState(GameState nextState) {
        if ( backEnd.getCurrPlayer() == backEnd.getChallenger() ){
            backEnd.updateCurrPlayer();
            backEnd.setCurrState(nextState);
        }
        else{
            backEnd.updateCurrPlayer();
        }

    }

    @Override
    public void execute() {
        backEnd.getCurrPlayer().getWorker1().setSpace(space1);
        backEnd.getCurrPlayer().getWorker2().setSpace(space2);
        //A questo punto il model modifica il liteGame
        //notify del LiteGame
        changeState(backEnd.choosingWorkers);

    }

    //update: il currPlayer del Server ha scelto dove piazzare i suoi giocatori
    // lancio execute che agisce sul model
    // lancio changeState()
    @Override
    public void update(SpaceMessage message) {
        //Copio celle passatemi dalla Workers positions e le converto in celle del model
    }
}

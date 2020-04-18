package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class CharonSwitchState implements GameState {
    private BackEnd backEnd;
    private Space spaceToSwitch;

    public CharonSwitchState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public void changeState(GameState nextState) {
        backEnd.setCurrState(nextState);
    }

    //chiamata a charonPower in game
    @Override
    public void execute() {
        try {
            backEnd.getGame().charonPower( backEnd.getCurrWorker(), spaceToSwitch.getWorker() );
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
        }
        //TODO: devo andare in moving solo se la mossa fatta non ha lanciato l'eccezione
        changeState(backEnd.moving);
    }

    //update: riceve una cella in cui è contenuto il worker da switchare
    //execute: esegue lo switch
    //changeState: porta in moving
    @Override
    public void update(PlayersInTheGame message) {

    }
}

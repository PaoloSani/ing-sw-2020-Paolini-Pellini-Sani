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
        spaceToSwitch = null;
    }



    //chiamata a charonPower in game
    @Override
    public void execute() {
        spaceToSwitch = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);

        try {
            backEnd.getGame().charonPower( backEnd.getCurrWorker(), spaceToSwitch.getWorker() );
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
        }

    }

    //update: riceve una cella in cui è contenuto il worker da switchare
    //execute: esegue lo switch
    //changeState: porta in moving
}

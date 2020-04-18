package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class CharonSwitching implements GameState {
    private Server server;
    private Space spaceToSwitch;

    public CharonSwitching(Server server) {
        this.server = server;
    }

    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }

    //chiamata a charonPower in game
    @Override
    public void execute() {
        try {
            server.getGame().charonPower( server.getCurrWorker(), spaceToSwitch.getWorker() );
        } catch (IllegalSpaceException e) {
            e.printStackTrace();
        }
        //TODO: devo andare in moving solo se la mossa fatta non ha lanciato l'eccezione
        changeState(server.moving);
    }

    //update: riceve una cella in cui è contenuto il worker da switchare
    //execute: esegue lo switch
    //changeState: porta in moving
    @Override
    public void update(PlayersInTheGame message) {

    }
}

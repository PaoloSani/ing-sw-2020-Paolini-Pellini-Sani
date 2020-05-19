package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;

public class WinState implements GameState {
    BackEnd backEnd;


    public WinState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }


    @Override
    public boolean execute() {
        backEnd.getGame().setWinner(true);
        backEnd.getGame().refreshLiteGame();
        //todo ho modificato questo .notify(backEnd.getGame().getLiteGame())
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());
        return true;
    }

    @Override
    public void reset() {

    }

}

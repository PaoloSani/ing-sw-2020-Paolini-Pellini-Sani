package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Challenger;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.util.Observer;
import it.polimi.ingsw.virtualView.PlayersInTheGame;


public class SetPlayersState implements Observer<PlayersInTheGame>, GameState {
    private BackEnd backEnd;
    private String name;
    private String name1;
    private String name2;
    private God god1;
    private God god2;
    private God god3;

    public SetPlayersState(BackEnd backEnd) {
        this.backEnd = backEnd;
        this.name = null;
        this.name1 = null;
        this.name2 = null;
        this.god1 = null;
        this.god2 = null;
        this.god3 = null;
    }

    @Override
    public void execute() {
        backEnd.setPlayer2( new Player( this.name, god1, backEnd.getGame()) );
        backEnd.setPlayer3( new Player( this.name1, god2, backEnd.getGame()) );
        backEnd.setChallenger( new Challenger( this.name2, god3, backEnd.getGame()) );
        //invio la classe litegame message dal model alla view
        //server.getGame().message.notify()
        changeState(backEnd.placingWorkers);
    }


    @Override
    public void changeState(GameState nextState) {
        backEnd.setCurrState(nextState);
    }


    //update: riceve tramite notifica i tre nickname dalla virtual view e le tre divinità e lancia execute

    @Override
    public void update(PlayersInTheGame message) {
        god1 = message.getGod1();
        god2 = message.getGod2();
        god3 = message.getGod3();

        name = message.getName1();
        name1 = message.getName2();
        name2 = message.getName3();
        execute();
    }
}

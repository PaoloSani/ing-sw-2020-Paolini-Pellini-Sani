package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.GameState;


public class SetPlayersState implements  GameState {
    private BackEnd backEnd;
    private String name1;
    private String name2;
    private String name3;
    private God god1;
    private God god2;
    private God god3;

    public SetPlayersState(BackEnd backEnd) {
        this.backEnd = backEnd;
        this.name1 = null;
        this.name2 = null;
        this.name3 = null;
        this.god1 = null;
        this.god2 = null;
        this.god3 = null;
    }

    @Override
    public boolean execute() {
        god1 = backEnd.getGameMessage().getGod1();
        god2 = backEnd.getGameMessage().getGod2();
        god3 = backEnd.getGameMessage().getGod3();

        name1 = backEnd.getGameMessage().getName1();
        name2 = backEnd.getGameMessage().getName2();
        name3 = backEnd.getGameMessage().getName3();

        backEnd.setChallenger(new Player(this.name1, god1, backEnd.getGame()));
        backEnd.setPlayer2(new Player(this.name2, god2, backEnd.getGame()));
        backEnd.setPlayer3(new Player(this.name3, god3, backEnd.getGame()));

        //se hypnus è presente in gioco, la classe Player ha già settato il costraint

        backEnd.getGame().setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());
        //invio la classe litegame backEnd.getGameMessage dal model alla view
        backEnd.getGame().getLiteGame().addObservers(backEnd.getGameMessage().getFrontEnd());
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());
        return true;
    }

    @Override
    public void reset() {
        this.name1 = null;
        this.name2 = null;
        this.name3 = null;
        this.god1 = null;
        this.god2 = null;
        this.god3 = null;
    }
}

    //update: riceve tramite notifica i tre nickname dalla virtual view e le tre divinità e lancia execute


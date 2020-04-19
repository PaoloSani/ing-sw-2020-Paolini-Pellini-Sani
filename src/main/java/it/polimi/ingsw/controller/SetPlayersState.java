package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;


public class SetPlayersState implements  GameState {
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
    public boolean execute() {
        god1 = backEnd.getGameMessage.getGod1();
        god2 = backEnd.getGameMessage.getGod2();
        god3 = backEnd.getGameMessage.getGod3();

        name = backEnd.getGameMessage.getName1();
        name1 = backEnd.getGameMessage.getName2();
        name2 = backEnd.getGameMessage.getName3();
        backEnd.setPlayer2( new Player( this.name, god1, backEnd.getGame()) );
        backEnd.setPlayer3( new Player( this.name1, god2, backEnd.getGame()) );
        backEnd.setChallenger( new Player( this.name2, god3, backEnd.getGame()) );

        //invio la classe litegame backEnd.getGameMessage dal model alla view
        //server.getGame().backEnd.getGameMessage.notify()

    }



    //update: riceve tramite notifica i tre nickname dalla virtual view e le tre divinità e lancia execute
    }
}

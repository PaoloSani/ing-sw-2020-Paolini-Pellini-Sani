package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Challenger;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.GameState;

public class SettingPlayers implements GameState {
    Server server;
    String name;
    String name1;
    String name2;
    God god1;
    God god2;
    God god3;

    public SettingPlayers(Server server) {
        this.server = server;
        this.name = null;
        this.name1 = null;
        this.name2 = null;
        this.god1 = null;
        this.god2 = null;
        this.god3 = null;
    }

    @Override
    public void execute() {
        server.setPlayer2( new Player( this.name, god1, server.getGame()));
        server.setPlayer3( new Player( this.name1, god2, server.getGame()));
        server.setChallenger( new Challenger( this.name2, god3, server.getGame()));
        changeState(server.placingWorkers);
    }


    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }


    //update: riceve tramite notifica i tre nickname dalla virtual view e le tre divinità e lancia execute
    //

}

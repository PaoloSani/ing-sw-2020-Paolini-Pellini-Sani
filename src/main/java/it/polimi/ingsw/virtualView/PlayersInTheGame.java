package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.controller.Server;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.List;


public class PlayersInTheGame extends Observable<PlayersInTheGame> {
    Server server;
    String name1;       // Challenger: sceglie le carte e gioca per ultimo
    String name2;       // Start Player: giocatore che gioca per primo il turno e pesca per primo la carta
    String name3;

    God god1;
    God god2;
    God god3;
    private List<Observer<PlayersInTheGame>> observers = new ArrayList<>();

    public void addObservers(Observer<PlayersInTheGame> observer){
        observers.add(observer);
    }

    public PlayersInTheGame(Server server) {
        this.server = server;
        addObservers((Observer<PlayersInTheGame>) server.settingPlayers);
    }

    public void notify(PlayersInTheGame message){
        for(Observer<PlayersInTheGame> observer: observers){
            observer.update(message);
        }
    }


    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public God getGod1() {
        return god1;
    }

    public void setGod1(God god1) {
        this.god1 = god1;
    }

    public God getGod2() {
        return god2;
    }

    public void setGod2(God god2) {
        this.god2 = god2;
    }

    public God getGod3() {
        return god3;
    }

    public void setGod3(God god3) {
        this.god3 = god3;
    }

}

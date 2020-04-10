package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.controller.Server;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.List;

public class PlayersInTheGame extends Observable<PlayersInTheGame>/*, Observer<Game>*/ {

    private final List<Observer<PlayersInTheGame>> observers = new ArrayList<>();

    String nickname1;
    God god1;

    String nickname2;
    God god2;

    String nickname3;
    God god3;

    public String getNickname1() {
        return nickname1;
    }

    public void setNickname1(String nickname1) {
        this.nickname1 = nickname1;
    }

    public God getGod1() {
        return god1;
    }

    public void setGod1(God god1) {
        this.god1 = god1;
    }

    public String getNickname2() {
        return nickname2;
    }

    public void setNickname2(String nickname2) {
        this.nickname2 = nickname2;
    }

    public God getGod2() {
        return god2;
    }

    public void setGod2(God god2) {
        this.god2 = god2;
    }

    public String getNickname3() {
        return nickname3;
    }

    public void setNickname3(String nickname3) {
        this.nickname3 = nickname3;
    }

    public God getGod3() {
        return god3;
    }

    public void setGod3(God god3) {
        this.god3 = god3;
    }

    @Override
    public void addObservers(Observer<PlayersInTheGame> observer) {
            observers.add(observer);
    }

    @Override
    public void notify(PlayersInTheGame playerToSet) {
        for(Observer<PlayersInTheGame> observer : observers) {
            observer.update(playerToSet);
        }
    }

 /*  @Override
    public void update(Game message) {

    }*/
}

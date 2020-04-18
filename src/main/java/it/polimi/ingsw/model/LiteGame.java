package it.polimi.ingsw.model;

import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.List;

//Classe da passare come messaggio alla virtual view
public class LiteGame extends Observable<LiteGame> {


    public LiteGame() {
    }

    private List<Observer<LiteGame>> observers = new ArrayList<>();

    public void addObservers(Observer<LiteGame> observer){
        observers.add(observer);
    }

    public void removeObservers(Observer<LiteGame> observer){
        observers.remove(observer);
    }

    public void notify(LiteGame message){
        for(Observer<LiteGame> observer: observers){
            observer.update(message);
        }
    }

    //notify alla virtual view
}

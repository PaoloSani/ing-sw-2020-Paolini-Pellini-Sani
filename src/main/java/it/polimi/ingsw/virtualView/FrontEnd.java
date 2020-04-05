package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.List;

public class FrontEnd implements Observable<FrontEnd> {

    private final List<Observer<FrontEnd>> observers = new ArrayList<>();

    @Override
    public void addObservers(Observer<FrontEnd> observer) {
        observers.add(observer);
    }

    @Override
    public void notify(FrontEnd message) {
        for(Observer<FrontEnd> observer : observers) {
            observer.update(message);
        }
    }
}

package it.polimi.ingsw.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable is an implementation of the observable object of the observer pattern
 * @param <T> : type of the message to send to the observer
 */
public class Observable<T> {

    /**
     * List of observer of the object
     */
    private List<Observer<T>> observers = new ArrayList<>();

    /**
     * Add an observer to the observer list
     * @param observer : observer to add
     */
    public void addObservers(Observer<T> observer){
        observers.add(observer);
    }

    /**
     * Notifies the observers
     * @param message : message to send to the observer
     */
    public void notify(T message){
        for(Observer<T> observer: observers){
            observer.update(message);
        }
    }

}

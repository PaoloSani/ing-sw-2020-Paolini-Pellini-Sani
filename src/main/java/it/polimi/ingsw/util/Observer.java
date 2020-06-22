package it.polimi.ingsw.util;

/**
 * an interface for the observer object of the observer pattern
 * @param <T> : type of the object observed
 */
public interface Observer<T> {

    /**
     * processes an update from the observed object
     * @param message: message received from the observable instance
     */
    void update(T message);
}

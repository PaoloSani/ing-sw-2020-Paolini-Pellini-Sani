package it.polimi.ingsw.util;

import java.util.ArrayList;
import java.util.List;

public interface Observable<T> {

    public void addObservers(Observer<T> observer);

    public void notify(T message);

}

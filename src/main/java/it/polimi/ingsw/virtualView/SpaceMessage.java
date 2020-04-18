package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.List;

public class SpaceMessage extends Observable <SpaceMessage> {
    private FrontEnd frontEnd;
    //Utilizzati per settare i due workers di ogni players
    int x1, x2, y1, y2;
    // Lo uso per differenziare i vari update negli stati del controller
    int flag;


    private List<Observer<SpaceMessage>> observers = new ArrayList<>();

    public void addObservers(Observer<SpaceMessage> observer){
        observers.add(observer);
    }

    public SpaceMessage(FrontEnd frontend) {
        this.frontEnd = frontend;
        addObservers((Observer<SpaceMessage>) frontend.getBackEnd().placeWorkersState);
        addObservers((Observer<SpaceMessage>) frontend.getBackEnd().chooseWorkerState);
    }

    public void notify(SpaceMessage message){
        for(Observer<SpaceMessage> observer: observers){
            observer.update(message);
        }
    }





}

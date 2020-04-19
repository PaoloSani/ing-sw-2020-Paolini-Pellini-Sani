package it.polimi.ingsw.model;

import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.List;

//Classe da passare come messaggio alla virtual view
public class LiteGame extends Observable<LiteGame> {

    private String name1;       // Challenger: sceglie le carte e gioca per ultimo
    private String name2;       // Start Player: giocatore che gioca per primo il turno e pesca per primo la carta
    private String name3;

    private God god1;
    private God god2;
    private God god3;

    private String currPlayer; //TODO:serve??
    private int[] currWorker = new int[]{-1,0};

    private int level1;
    private int level2;
    private int level3;
    private int dome;



    private char[][][] table;

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

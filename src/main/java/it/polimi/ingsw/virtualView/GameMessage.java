package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.List;

public class --GameMessage extends Observable <GameMessage> {
    private FrontEnd frontEnd;

    //Utilizzati per settare i due workers di ogni players
    private int[] space1 = new int[2]{-1,0};
    private int[] space2 = new int[2]{0,0};

    // Lo uso per differenziare il caso della Build
    int level;

    private String name1;       // Challenger: sceglie le carte e gioca per ultimo
    private String name2;       // Start Player: giocatore che gioca per primo il turno e pesca per primo la carta
    private String name3;

    private God god1;
    private God god2;
    private God god3;

    private boolean charonSwitching;


    private List<Observer<GameMessage>> observers = new ArrayList<>();

    public void addObservers(Observer<GameMessage> observer) {
        observers.add(observer);
    }

    public int[] getSpace1() {
        return space1;
    }

    public int[] getSpace2() {
        return space2;
    }


    public int getLevel() {
        return level;
    }

    public boolean isCharonSwitching() {
        return charonSwitching;
    }

    public GameMessage(FrontEnd frontend) {
        this.charonSwitching = false;       //Viene settato a true dal frontend, dopo viene resettato a false nella move
        this.frontEnd = frontend;
        addObservers( frontend.getBackEnd() );

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

    public void notify(GameMessage message) {
        for (Observer<GameMessage> observer : observers) {
            observer.update(message);
        }
    }
}

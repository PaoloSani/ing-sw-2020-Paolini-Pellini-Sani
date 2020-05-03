package it.polimi.ingsw.client;

import it.polimi.ingsw.model.God;

public class ClientMessage {
    String name;
    God god;
    String action;

    //TODO: controlla che siano diverse!
    int [] space1;
    int [] space2;

    public String getName() {
        return name;
    }

    public God getGod() {
        return god;
    }

    public void setGod(God god) {
        this.god = god;
    }

    public void setSpace2(int[] space2) {
        this.space2 = space2;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int[] getSpace1() {
        return space1;
    }

    public void setSpace1(int[] space1) {
        this.space1 = space1;
    }

    public int[] getSpace2() {
        return space2;
    }
}

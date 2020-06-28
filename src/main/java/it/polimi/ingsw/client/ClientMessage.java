package it.polimi.ingsw.client;

import it.polimi.ingsw.model.God;


import java.io.Serializable;

/**
 * It is the message sent to the server to describe the client's choices.
 */

public class ClientMessage implements Serializable {

    /**
     * It is the name of the client
     */
    String name;

    /**
     * It is the god chosen by the client.
     */
    God god;

    /**
     * It is the action performed by the client during his turn.
     */
    String action;

    /**
     * It is the space chosen by the client.
     */
    int [] space1 = null;

    /**
     * It is the second space chosen by the client when he's placing his workers.
     */
    int [] space2 = null;

    /**
     * It tells how many level the client wants to build.
     */
    int levelToBuild;

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

    public void setName(String name) { this.name = name; }

    public int[] getSpace1() {
        return space1;
    }

    public void setSpace1(int[] space1) {
        this.space1 = space1;
    }

    public int[] getSpace2() {
        return space2;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getLevelToBuild() {
        return levelToBuild;
    }

    public void setLevelToBuild(int levelToBuild) {
        this.levelToBuild = levelToBuild;
    }

}

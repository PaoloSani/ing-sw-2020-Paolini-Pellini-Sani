package it.polimi.ingsw.model;

public class Worker {
    private Space space = null;
    private Player player;

    public Worker(Player player) {
        this.player = player;
    }

    public Space getSpace() {
        return space;
    }

    public Player getPlayer() {
        return player;
    }

    public void setSpace(Space space) {
        this.space = space;
    }


}


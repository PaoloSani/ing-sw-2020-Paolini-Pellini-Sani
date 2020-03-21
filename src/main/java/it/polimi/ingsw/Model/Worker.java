package it.polimi.ingsw.Model;

public class Worker {
    private Space space;
    private Player player;

    public Worker(Space space, Player player) {
        this.space = space;
        this.player = player;
    }

    public Space getSpace() {
        return space;
    }

    public Player getPlayer() {
        return player;
    }


}


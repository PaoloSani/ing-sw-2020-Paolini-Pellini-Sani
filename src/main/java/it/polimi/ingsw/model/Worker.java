package it.polimi.ingsw.model;

/**
 * Class used to describe the worker
 */
public class Worker {
    private Space space = null;
    private Player player;

    public Worker( Player player ) {
        this.player = player;
    }

    public Space getSpace() {
        return space;
    }

    public Player getPlayer() {
        return player;
    }

    public void setSpace( Space space ) {
        if(space == null){
            this.space = null;
        }
        else{
            this.space = space;
            this.space.setWorker(this);
        }
    }

}


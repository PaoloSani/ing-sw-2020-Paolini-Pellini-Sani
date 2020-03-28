package it.polimi.ingsw.model;

public class Space {
    private final int x;
    private final int y;
    private int height;
    private Worker worker;


    public Space( int x, int y ) {
        this.x = x;
        this.y = y;
        this.height = 0;
        this.worker = null;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setHeight( int height ) {
        this.height = height;
    }

    public void setWorker( Worker worker ) {
        this.worker = worker;
    }

}

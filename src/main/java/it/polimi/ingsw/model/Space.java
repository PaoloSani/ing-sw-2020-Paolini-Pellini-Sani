package it.polimi.ingsw.model;

public class Space {
    private final int x;
    private final int y;
    private int height;

    private boolean dome;

    private Worker worker;

    public Space( int x, int y ) {
        this.x = x;
        this.y = y;
        this.height = 0;
        this.worker = null;
        this.dome = false;
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
        if ( height == 4 )
            setDome();
    }

    public void setWorker( Worker worker ) {
        this.worker = worker;
    }

    public boolean isDomed() {
        return dome;
    }

    public void setDome() {
        this.dome = true;
    }

}

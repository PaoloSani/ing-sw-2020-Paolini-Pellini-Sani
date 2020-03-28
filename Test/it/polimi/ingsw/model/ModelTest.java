package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTest {
    private Model model = new Model();
    private Player player1 = new Player("test1");
    private Player player2 = new Player("test2");
    private Worker myWorker = new Worker(player1);
    private Worker oppWorker = new Worker(player2);

    @Test (expected = IllegalSpaceException.class)
    public void negativeX() {
        myWorker.setSpace(new Space(0,0));
        oppWorker.setSpace(new Space(1,0));
        model.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedX() {
        myWorker.setSpace(new Space(4,0));
        oppWorker.setSpace(new Space(3,0));
        model.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void negativeY() {
        myWorker.setSpace(new Space(0,0));
        oppWorker.setSpace(new Space(0,1));
        model.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedY() {
        myWorker.setSpace(new Space(0,4));
        oppWorker.setSpace(new Space(0,3));
        model.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void negativeX() {
        myWorker.setSpace(new Space(0,0));
        oppWorker.setSpace(new Space(1,0));
        model.charonPower(myWorker, oppWorker);
    }
}
package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTest {
    private Model model = new Model();
    private Player player1 = new Player("test1", model);
    private Player player2 = new Player("test2", model);
    private Worker myWorker = new Worker(player1);
    private Worker oppWorker = new Worker(player2);

    //*******************//
    //Test su charonPower//
    //*******************//

    @Test (expected = IllegalSpaceException.class)
    public void negativeXCharon() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(0,0));
        oppWorker.setSpace(model.getSpace(1,0));
        model.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedXCharon() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(4,0));
        oppWorker.setSpace(model.getSpace(3,0));
        model.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void negativeYCharon() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(0,0));
        oppWorker.setSpace(model.getSpace(0,1));
        model.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedYCharon() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(0,4));
        oppWorker.setSpace(model.getSpace(0,3));
        model.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void domedSpaceCharon() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(1,1));
        oppWorker.setSpace(model.getSpace(1,0));
        model.getSpace(1,2).setDome();
        model.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void occupiedByWorkerSpaceCharon() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(1,1));
        oppWorker.setSpace(model.getSpace(1,0));
        Worker otherWorker = new Worker(new Player("test3", model));
        otherWorker.setSpace(model.getSpace(1,2));
        model.getSpace(1,2).setWorker(otherWorker);
        model.getSpace(1,2).setDome();
        model.charonPower(myWorker, oppWorker);
    }

    @Test
    public void charonHasWorked() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(1,1));
        oppWorker.setSpace(model.getSpace(1,0));
        assertNotEquals(oppWorker.getSpace(), model.getSpace(1, 2));
        model.charonPower(myWorker, oppWorker);
        assertEquals(oppWorker.getSpace(), model.getSpace(1,2));
    }


    //*********************//
    //Test su minotaurPower//
    //*********************//

    @Test (expected = IllegalSpaceException.class)
    public void negativeXMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(1,0));
        oppWorker.setSpace(model.getSpace(0,0));
        model.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedXMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(3,0));
        oppWorker.setSpace(model.getSpace(4,0));
        model.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void negativeYMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(0,1));
        oppWorker.setSpace(model.getSpace(0,0));
        model.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedYMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(0,3));
        oppWorker.setSpace(model.getSpace(0,4));
        model.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void domedSpaceMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(0,0));
        oppWorker.setSpace(model.getSpace(0,1));
        model.getSpace(0,2).setDome();
        model.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void occupiedSpaceByWorkerMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(0,0));
        oppWorker.setSpace(model.getSpace(0,1));
        Worker otherWorker = new Worker(new Player("test3", model));
        otherWorker.setSpace(model.getSpace(0,2));
        model.getSpace(0,2).setWorker(otherWorker);
        model.minotaurPower(myWorker, oppWorker);
    }

    @Test
    public void minotaurHasWorked() throws IllegalSpaceException {
        myWorker.setSpace(model.getSpace(0,0));
        oppWorker.setSpace(model.getSpace(0,1));
        assertNotEquals(oppWorker.getSpace(), model.getSpace(0,2));
        model.minotaurPower(myWorker, oppWorker);
        assertEquals(oppWorker.getSpace(), model.getSpace(0,2));
    }
}
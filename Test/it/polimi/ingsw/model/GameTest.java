package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private Game game = new Game();
    private Player player1 = new Player("test1", game);
    private Player player2 = new Player("test2", game);
    private Worker myWorker = new Worker(player1);
    private Worker oppWorker = new Worker(player2);


    //********************//
    //Test su isFreeToMove//
    //********************//

    @Test
    public void freeToMoveTrue() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(2,2));
        game.getSpace(2,2).setWorker(myWorker);
        assertTrue(game.isFreeToMove(myWorker));
    }

    @Test
    public void notFreeToMoveTrue() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(2,2));
        game.getSpace(2,2).setWorker(myWorker);

        game.getSpace(1,1).setDome();
        game.getSpace(1,2).setDome();
        game.getSpace(1,3).setDome();
        game.getSpace(2,1).setDome();
        game.getSpace(2,3).setDome();
        game.getSpace(3,1).setDome();
        game.getSpace(3,2).setDome();
        game.getSpace(3,3).setDome();
        
        assertFalse(game.isFreeToMove(myWorker));
    }

    //*******************//
    //Test su charonPower//
    //*******************//

    @Test (expected = IllegalSpaceException.class)
    public void negativeXCharon() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(1,0));
        game.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedXCharon() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(4,0));
        oppWorker.setSpace(game.getSpace(3,0));
        game.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void negativeYCharon() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(0,1));
        game.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedYCharon() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(0,4));
        oppWorker.setSpace(game.getSpace(0,3));
        game.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void domedSpaceCharon() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(1,1));
        oppWorker.setSpace(game.getSpace(1,0));
        game.getSpace(1,2).setDome();
        game.charonPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void occupiedByWorkerSpaceCharon() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(1,1));
        oppWorker.setSpace(game.getSpace(1,0));
        Worker otherWorker = new Worker(new Player("test3", game));
        otherWorker.setSpace(game.getSpace(1,2));
        game.getSpace(1,2).setWorker(otherWorker);
        game.charonPower(myWorker, oppWorker);
    }

    @Test
    public void charonHasWorked() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(1,1));
        oppWorker.setSpace(game.getSpace(1,0));
        assertNotEquals(oppWorker.getSpace(), game.getSpace(1, 2));
        game.charonPower(myWorker, oppWorker);
        assertEquals(oppWorker.getSpace(), game.getSpace(1,2));
        assertEquals(myWorker.getSpace(), game.getSpace(1,1));
    }


    //*********************//
    //Test su minotaurPower//
    //*********************//

    @Test (expected = IllegalSpaceException.class)
    public void negativeXMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(1,0));
        oppWorker.setSpace(game.getSpace(0,0));
        game.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedXMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(3,0));
        oppWorker.setSpace(game.getSpace(4,0));
        game.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void negativeYMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(0,1));
        oppWorker.setSpace(game.getSpace(0,0));
        game.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void unboundedYMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(0,3));
        oppWorker.setSpace(game.getSpace(0,4));
        game.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void domedSpaceMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(0,1));
        game.getSpace(0,2).setDome();
        game.minotaurPower(myWorker, oppWorker);
    }

    @Test (expected = IllegalSpaceException.class)
    public void occupiedSpaceByWorkerMinotaur() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(0,1));
        Worker otherWorker = new Worker(new Player("test3", game));
        otherWorker.setSpace(game.getSpace(0,2));
        game.getSpace(0,2).setWorker(otherWorker);
        game.minotaurPower(myWorker, oppWorker);
    }

    @Test
    public void minotaurHasWorked() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(0,1));
        assertNotEquals(oppWorker.getSpace(), game.getSpace(0,2));
        game.minotaurPower(myWorker, oppWorker);
        assertEquals(oppWorker.getSpace(), game.getSpace(0,2));
        assertEquals(myWorker.getSpace(), game.getSpace(0,0));
    }
}
package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private Game game = new Game();
    private Player player1 = new Player("test1", God.CHARON, game);
    private Player player2 = new Player("test2", God.APOLLO, game);
    private Worker myWorker = new Worker(player1);
    private Worker oppWorker = new Worker(player2);


    //********************//
    //Test su isFreeToMove//
    //********************//

    @Test
    public void freeToMoveTrue() {
        myWorker.setSpace(game.getSpace(2,2));
        game.getSpace(2,2).setWorker(myWorker);
        assertTrue(game.isFreeToMove(myWorker));
    }

    @Test
    public void notFreeToMoveTrue() {
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

    @Test
    public void negativeXCharon() {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(1,0));
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void unboundedXCharon() {
        myWorker.setSpace(game.getSpace(4,0));
        oppWorker.setSpace(game.getSpace(3,0));
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void negativeYCharon() {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(0,1));
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void unboundedYCharon() {
        myWorker.setSpace(game.getSpace(0,4));
        oppWorker.setSpace(game.getSpace(0,3));
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void domedSpaceCharon() {
        myWorker.setSpace(game.getSpace(1,1));
        oppWorker.setSpace(game.getSpace(1,0));
        game.getSpace(1,2).setDome();
        game.charonPower(myWorker, oppWorker);
    }

    @Test
    public void occupiedByWorkerSpaceCharon() {
        myWorker.setSpace(game.getSpace(1,1));
        oppWorker.setSpace(game.getSpace(1,0));
        Worker otherWorker = new Worker(new Player("test3", God.DEMETER, game));
        otherWorker.setSpace(game.getSpace(1,2));
        game.getSpace(1,2).setWorker(otherWorker);
        assertFalse(game.charonPower(myWorker, oppWorker));
    }

    @Test
    public void charonHasWorked() {
        myWorker.setSpace(game.getSpace(1,1));
        oppWorker.setSpace(game.getSpace(1,0));
        assertNotEquals(oppWorker.getSpace(), game.getSpace(1, 2));
        assertTrue(game.charonPower(myWorker, oppWorker));
        assertEquals(oppWorker.getSpace(), game.getSpace(1,2));
        assertEquals(myWorker.getSpace(), game.getSpace(1,1));
    }


    //*********************//
    //Test su minotaurPower//
    //*********************//

    @Test
    public void negativeXMinotaur() {
        myWorker.setSpace(game.getSpace(1,0));
        oppWorker.setSpace(game.getSpace(0,0));
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void unboundedXMinotaur() {
        myWorker.setSpace(game.getSpace(3,0));
        oppWorker.setSpace(game.getSpace(4,0));
       assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void negativeYMinotaur() {
        myWorker.setSpace(game.getSpace(0,1));
        oppWorker.setSpace(game.getSpace(0,0));
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void unboundedYMinotaur() {
        myWorker.setSpace(game.getSpace(0,3));
        oppWorker.setSpace(game.getSpace(0,4));
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void domedSpaceMinotaur() {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(0,1));
        game.getSpace(0,2).setDome();
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void occupiedSpaceByWorkerMinotaur() {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(0,1));
        Worker otherWorker = new Worker(new Player("test3", God.DEMETER, game));
        otherWorker.setSpace(game.getSpace(0,2));
        game.getSpace(0,2).setWorker(otherWorker);
        assertFalse(game.minotaurPower(myWorker, oppWorker));
    }

    @Test
    public void minotaurHasWorked() throws IllegalSpaceException {
        myWorker.setSpace(game.getSpace(0,0));
        oppWorker.setSpace(game.getSpace(0,1));
        assertNotEquals(oppWorker.getSpace(), game.getSpace(0,2));
        assertTrue(game.minotaurPower(myWorker, oppWorker));
        assertEquals(oppWorker.getSpace(), game.getSpace(0,2));
        assertEquals(myWorker.getSpace(), game.getSpace(0,0));
    }
}
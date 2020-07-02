package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveDefaultTest {
    private Space currSpace, nextSpace;
    private Worker myWorker;
    private MoveDefault moveDefault = new MoveDefault();
    private Game game = new Game();
    private Player player = new Player("test", God.DEMETER, game);


    @Test
    public void unboundedX() {
        currSpace = new Space(4,0);
        nextSpace = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void negativeX() {
        currSpace = new Space(0,4);
        nextSpace = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void unboundedY() {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void negativeY() {
        currSpace = new Space(0,0);
        nextSpace = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void positiveNotNeighbouringX() {
        currSpace = new Space(2,4);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void negativeNotNeighbouringX() {
        currSpace = new Space(0,4);
        nextSpace = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void positiveNotNeighbouringY() {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void negativeNotNeighbouringY() {
        currSpace = new Space(0,2);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void noMoreThanOneLevel() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertTrue( nextSpace.getHeight() - currSpace.getHeight() > 1 );
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void samePosition() {
        currSpace = new Space(0,0);
        nextSpace = currSpace;
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void nextSpaceOccupiedByWorker() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        nextSpace.setWorker(new Worker(new Player("test2", God.ATHENA, game)));

        assertNotNull(nextSpace.getWorker());
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void nextSpaceOccupiedByDome() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);

        assertTrue(nextSpace.isDomed());
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void isBlockedByAthena() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        game.getConstraint().setAthena(true);
        nextSpace.setHeight(1);
        player.setGod(God.ZEUS);

        assertTrue(game.getConstraint().athenaBlocks());
        assertFalse(moveDefault.execute( myWorker, nextSpace));
    }

    @Test
    public void isBlockedByAthenaIsAthena() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        game.getConstraint().setAthena(true);
        nextSpace.setHeight(1);
        currSpace.setWorker(myWorker);
        player.setGod(God.ATHENA);

        assertTrue(game.getConstraint().athenaBlocks());
        assertTrue(moveDefault.execute( myWorker, nextSpace));
        assertSame(myWorker.getSpace(), nextSpace);
        assertNull(currSpace.getWorker());
    }

    @Test
    public void normalMoveCondition() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        currSpace.setWorker(myWorker);

        assertNotSame(nextSpace.getWorker(), myWorker);
        assertTrue(moveDefault.execute( myWorker, nextSpace ));
        assertSame(nextSpace.getWorker(), myWorker);
        assertSame(myWorker.getSpace(), nextSpace);
        assertNull(currSpace.getWorker());
    }

    @Test
    public void standardWinning() {
        currSpace = new Space(0,0);
        nextSpace = new Space(1,1);
        currSpace.setHeight(2);
        nextSpace.setHeight(3);

        player.isWinner(currSpace,nextSpace);
        assertTrue(game.getLiteGame().isWinner());
    }
}
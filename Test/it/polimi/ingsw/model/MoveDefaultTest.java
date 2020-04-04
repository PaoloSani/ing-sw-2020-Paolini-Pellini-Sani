package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveDefaultTest {
    private Space currSpace, nextSpace;
    private Worker myWorker;
    private MoveDefault moveDefault = new MoveDefault();
    private Game game = new Game();
    private Player player = new Player("test", game);


    @Test ( expected = IllegalSpaceException.class )
    public void unboundedX() throws IllegalSpaceException {
        currSpace = new Space(4,0);
        nextSpace = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeX() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void unboundedY() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeY() throws IllegalSpaceException {
        currSpace = new Space(0,0);
        nextSpace = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void positiveNotNeighbouringX() throws IllegalSpaceException {
        currSpace = new Space(2,4);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeNotNeighbouringX() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void positiveNotNeighbouringY() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class)
    public void negativeNotNeighbouringY() throws IllegalSpaceException {
        currSpace = new Space(0,2);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void noMoreThanOneLevel() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertTrue( nextSpace.getHeight() - currSpace.getHeight() > 1 );
        moveDefault.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void samePosition() throws IllegalSpaceException {
        currSpace = new Space(0,0);
        nextSpace = currSpace;
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void nextSpaceOccupiedByWorker() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        nextSpace.setWorker(new Worker(new Player("test2", game)));

        assertNotNull(nextSpace.getWorker());
        moveDefault.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void nextSpaceOccupiedByDome() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);

        assertTrue(nextSpace.isDomed());
        moveDefault.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void isBlockedByAthena() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        game.getConstraint().setAthena(true);
        nextSpace.setHeight(1);
        player.setGod(God.ZEUS);

        assertTrue(game.getConstraint().athenaBlocks());
        moveDefault.execute( myWorker, nextSpace );
    }

    @Test
    public void isBlockedByAthenaIsAthena() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        game.getConstraint().setAthena(true);
        nextSpace.setHeight(1);
        currSpace.setWorker(myWorker);
        player.setGod(God.ATHENA);

        assertTrue(game.getConstraint().athenaBlocks());
        moveDefault.execute( myWorker, nextSpace );
        assertSame(myWorker.getSpace(), nextSpace);
        assertNull(currSpace.getWorker());
    }

    @Test
    public void normalMoveCondition() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        currSpace.setWorker(myWorker);

        assertNotSame(nextSpace.getWorker(), myWorker);
        moveDefault.execute( myWorker, nextSpace );
        assertSame(nextSpace.getWorker(), myWorker);
        assertSame(myWorker.getSpace(), nextSpace);
        assertNull(currSpace.getWorker());
    }
}
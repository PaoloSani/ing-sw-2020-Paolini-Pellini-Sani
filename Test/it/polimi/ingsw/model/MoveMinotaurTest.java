package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

//TODO: Riccardo
public class MoveMinotaurTest {
    private Space currSpace, nextSpace;
    private Worker myWorker;
    private MoveMinotaur moveMinotaur = new MoveMinotaur();
    private Game game = new Game();
    private Player player = new Player("test", God.MINOTAUR, game);


    @Test ( expected = IllegalSpaceException.class )
    public void unboundedX() throws IllegalSpaceException {
        currSpace = new Space(4,0);
        nextSpace = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveMinotaur.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeXTest() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveMinotaur.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void unboundedY() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveMinotaur.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeY() throws IllegalSpaceException {
        currSpace = new Space(0,0);
        nextSpace = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveMinotaur.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void positiveNotNeighbouringX() throws IllegalSpaceException {
        currSpace = new Space(2,4);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveMinotaur.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeNotNeighbouringX() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveMinotaur.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void positiveNotNeighbouringY() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveMinotaur.execute( myWorker, nextSpace);
    }

    @Test ( expected = IllegalSpaceException.class)
    public void negativeNotNeighbouringY() throws IllegalSpaceException {
        currSpace = new Space(0,2);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveMinotaur.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void noMoreThanOneLevel() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertTrue( nextSpace.getHeight() - currSpace.getHeight() > 1 );
        moveMinotaur.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void samePosition() throws IllegalSpaceException {
        currSpace = new Space(0,0);
        nextSpace = currSpace;
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveMinotaur.execute( myWorker, nextSpace );
    }


    @Test ( expected = IllegalSpaceException.class )
    public void nextSpaceOccupiedByDome() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);

        assertTrue(nextSpace.isDomed());
        moveMinotaur.execute( myWorker, nextSpace );
    }
    @Test ( expected = IllegalSpaceException.class )
    public void isBlockedByAthena() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        game.getConstraint().setAthena(true);
        nextSpace.setHeight(1);

        assertTrue(game.getConstraint().athenaBlocks());
        moveMinotaur.execute( myWorker, nextSpace );
    }

    @Test
    public void minotaurMoveCondition() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        Worker oppWorker = new Worker(new Player("test2", God.DEMETER, game));
        oppWorker.setSpace(nextSpace);
        nextSpace.setWorker(oppWorker);

        currSpace.setWorker(myWorker);

        assertNotSame(nextSpace.getWorker(), myWorker);
        assertNotNull(nextSpace.getWorker());
        moveMinotaur.execute( myWorker, nextSpace );
        assertNull(currSpace.getWorker());
        assertSame(nextSpace.getWorker(), myWorker);
        assertSame(myWorker.getSpace(), nextSpace);
    }

    @Test
    public void normalMoveCondition() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        currSpace.setWorker(myWorker);

        assertNotSame(nextSpace.getWorker(), myWorker);
        moveMinotaur.execute( myWorker, nextSpace );
        assertNull(currSpace.getWorker());
        assertSame(nextSpace.getWorker(), myWorker);
        assertSame(myWorker.getSpace(), nextSpace);
    }
}
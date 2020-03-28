package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveDefaultTest {
    private Space currSpace, nextSpace;
    private Worker myWorker;
    private MoveDefault moveDefault = new MoveDefault();
    private Model model = new Model();
    private Player player = new Player("test", model);


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
        nextSpace = new Space(2, 4);
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
        nextSpace = new Space(0, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveDefault.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void nextSpaceOccupiedByWorker() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(2, 4);
        nextSpace.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        nextSpace.setWorker(new Worker(new Player("test2", model)));

        assertTrue(nextSpace.getWorker() != null);
        moveDefault.execute( myWorker, nextSpace );
    }



    @Test ( expected = IllegalSpaceException.class )
    public void nextSpaceOccupiedByDome() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(2, 4);
        nextSpace.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);

        assertTrue(nextSpace.isDomed());
        moveDefault.execute( myWorker, nextSpace );
    }

    @Test
    public void normalMoveCondition() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);

        assertFalse( nextSpace.getWorker() == myWorker);
        moveDefault.execute( myWorker, nextSpace );
        assertTrue( nextSpace.getWorker() == myWorker );
        assertTrue( myWorker.getSpace() == nextSpace );
    }
}
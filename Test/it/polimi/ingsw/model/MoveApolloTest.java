package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveApolloTest {
    private Space currSpace, nextSpace;
    private Worker myWorker;
    private MoveApollo moveApollo = new MoveApollo();
    private Model model = new Model();
    private Player player = new Player("test", model);
    private int level;

    @Test ( expected = IllegalSpaceException.class )
    public void negativeXTest() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void unboundedX() throws IllegalSpaceException {
        currSpace = new Space(4,0);
        nextSpace = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeY() throws IllegalSpaceException {
        currSpace = new Space(0,0);
        nextSpace = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void unboundedY() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void positiveNotNeighbouringX() throws IllegalSpaceException {
        currSpace = new Space(2,4);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeNotNeighbouringX() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class )
    public void positiveNotNeighbouringY() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class)
    public void negativeNotNeighbouringY() throws IllegalSpaceException {
        currSpace = new Space(0,2);
        nextSpace = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class)
    public void moreThanOneLevel() throws IllegalSpaceException {
        currSpace = new Space(0,2);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class)
    public void sameSpace() throws IllegalSpaceException {
        currSpace = new Space(0,2);
        nextSpace = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class)
    public void alliedWorker() throws IllegalSpaceException {
        currSpace = new Space(0,2);
        nextSpace = new Space(1, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        nextSpace.setWorker(new Worker(player));
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class)
    public void spaceDome() throws IllegalSpaceException {
        currSpace = new Space(0,2);
        nextSpace = new Space(1, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        nextSpace.setHeight(4);
        moveApollo.execute( myWorker, nextSpace );
    }

    @Test ( expected = IllegalSpaceException.class)
    public void athenaPower() throws IllegalSpaceException {
        Constraint constraint = new Constraint();
        constraint.setAthena(true);
        model.setConstraint(constraint);
        currSpace = new Space(0,2);
        nextSpace = new Space(1, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        nextSpace.setHeight(1);
        moveApollo.execute( myWorker, nextSpace );
    }

    public void verifyEnemyPositionX() throws IllegalSpaceException {

        currSpace = new Space(3,1);
        nextSpace = new Space(2, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        Worker opWorker = new Worker(new Player("Test2", model));
        nextSpace.setWorker(opWorker);
        moveApollo.execute( myWorker, nextSpace );
        assertEquals(4, myWorker.getSpace().getX());
    }

    public void verifyEnemyPositionY() throws IllegalSpaceException {

        currSpace = new Space(3,1);
        nextSpace = new Space(2, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        Worker opWorker = new Worker(new Player("Test2", model));
        nextSpace.setWorker(opWorker);
        moveApollo.execute( myWorker, nextSpace );
        assertEquals(0, myWorker.getSpace().getY());
    }
}
package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveApolloTest {
    private Space currSpace, nextSpace;
    private Worker myWorker, oppWorker;
    private MoveApollo moveApollo = new MoveApollo();
    private Game game = new Game();
    private Player player = new Player("test", God.APOLLO, game);
    private int level;

    @Test
    public void negativeXTest() {
        currSpace = new Space(0,4);
        nextSpace = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void unboundedX() {
        currSpace = new Space(4,0);
        nextSpace = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void negativeY() {
        currSpace = new Space(0,0);
        nextSpace = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void unboundedY() {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void positiveNotNeighbouringX() {
        currSpace = new Space(2,4);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void negativeNotNeighbouringX() {
        currSpace = new Space(0,4);
        nextSpace = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void positiveNotNeighbouringY() {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void negativeNotNeighbouringY() {
        currSpace = new Space(0,2);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void moreThanOneLevel() {
        currSpace = new Space(0,2);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void sameSpace() {
        currSpace = new Space(0,2);
        nextSpace = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void alliedWorker() {
        currSpace = new Space(0,2);
        nextSpace = new Space(1, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        nextSpace.setWorker(new Worker(player));
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void noSpaceToBuild() {
        currSpace = new Space(1,2);
        nextSpace = new Space(2, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        currSpace.setWorker(myWorker);
        nextSpace.setWorker(new Worker(player));

        game.getSpace(1,1).setHeight(4);
        game.getSpace(1,3).setHeight(4);
        game.getSpace(2,1).setHeight(4);
        game.getSpace(2,3).setHeight(4);
        game.getSpace(3,1).setHeight(4);
        game.getSpace(3,2).setHeight(4);
        game.getSpace(3,3).setHeight(4);

        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void spaceDome(){
        currSpace = new Space(0,2);
        nextSpace = new Space(1, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        nextSpace.setDome();
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void athenaPower() {
        Constraint constraint = new Constraint();
        constraint.setAthena(true);
        game.setConstraint(constraint);
        currSpace = new Space(0,2);
        nextSpace = new Space(1, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        nextSpace.setHeight(1);
        assertFalse(moveApollo.execute( myWorker, nextSpace ));
    }

    @Test
    public void normalMoveCondition() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        //Servono ?
        assertNotSame(nextSpace.getWorker(), myWorker);
        assertNull(nextSpace.getWorker());
        //mossa
        assertTrue(moveApollo.execute( myWorker, nextSpace ));

        assertSame(nextSpace.getWorker(), myWorker);
        assertSame(myWorker.getSpace(), nextSpace);
    }

    @Test
    public void apolloMoveCondition() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        //ATTENZIONE devo mettere anche questo per il controllo sennò currSpace.getWorker() == null
        currSpace.setWorker(myWorker);
        oppWorker = new Worker(new Player("Test 2", God.DEMETER, game));
        oppWorker.setSpace(nextSpace);
        //ATTENZIONE devo mettere anche questo per il controllo sennò nextSpace.getWorker() == null
        nextSpace.setWorker(oppWorker);
        assertNotSame(nextSpace.getWorker(), myWorker);
        assertNotSame(currSpace.getWorker(), oppWorker);
        assertNotNull(nextSpace.getWorker());
        assertNotNull(currSpace.getWorker());
        assertTrue(moveApollo.execute( myWorker, nextSpace ));
        assertEquals(nextSpace.getWorker(), myWorker);
        assertEquals(currSpace.getWorker(), oppWorker);
        assertEquals(oppWorker.getSpace(), currSpace);
        assertEquals(myWorker.getSpace(), nextSpace);

    }
}
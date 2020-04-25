package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class MoveMinotaurTest {
    private Space currSpace, nextSpace;
    private Worker myWorker;
    private MoveMinotaur moveMinotaur = new MoveMinotaur();
    private Game game = new Game();
    private Player player = new Player("test", God.MINOTAUR, game);


    @Test
    public void unboundedX() {
        currSpace = new Space(4,0);
        nextSpace = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void negativeXTest(){
        currSpace = new Space(0,4);
        nextSpace = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void unboundedY() {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void negativeY() {
        currSpace = new Space(0,0);
        nextSpace = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void positiveNotNeighbouringX() {
        currSpace = new Space(2,4);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void negativeNotNeighbouringX() {
        currSpace = new Space(0,4);
        nextSpace = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void positiveNotNeighbouringY() {
        currSpace = new Space(0,4);
        nextSpace = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void negativeNotNeighbouringY() {
        currSpace = new Space(0,2);
        nextSpace = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void noMoreThanOneLevel() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertTrue( nextSpace.getHeight() - currSpace.getHeight() > 1 );
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void samePosition(){
        currSpace = new Space(0,0);
        nextSpace = currSpace;
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }


    @Test
    public void nextSpaceOccupiedByDome() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        nextSpace.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);

        assertTrue(nextSpace.isDomed());
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }
    @Test
    public void isBlockedByAthena() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        game.getConstraint().setAthena(true);
        nextSpace.setHeight(1);

        assertTrue(game.getConstraint().athenaBlocks());
        assertFalse(moveMinotaur.execute( myWorker, nextSpace));
    }

    @Test
    public void minotaurMoveCondition(){
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
        assertTrue(moveMinotaur.execute( myWorker, nextSpace ));
        assertNull(currSpace.getWorker());
        assertSame(nextSpace.getWorker(), myWorker);
        assertSame(myWorker.getSpace(), nextSpace);
    }

    @Test
    public void normalMoveCondition() {
        currSpace = new Space(0,4);
        nextSpace = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        currSpace.setWorker(myWorker);

        assertNotSame(nextSpace.getWorker(), myWorker);
        assertTrue(moveMinotaur.execute( myWorker, nextSpace ));
        assertNull(currSpace.getWorker());
        assertSame(nextSpace.getWorker(), myWorker);
        assertSame(myWorker.getSpace(), nextSpace);
    }
}
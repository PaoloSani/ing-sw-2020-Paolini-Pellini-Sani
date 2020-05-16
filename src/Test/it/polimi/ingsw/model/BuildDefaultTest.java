package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class BuildDefaultTest {

    private Space currSpace, space;
    private Worker myWorker;
    private BuildDefault buildDefault = new BuildDefault();
    private Game game = new Game();
    private Player player = new Player("test", God.PAN, game);
    private int level;

    @Test
    public void negativeXTest() {
        currSpace = new Space(0,4);
        space = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void unboundedX() {
        currSpace = new Space(4,0);
        space = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void negativeY() {
        currSpace = new Space(0,0);
        space = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void unboundedY() {
        currSpace = new Space(0,4);
        space = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void positiveNotNeighbouringX() {
        buildDefault = new BuildDefault();
        game = new Game();
        currSpace = new Space(2,4);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void negativeNotNeighbouringX() {
        currSpace = new Space(0,4);
        space = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void positiveNotNeighbouringY() {
        currSpace = new Space(0,4);
        space = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void negativeNotNeighbouringY() {
        currSpace = new Space(0,2);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void samePosition(){
        currSpace = new Space(0,0);
        space = new Space(0, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void spaceOccupiedByOtherWorker() {
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        space.setWorker(new Worker(new Player("test2", God.ZEUS, game)));
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void spaceOccupiedByDome()  {
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        space.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }
    //tanto di default space.height = 0
    @Test
    public void levelExceptionCondition()  {
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 2;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void normalBuildCondition() {
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        space.setHeight(1);
        level = 2;
        assertNotEquals(level, space.getHeight());
        buildDefault.execute( myWorker, space, level);
        assertEquals(level, space.getHeight());
    }
}
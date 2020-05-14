package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class BuildDefaultTest {

    private Space currSpace, space;
    private Worker myWorker;
    private BuildDefault buildDefault;
    private Game game;
    private Player player;
    private int level;

    @Test
    public void negativeXTest() {
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
        currSpace = new Space(0,4);
        space = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void unboundedX() {
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
        currSpace = new Space(4,0);
        space = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void negativeY() {
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
        currSpace = new Space(0,0);
        space = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void unboundedY() {
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
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
        Player player = new Player("test", God.PAN, game);
        currSpace = new Space(2,4);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void negativeNotNeighbouringX() {
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
        currSpace = new Space(0,4);
        space = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void positiveNotNeighbouringY() {
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
        currSpace = new Space(0,4);
        space = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void negativeNotNeighbouringY() throws IllegalSpaceException {
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
        currSpace = new Space(0,2);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void samePosition(){
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
        currSpace = new Space(0,0);
        space = new Space(0, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void spaceOccupiedByOtherWorker() {
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
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
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
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
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 2;
        assertFalse(buildDefault.execute( myWorker, space, level));
    }

    @Test
    public void normalBuildCondition() {
        buildDefault = new BuildDefault();
        game = new Game();
        Player player = new Player("test", God.PAN, game);
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
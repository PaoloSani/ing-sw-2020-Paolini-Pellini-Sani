package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


public class BuildAtlasTest {

    private Space currSpace, space;
    private Worker myWorker;
    private BuildAtlas buildAtlas;
    private Game game;
    private Player player;
    private int level;


    @Test
    public void negativeXTest(){
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,4);
        space = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void unboundedX() {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(4,0);
        space = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void negativeY() {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,0);
        space = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void unboundedY() {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,4);
        space = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void positiveNotNeighbouringX(){
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(2,4);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void negativeNotNeighbouringX() {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,4);
        space = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void positiveNotNeighbouringY() {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,4);
        space = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void negativeNotNeighbouringY() {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,2);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void samePosition() {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,0);
        space = new Space(0, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void spaceOccupiedByOtherWorker()  {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        space.setWorker(new Worker(new Player("test2", God.ATHENA, game)));
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void spaceOccupiedByDome() {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,4);
        space = new Space(2, 4);
        space.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void atlasExceptionCondition(){
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 2;
        assertNotEquals(4, level);
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void atlasBuildCondition() {
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 4;
        assertNotEquals(level, space.getHeight());
        buildAtlas.execute( myWorker, space, level);
        Assert.assertTrue(space.isDomed());
    }

    @Test
    public void normalBuildCondition(){
        buildAtlas = new BuildAtlas();
        game = new Game();
        player = new Player("test", God.ATLAS, game );
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        space.setHeight(1);
        level = 2;
        assertNotEquals(level, space.getHeight());
        buildAtlas.execute( myWorker, space, level);
        Assert.assertEquals(level, space.getHeight());
    }

}

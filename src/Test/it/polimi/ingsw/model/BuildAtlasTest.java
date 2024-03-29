package it.polimi.ingsw.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;


public class BuildAtlasTest {

    private Space currSpace, space;
    private Worker myWorker;
    private BuildAtlas buildAtlas = new BuildAtlas();
    private Game game = new Game();
    private Player player = new Player("test", God.ATLAS, game );
    private int level;


    @Test
    public void negativeXTest(){
        currSpace = new Space(0,4);
        space = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void unboundedX() {
        currSpace = new Space(4,0);
        space = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void negativeY() {
        currSpace = new Space(0,0);
        space = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void unboundedY() {
        currSpace = new Space(0,4);
        space = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void positiveNotNeighbouringX(){
        currSpace = new Space(2,4);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void negativeNotNeighbouringX() {
        currSpace = new Space(0,4);
        space = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void positiveNotNeighbouringY() {
        currSpace = new Space(0,4);
        space = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void negativeNotNeighbouringY() {
        currSpace = new Space(0,2);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void samePosition() {
        currSpace = new Space(0,0);
        space = new Space(0, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildAtlas.execute( myWorker, space, level));
    }

    @Test
    public void spaceOccupiedByOtherWorker()  {
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

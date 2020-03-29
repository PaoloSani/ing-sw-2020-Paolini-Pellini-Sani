package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildHephaestusTest {

    private Space currSpace, space;
    private Worker myWorker;
    private BuildHephaestus buildHephaestus = new BuildHephaestus();
    private Model model = new Model();
    private Player player = new Player("test", model);
    private int level;

    @Test ( expected = IllegalSpaceException.class )
    public void negativeXTest() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        space = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void unboundedX() throws IllegalSpaceException {
        currSpace = new Space(4,0);
        space = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeY() throws IllegalSpaceException {
        currSpace = new Space(0,0);
        space = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void unboundedY() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        space = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void positiveNotNeighbouringX() throws IllegalSpaceException {
        currSpace = new Space(2,4);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void negativeNotNeighbouringX() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        space = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void positiveNotNeighbouringY() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        space = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class)
    public void negativeNotNeighbouringY() throws IllegalSpaceException {
        currSpace = new Space(0,2);
        space = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void samePosition() throws IllegalSpaceException {
        currSpace = new Space(0,0);
        space = new Space(0, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void spaceOccupiedByOtherWorker() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        space.setWorker(new Worker(new Player("test2", model)));
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void spaceOccupiedByDome() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        space = new Space(2, 4);
        space.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test ( expected = IllegalSpaceException.class )
    public void SecondLevelDome() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        currSpace.setHeight(2);
        space = new Space(2, 4);
        space.setHeight(2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 4;
        buildHephaestus.execute( myWorker, space, level);
    }

    @Test
    public void normalBuildCondition() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        space.setHeight(1);
        level = 2;
        assertFalse(level == space.getHeight());
        buildHephaestus.execute( myWorker, space, level);
        assertEquals(level, space.getHeight());
    }

    @Test
    public void hephaestusBuildCondition() throws IllegalSpaceException {
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        space.setHeight(1);
        level = 3;
        assertFalse(level == space.getHeight());
        buildHephaestus.execute( myWorker, space, level);
        assertEquals(level, space.getHeight());
    }
}
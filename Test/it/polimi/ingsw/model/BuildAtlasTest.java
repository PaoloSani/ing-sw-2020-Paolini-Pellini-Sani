package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildAtlasTest {

    private Space currSpace, space;
    private Worker myWorker, otherWorker;
    private BuildAtlas buildAtlas = new BuildAtlas();
    private Model model = new Model();
    private Player player = new Player("test", model);
    private int level;


    @Test ( expected = IllegalSpaceException.class)
    public void executeTest() throws IllegalSpaceException {
        currSpace = new Space(1,4);
        space = new Space(5, 1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        buildAtlas.execute(myWorker, space, level);
    }


}

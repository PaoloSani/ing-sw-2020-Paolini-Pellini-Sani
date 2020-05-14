package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;


public class BuildZeusTest {

    private Space currSpace, space;
    private Worker myWorker;
    private BuildZeus buildZeus;
    private Game game ;
    private Player player;
    private int level;


    @Test
    public void unboundedX() {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        game = new Game();
        currSpace = new Space(4,0);
        space = new Space(5, 0);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void negativeX()  {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,4);
        space = new Space(-1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void unboundedY() {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,4);
        space = new Space(0, 5);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void negativeY()  {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,0);
        space = new Space(0, -1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void positiveNotNeighbouringX() {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(2,4);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void negativeNotNeighbouringX() {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,4);
        space = new Space(2, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void positiveNotNeighbouringY() {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,4);
        space = new Space(0, 2);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void negativeNotNeighbouringY(){
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,2);
        space = new Space(0, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void spaceOccupiedByOtherWorker() {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        space.setWorker(new Worker(new Player("test2", God.ATLAS, game)));
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void spaceOccupiedByDome() {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        space.setHeight(4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void noMoreThanOneLevel() {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 2;
        assertTrue( level > space.getHeight() + 1 );
        assertFalse(buildZeus.execute( myWorker, space, level ));
    }

    @Test
    public void normalBuildCondition() {
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,4);
        space = new Space(1, 4);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;
        assertNotEquals(level, space.getHeight());
        buildZeus.execute( myWorker, space, level);
        assertSame(myWorker.getSpace(), currSpace);
        assertEquals(level, space.getHeight());
    }

    @Test
    public void buildInSamePosition(){
        game = new Game();
        player = new Player("test", God.ZEUS, game);
        buildZeus= new BuildZeus();
        currSpace = new Space(0,0);
        space = currSpace;
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        currSpace.setWorker(myWorker);
        level = 1;
        assertNotEquals(space.getHeight(), level);
        buildZeus.execute( myWorker, space, level);
        assertEquals(space.getHeight(), level);
        assertSame(myWorker.getSpace(), currSpace);
        assertEquals(currSpace.getHeight(), level);
    }
}
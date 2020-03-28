import it.polimi.ingsw.model.*;
import org.junit.Test;

class BuildAtlasTest {
    private Space currSpace, space;
    private Worker myWorker, otherWorker;
    private BuildAtlas buildAtlas = new BuildAtlas();
    private Model model = new Model();
    private Player player = new Player("test", model);
    private int level;

    //@Test(expected = IllegalSpaceException.class)
    public void exceptionTest1() throws IllegalSpaceException {
        currSpace = new Space(4,1);
        space = new Space(5, 1);
        myWorker = new Worker(player);
        myWorker.setSpace(currSpace);
        level = 1;

        buildAtlas.execute(myWorker, space, level);
    }
}
package it.polimi.ingsw.model;

/**
 * BuildAtlas implements the build method for Atlas
 */

public class BuildAtlas implements Build{
    /**
     * @param worker Worker performing the build
     * @param space Space where you want to build
     * @param level Level you want to build
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean execute( Worker worker, Space space, int level ) {
        int currX, currY, newH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        newH = space.getHeight() + 1;

        if (worker.getPlayer().getGame().invalidSpace(space, worker.getSpace()) ||     //checks if the space is valid (in the table and near to the worker
                currX == space.getX() && currY == space.getY() ||     //checks if the worker tries to build under himself
                space.getWorker() != null ||     //the space must not be occupied by a worker
                space.isDomed() ||
                (newH != level && level != 4))     //Atlas may build only one level or a couple

            return false;

        return worker.getPlayer().getGame().buildSwitch(space, level, true);
    }
}

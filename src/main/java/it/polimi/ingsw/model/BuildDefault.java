package it.polimi.ingsw.model;

/**
 * BuildDefault implements the normal build method
 */
public class BuildDefault implements Build {
    /**
     * @param worker Worker performing the build
     * @param space Space where you want to build
     * @param level Level you want to build
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean execute( Worker worker, Space space, int level ) {
        boolean result = false;
        int currX, currY, newH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        newH = space.getHeight() + 1;

        if (!worker.getPlayer().getGame().invalidSpace(space, worker.getSpace()) &&
                (currX != space.getX() || currY != space.getY()) &&
                space.getWorker() == null &&
                !space.isDomed() &&
                newH == level)     //may build only one level at time
        {
            result = worker.getPlayer().getGame().buildSwitch(space, level, false);
        }

        return result;
    }

}

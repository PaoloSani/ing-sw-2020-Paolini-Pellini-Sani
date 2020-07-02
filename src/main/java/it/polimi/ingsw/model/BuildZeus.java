package it.polimi.ingsw.model;

/**
 * BuildZeus implements the build strategy for Zeus
 */
public class BuildZeus implements Build {
    /**
     * @param worker Worker performing the build
     * @param space Space where you want to build
     * @param level Level you want to build
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean execute( Worker worker, Space space, int level ) {
        int newH = space.getHeight() + 1;

        //as in buildDefault except from the fact that the player may build under himself
        if (    worker.getPlayer().getGame().invalidSpace(space, worker.getSpace())  ||
                (space.getWorker() != null  && space.getWorker() != worker)    ||
                space.isDomed()                                                ||
                newH != level                                                   )

            return false;

        return worker.getPlayer().getGame().buildSwitch(space, level,false);
    }




}


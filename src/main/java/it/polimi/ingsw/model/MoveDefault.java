package it.polimi.ingsw.model;

public class MoveDefault implements Move {
    /**
     * @param worker Worker performing the move
     * @param nextSpace Space where you want to move
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean execute( Worker worker, Space nextSpace ) {
        boolean result = true;
        int currH = worker.getSpace().getHeight();

        //if the player is Athena at first it resets the block
        if( worker.getPlayer().getGod() == God.ATHENA ){
            worker.getPlayer().getGame().getConstraint().setAthena(false);
        }


        if (    worker.getPlayer().getGame().invalidMoveSpace(nextSpace,worker.getSpace()) ||
                nextSpace.getWorker() != null                                         ||
                nextSpace.isDomed()                                                   ||
                //if Athena's block is active, the player cannot move up
                (worker.getPlayer().getGame().getConstraint().athenaBlocks() && (nextSpace.getHeight() - currH == 1)))

            result = false;

        //winning condition
        worker.getPlayer().isWinner(worker.getSpace(), nextSpace);

        if ( result ) {
            //If Athena moved up, set the block
            if (worker.getPlayer().getGod() == God.ATHENA && (nextSpace.getHeight() - currH == 1)) {
                worker.getPlayer().getGame().getConstraint().setAthena(true);
            }

            //current worker's new position
            worker.getSpace().setWorker(null);      //the previous space is now empty
            worker.setSpace(nextSpace);             //the new space contains the worker and the worker references the new space
        }
        return result;
    }
}

package it.polimi.ingsw.model;


/**
 * MoveApollo implements the move strategy for Apollo
 */

public class MoveApollo implements Move {
    /**
     * @param worker Worker performing the move
     * @param nextSpace Space where you want to move
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean execute(Worker worker, Space nextSpace) {
        boolean result = true;
        int currH = worker.getSpace().getHeight();
        Worker oppWorker;

        if (    worker.getPlayer().getGame().invalidMoveSpace(nextSpace,worker.getSpace())      ||     //checks if the space is valid
                ( nextSpace.getWorker() != null &&
                        nextSpace.getWorker().getPlayer().equals(worker.getPlayer()))           ||     //the next space isn't occupied by a worker of the current player
                ( nextSpace.getWorker() != null &&
                        !worker.getPlayer().getGame().isFreeToBuild(nextSpace.getWorker()))     ||     //if the worker in the next space cannot perform a build, it blocks the action
                nextSpace.isDomed()                                                             ||     //the next space isn't domed
                //Athena's block
                (worker.getPlayer().getGame().getConstraint().athenaBlocks() && (nextSpace.getHeight() - currH == 1)))

            result = false;

        //winning condition
        worker.getPlayer().isWinner(worker.getSpace(), nextSpace);

        if ( result ) {
            //normal move
            //the current worker is placed in the opponent worker initial space
            if ( nextSpace.getWorker() == null ) {
                worker.getSpace().setWorker(null);                    //the previous space is set to empty
            }
            //move with switch
            else {
                oppWorker = nextSpace.getWorker();        //saves the opponent space
                worker.getSpace().setWorker(oppWorker);   //the current player's initial space now contains the oppWorker
                oppWorker.setSpace(worker.getSpace());    //the oppWorker is placed in the current worker's initial space

            }
            worker.setSpace(nextSpace);                           //the next space contains the worker and the worker in the next space
        }
        return result;
    }
}

package it.polimi.ingsw.model;

/**
 * MoveMinotaur implements the move of Minotaur
 */
public class MoveMinotaur implements Move {
    /**
     * @param worker Worker performing the move
     * @param nextSpace Space where you want to move
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean execute( Worker worker, Space nextSpace ) {
        boolean result = true;
        int currH = worker.getSpace().getHeight();
        Game myGame = worker.getPlayer().getGame();

        //similar to other strategies
        if (    myGame.invalidMoveSpace(nextSpace, worker.getSpace())                 ||
                nextSpace.isDomed()                                                   ||
                (worker.getPlayer().getGame().getConstraint().athenaBlocks() && (nextSpace.getHeight() - currH == 1)))

            result = false;

        worker.getPlayer().isWinner(worker.getSpace(), nextSpace);



        if( result && nextSpace.getWorker() != null ){
            //performs the shift of an opponent's worker if possible
            result =  myGame.minotaurPower(worker, nextSpace.getWorker());
        }

        if ( result ) {
            //if the shift was performed, the current worker is placed in the nextSpace
            worker.getSpace().setWorker(null);
            worker.setSpace(nextSpace);
        }
        return result;
    }
}

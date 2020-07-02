package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;


/**
 * MoveState is the class that represents the state of the FSM in which the current worker execute his move
 */

public class MoveState implements GameState {
    private BackEnd backEnd;
    private Space nextSpace;
    int counterArtemis;
    private Space lastSpaceArtemis;
    private boolean returnBack;
    private boolean toReset;

    /**
     * Class constructor
     * @param backEnd: reference BackEnd
     */
    public MoveState(BackEnd backEnd) {
        this.backEnd = backEnd;
        counterArtemis = 0;
        lastSpaceArtemis = null;
        returnBack = false;
        toReset = false;
    }

    /**
     * @return true in case the execute was successful, meaning that the method has moved the worker correctly in the game table.
     */
    @Override
    public boolean execute() {
        boolean result = true;
        if(!toReset) {

            nextSpace = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
            if (nextSpace == null) result = false;

            if (result) {

                //Artemis' second move
                if (backEnd.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 1 && nextSpace.getWorker() == null) {
                    if (lastSpaceArtemis == nextSpace)
                        returnBack = true;
                    else {
                        //the counter is set to 2 if the second move can be correctly executed
                        counterArtemis++;
                    }
                }

                //Artemis' first move
                if (backEnd.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 0 && nextSpace.getWorker() == null ){
                    lastSpaceArtemis = backEnd.getCurrWorker().getSpace();
                    counterArtemis++;
                }


                if (!returnBack) {
                    if (!backEnd.getCurrPlayer().moveWorker(backEnd.getCurrWorker(), nextSpace))
                        result = false;
                }
                else result = false;


                if ( ((backEnd.getCurrPlayer().getGod() != God.TRITON && backEnd.getCurrPlayer().getGod() != God.ARTEMIS) ||
                        (backEnd.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 2)  ) && result)
                {
                    setToReset(true);
                }

                //Checks if triton can use his power again
                if ( backEnd.getCurrPlayer().getGod() == God.TRITON  &&
                        nextSpace.getX() > 0 && nextSpace.getX() < 4 &&
                        nextSpace.getY() > 0 && nextSpace.getY() < 4 && result) {
                    setToReset(true);
                }

                returnBack = false;
            }
        }

        backEnd.getGame().setCurrWorker(backEnd.getCurrWorker());
        backEnd.getGame().refreshLiteGame();
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());
        return result;
    }

    public void setToReset(boolean toReset) {
        this.toReset = toReset;
    }


    /**
     * Sets the counters back to 0 and the boolean constraints to false
     */
    @Override
    public void reset(){
        counterArtemis = 0;
        lastSpaceArtemis = null;
        returnBack = false;
        toReset = false;
    }
}

package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;


/**
 * Class that represents the state of the FSM in which the current worker execute his move
 */

public class MoveState implements GameState {
    private BackEnd backEnd;
    private Space nextSpace;
    int counterArtemis;
    private Space lastSpaceArtemis;
    private boolean returnBack;
    private boolean toReset;

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
                if (backEnd.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 1) {
                    if (lastSpaceArtemis == nextSpace)
                        returnBack = true;
                    else counterArtemis++;
                }

                if (backEnd.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 0) {
                    lastSpaceArtemis = backEnd.getCurrWorker().getSpace();
                    counterArtemis++;
                }


                if (!returnBack) {

                    if (!backEnd.getCurrPlayer().moveWorker(backEnd.getCurrWorker(), nextSpace))
                        result = false;

                } else result = false;


                if (((backEnd.getCurrPlayer().getGod() != God.TRITON && backEnd.getCurrPlayer().getGod() != God.ARTEMIS) ||
                        (backEnd.getCurrPlayer().getGod() == God.ARTEMIS && counterArtemis == 2)) && result) {
                    setToReset(true);
                }

                //caso in cui Tritone esce dal perimetro
                if (backEnd.getCurrPlayer().getGod() == God.TRITON &&   //cella fuori dal perimetro
                        nextSpace.getX() > 0 && nextSpace.getX() < 4 &&
                        nextSpace.getY() > 0 && nextSpace.getY() < 4 && result) {
                    setToReset(true);

                }

                returnBack = false;
            }
        }
        //Aggiorno il LiteGame
        backEnd.getGame().setCurrWorker(backEnd.getCurrWorker());
        backEnd.getGame().refreshLiteGame();
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());   //Notifico la VView
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

    //update: riceve una cella in cui è contenuto la cella dove andare
    //execute: esegue la move





}

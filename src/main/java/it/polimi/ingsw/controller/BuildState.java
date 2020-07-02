package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;


/**
 * BuildState is a class part of the FSM of the controller. It manages the call of methods in the Model, which change the building in the
 * game table and writes the output on the LiteGame class, which will be sent to clients.
 */
public class BuildState implements GameState {
    private final BackEnd backEnd;
    private int level;
    private Space lastSpace;
    private int counterDemeter;
    private int counterPoseidon;
    private int counterHephaestus;
    private boolean hephaestusConstraint;
    private boolean toReset;


    /**
     * Constructor of the class
     * @param backEnd: reference BackEnd
     */
    public BuildState(BackEnd backEnd) {
        this.backEnd = backEnd;
        reset();
    }


    /**
     * @return true in case the execute was successful, meaning that the method has built something correctly in the game table.
     */
    @Override
    public boolean execute() {
        boolean result = true;
       if ( !toReset ) {
            Space toBuild = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
            if (toBuild == null) result = false;

            if (result) {
                level = backEnd.getGameMessage().getLevel();

                //Demeter case: she may build two times in different Spaces, I use the counter only if the selected doesn't contain a worker
                if (backEnd.getCurrPlayer().getGod() == God.DEMETER && counterDemeter >= 0 && ( toBuild.getWorker() == null ) ){
                    if ( counterDemeter == 1 && lastSpace != toBuild  )
                        setToReset(true);
                    if ( counterDemeter == 0 ) {
                        lastSpace = toBuild;
                    }
                }

                //Hephaestus case: he may build two times on the same space, if the second time he doesn't build a dome
                if ( backEnd.getCurrPlayer().getGod() == God.HEPHAESTUS && counterHephaestus >= 0 && toBuild.getWorker() == null ) {
                    if ( counterHephaestus == 0 ) {
                        lastSpace = toBuild;
                    }
                    else hephaestusConstraint = level == 4 || toBuild != lastSpace;
                }

                if ( !hephaestusConstraint && !( counterDemeter == 1 && ( toBuild == lastSpace || toBuild.getWorker() != null))) {
                    if (!backEnd.getCurrPlayer().buildSpace(backEnd.getCurrWorker(), toBuild, level)) result = false;
                } else result = false;

                if ( result && counterPoseidon >= 0 && backEnd.getCurrPlayer().getGod() == God.POSEIDON) {
                    counterPoseidon++;
                    if (counterPoseidon == 3) setToReset(true);
                }

                if ( result ) {
                    //If the counter is -1, Poseidon has built with his first worker so I can check if the power may be used with the other one
                    if (backEnd.getCurrPlayer().getGod() == God.POSEIDON && counterPoseidon == -1) {
                        //check if the second worker is on ground level
                        if (backEnd.getCurrPlayer().getOtherWorker(backEnd.getCurrWorker()).getSpace().getHeight() == 0) {
                            backEnd.setCurrWorker(backEnd.getCurrPlayer().getOtherWorker(backEnd.getCurrWorker()));
                            backEnd.getGame().setCurrWorker(backEnd.getCurrWorker());
                            counterPoseidon++;
                        } else {
                            setToReset(true);
                        }
                    }

                    if ( counterDemeter == 0 && backEnd.getCurrPlayer().getGod() == God.DEMETER ) counterDemeter++;
                    if ( backEnd.getCurrPlayer().getGod() == God.HEPHAESTUS  ) counterHephaestus++;
                    if ( counterHephaestus == 2 ) setToReset(true);
                }
            }
        }

        boolean saveAthena = backEnd.getGame().getConstraint().athenaBlocks();
        backEnd.getGame().getConstraint().setAthena(false);

        backEnd.getGame().getConstraint().setAthena(saveAthena);



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
        counterDemeter = 0;
        counterPoseidon = -1;
        counterHephaestus = 0;
        toReset = false;
        level = 0;
        hephaestusConstraint = false;
        lastSpace = null;
    }


}

package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class BuildState implements GameState {
    private BackEnd backEnd;
    private int level;
    private Space toBuild;
    private Space lastSpace;
    private int counterDemeter;
    private int counterPoseidon;
    private int counterHephaestus;
    private boolean hephaestusConstraint;


    public BuildState(BackEnd backEnd) {
        this.backEnd = backEnd;
        resetBuilding();
    }


    @Override
    public void execute() {
        if (backEnd.getCurrPlayer().getGod() == God.DEMETER && counterDemeter >= 0){
            lastSpace = toBuild;
            counterDemeter++;
        }

        //TODO: correggere il pattern strategy per BuildHephaestus --> portarlo a BuildDefault
        if (backEnd.getCurrPlayer().getGod() == God.HEPHAESTUS && counterHephaestus == 0){
            lastSpace = toBuild;
            counterHephaestus++;
        }

        if (backEnd.getCurrPlayer().getGod() == God.HEPHAESTUS && counterHephaestus == 1){
            if (level == 4 || toBuild != lastSpace) hephaestusConstraint = true;
            else counterHephaestus++;
        }

        if ( counterPoseidon >= 1 ){
            counterPoseidon++;
        }

        if (!hephaestusConstraint) {
            try {
                backEnd.getCurrPlayer().buildSpace(backEnd.getCurrWorker(), toBuild, level);
            } catch (IllegalSpaceException e) {
                e.printStackTrace();
            }
        }

        if (    ( backEnd.getCurrPlayer().getGod() != God.POSEIDON                               &&
                  backEnd.getCurrPlayer().getGod() != God.DEMETER                                &&
                  backEnd.getCurrPlayer().getGod() != God.HEPHAESTUS )                           ||
                ( backEnd.getCurrPlayer().getGod() == God.POSEIDON && counterPoseidon == 4 )     ||      //Significa che ho costruito già 3 volte
                ( backEnd.getCurrPlayer().getGod() == God.DEMETER && counterDemeter == 2 )       ||
                ( backEnd.getCurrPlayer().getGod() == God.HEPHAESTUS && counterHephaestus == 1 )  ) {

            resetBuilding();
            backEnd.updateCurrPlayer();
            changeState(backEnd.chooseWorkerState);
        }

        if ( backEnd.getCurrPlayer().getGod() == God.POSEIDON ){
            if ( counterPoseidon == 0 ){
                backEnd.setCurrWorker(backEnd.getCurrPlayer().getOtherWorker(backEnd.getCurrWorker()));        //cambio lavoratore
                if ( backEnd.getCurrWorker().getSpace().getHeight() != 0 ) {      //se non è a terra il suo potere non vale
                    resetBuilding();
                    backEnd.updateCurrPlayer();
                    changeState(backEnd.chooseWorkerState);
                }
                counterPoseidon++;      //lo aggiorno già a 1 così mi accorgo che ho attivato il suo potere e sto costruendo col secondo worker
            }

        }

        hephaestusConstraint = false;

    }

    @Override
    public void changeState(GameState state) {
        backEnd.setCurrState( state );
    }

    public void resetBuilding(){
        counterDemeter = 0;
        counterPoseidon = 0;
        counterHephaestus = 0;
        hephaestusConstraint = false;
        lastSpace = null;
    }

    //update: riceve una cella in cui costruire
    //execute: esegue la costruzione
    //changeState: porta in choosingWorker se non ha poteri speciali
    @Override
    public void update(PlayersInTheGame message) {

    }
}

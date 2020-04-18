package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class Building implements GameState {
    private Server server;
    private int level;
    private Space toBuild;
    private Space lastSpace;
    private int counterDemeter;
    private int counterPoseidon;
    private int counterHephaestus;
    private boolean hephaestusConstraint;


    public Building(Server server) {
        this.server = server;
        resetBuilding();
    }


    @Override
    public void execute() {
        if (server.getCurrPlayer().getGod() == God.DEMETER && counterDemeter >= 0){
            lastSpace = toBuild;
            counterDemeter++;
        }

        //TODO: correggere il pattern strategy per BuildHephaestus --> portarlo a BuildDefault
        if (server.getCurrPlayer().getGod() == God.HEPHAESTUS && counterHephaestus == 0){
            lastSpace = toBuild;
            counterHephaestus++;
        }

        if (server.getCurrPlayer().getGod() == God.HEPHAESTUS && counterHephaestus == 1){
            if (level == 4 || toBuild != lastSpace) hephaestusConstraint = true;
            else counterHephaestus++;
        }

        if ( counterPoseidon >= 1 ){
            counterPoseidon++;
        }

        if (!hephaestusConstraint) {
            try {
                server.getCurrPlayer().buildSpace(server.getCurrWorker(), toBuild, level);
            } catch (IllegalSpaceException e) {
                e.printStackTrace();
            }
        }

        if (    ( server.getCurrPlayer().getGod() != God.POSEIDON                               &&
                  server.getCurrPlayer().getGod() != God.DEMETER                                &&
                  server.getCurrPlayer().getGod() != God.HEPHAESTUS )                           ||
                ( server.getCurrPlayer().getGod() == God.POSEIDON && counterPoseidon == 4 )     ||      //Significa che ho costruito già 3 volte
                ( server.getCurrPlayer().getGod() == God.DEMETER && counterDemeter == 2 )       ||
                ( server.getCurrPlayer().getGod() == God.HEPHAESTUS && counterHephaestus == 1 )  ) {

            resetBuilding();
            server.updateCurrPlayer();
            changeState(server.choosingWorkers);
        }

        if ( server.getCurrPlayer().getGod() == God.POSEIDON ){
            if ( counterPoseidon == 0 ){
                server.setCurrWorker(server.getCurrPlayer().getOtherWorker(server.getCurrWorker()));        //cambio lavoratore
                if ( server.getCurrWorker().getSpace().getHeight() != 0 ) {      //se non è a terra il suo potere non vale
                    resetBuilding();
                    server.updateCurrPlayer();
                    changeState(server.choosingWorkers);
                }
                counterPoseidon++;      //lo aggiorno già a 1 così mi accorgo che ho attivato il suo potere e sto costruendo col secondo worker
            }

        }

        hephaestusConstraint = false;

    }

    @Override
    public void changeState(GameState state) {
        server.setCurrState( state );
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

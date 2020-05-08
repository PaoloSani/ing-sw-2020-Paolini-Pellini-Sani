package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.IllegalSpaceException;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.GameState;



public class BuildState implements GameState {
    private BackEnd backEnd;
    private int level;
    private Space lastSpace;
    private int counterDemeter;
    private int counterPoseidon;
    private int counterHephaestus;
    private boolean hephaestusConstraint;
    private boolean toReset;


    public BuildState(BackEnd backEnd) {
        this.backEnd = backEnd;
        reset();
    }



    @Override
    public boolean execute() {
        boolean result = true;
        //update: riceve una cella in cui costruire
        //execute: esegue la costruzione
        //changeState: porta in choosingWorker se non ha poteri speciali

        // prendo le coordinate della cella dove costruire:
        if ( !toReset ) {
            Space toBuild = backEnd.getGame().getSpace(backEnd.getGameMessage().getSpace1()[0], backEnd.getGameMessage().getSpace1()[1]);
            if (toBuild == null) result = false;

            if (result) {
                level = backEnd.getGameMessage().getLevel();

                //caso Demetra: può costruire due volte ma non sulla stessa cella, la prima volta salvo la cella
                if (backEnd.getCurrPlayer().getGod() == God.DEMETER && counterDemeter >= 0) {

                    if (counterDemeter == 1 && lastSpace != toBuild)
                        setToReset(true);

                    if (counterDemeter == 0) {
                        lastSpace = toBuild;
                    }

                }

                //caso Efesto: può costruire al massimo due volte sulla stessa cella, ma la seconda non una cupola
                if (backEnd.getCurrPlayer().getGod() == God.HEPHAESTUS && counterHephaestus >= 0) {
                    if (counterHephaestus == 0) {
                        lastSpace = toBuild;
                        counterHephaestus++;
                    } else if (level == 4 || toBuild != lastSpace)
                        hephaestusConstraint = true;
                    else counterHephaestus++;

                    if (counterHephaestus == 2 && !hephaestusConstraint) setToReset(true);
                }

                //alla fine del suo turno può costruire al massimo tre volte con il worker che non ha usato a patto che questo sia a terra
                if (counterPoseidon >= 0 && backEnd.getCurrPlayer().getGod() == God.POSEIDON) {
                    counterPoseidon++;
                    if (counterPoseidon == 3) setToReset(true);
                }

                if (!hephaestusConstraint && !(counterDemeter == 1 && toBuild == lastSpace)) {       //può costruire al massimo due volte e non sulla stessa cella
                    if (!backEnd.getCurrPlayer().buildSpace(backEnd.getCurrWorker(), toBuild, level)) result = false;
                } else result = false;

                if ( result ) {
                    if (backEnd.getCurrPlayer().getGod() == God.POSEIDON && counterPoseidon == -1) {    //se il counter è -1 significa che ho costruito con il worker di partenza senza usare il suo potere
                        backEnd.setCurrWorker(backEnd.getCurrPlayer().getOtherWorker(backEnd.getCurrWorker()));        //cambio lavoratore
                        if (backEnd.getCurrWorker().getSpace().getHeight() == 0) {
                            counterPoseidon++;      //lo aggiorno già a 0 così mi accorgo che ho attivato il suo potere e sto costruendo col secondo worker
                        } else {                      //se non è a terra il suo potere non vale
                            setToReset(true);
                        }


                    }

                    hephaestusConstraint = false;

                    if (counterDemeter == 0 && backEnd.getCurrPlayer().getGod() == God.DEMETER) counterDemeter++;
                }
            }
        }

        //le righe che seguono (97-104) sono per il caso sfigatissimo
        boolean saveAthena = backEnd.getGame().getConstraint().athenaBlocks();
        backEnd.getGame().getConstraint().setAthena(false);
        //CASO SFIGATISSIMO: il currWorker si è appena bloccato e l'altro worker è già bloccato tra un cerchio di cupole
        if ( !backEnd.getGame().isFreeToMove(backEnd.getCurrWorker()) && !backEnd.getGame().isFreeToMove(backEnd.getCurrPlayer().getOtherWorker(backEnd.getCurrWorker())) ){
            backEnd.setToRemove(backEnd.getCurrPlayer());
            backEnd.getGame().setCurrWorker(null);
        }
        backEnd.getGame().getConstraint().setAthena(saveAthena);



        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());   //Notifico la View
        return result;
    }
    public void setToReset(boolean toReset) {
        this.toReset = toReset;
    }

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

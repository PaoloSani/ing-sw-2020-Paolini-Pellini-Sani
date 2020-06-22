package it.polimi.ingsw.model;

/**
 * Class that implements the build method for Zeus
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

        //controllo se la space è valida
        if (    worker.getPlayer().getGame().invalidSpace(space, worker.getSpace())  ||     //colonna non valida
                //currX == space.getX() && currY == space.getY()               ||     può costruisce sotto di sé
                (space.getWorker() != null  && space.getWorker() != worker)    ||     //la cella è occupata da un worker
                space.isDomed()                                                ||     //nella cella è già presente una cupola
                newH != level                                                   )     //Controlla che l'altezza del livello da costruire sia giusto

            return false;

        //controllo l'altezza richiesta
        return worker.getPlayer().getGame().buildSwitch(space, level,false);
    }




}


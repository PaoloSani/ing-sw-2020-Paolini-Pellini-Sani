package it.polimi.ingsw.model;

/**
 * Class that implements the build method for Atlas
 */

public class BuildAtlas implements Build{
    /**
     * @param worker Worker performing the build
     * @param space Space where you want to build
     * @param level Level you want to build
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean execute( Worker worker, Space space, int level ) {
        int currX, currY, newH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        newH = space.getHeight() + 1;

        //controllo se la space è valida
        if (worker.getPlayer().getGame().invalidSpace(space, worker.getSpace()) ||     //colonna non valida
                currX == space.getX() && currY == space.getY() ||     //si costruisce sotto di sé
                space.getWorker() != null ||     //la cella è occupata da un worker
                space.isDomed() ||     //nella cella è già presente una cupola
                (newH != level && level != 4))     //Controlla che l'altezza del livello da costruire sia giusto
            //ad eccezione delle cupole, che sono sempre accettate

            return false;

        //controllo l'altezza richiesta
        //Poiché a questo punto si ha newH == level, impongo la condizione dello switch sul level
        return worker.getPlayer().getGame().buildSwitch(space, level, true);
    }
}

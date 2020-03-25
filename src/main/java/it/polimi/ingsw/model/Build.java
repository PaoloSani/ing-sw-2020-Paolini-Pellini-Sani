package it.polimi.ingsw.model;

// Interfaccia per il pattern stategy per la Build.

public interface Build {
    public void execute( Worker worker, Space space, int level ) throws IllegalSpaceException;
}




/* Paolo : potere di caronte nel controller, efesto
   Riccardo : atlas, apollo
   Giuseppe : minotauro, zeus
 */

/*
    Nella build mancano dei controlli per verificare che la cella passata sia effettivamente nelle 5x5
                space.getX() > 4 && space.getX() < 0                           ||
                space.getY() > 4 && space.getY() < 0                           ||     //space non appartenente alla tabella
*/
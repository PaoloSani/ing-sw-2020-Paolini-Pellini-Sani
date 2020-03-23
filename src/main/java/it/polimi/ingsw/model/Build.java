package it.polimi.ingsw.model;

// Interfaccia per il pattern stategy per la Build.

public interface Build {
    public void execute( Worker worker, Space space, int level ) throws IllegalSpaceException;
}




/* Paolo : potere di caronte nel controller, efesto
   Riccardo : atlas, apollo
   Giuseppe : minotauro, zeus
 */
package it.polimi.ingsw.model;

/**
 * The class is instantiated whenever either Athena or Hypnus is present in the game.
 * If the Athena flag is true, all opposing players cannot go up to the next level.
 * If Hypnus is present, opposing workers cannot move the highest worker.
 */
public class Constraint {
    private boolean Athena;
    private boolean Hypnus;

    public Constraint() {
        Athena = false;
        Hypnus = false;
    }
    // Getter
    public boolean athenaBlocks() {
        return Athena;
    }

    public boolean hypnusBlocks() {
        return Hypnus;
    }

    public void setHypnus(boolean hypnus) {
        Hypnus = hypnus;
    }

    public void setAthena(boolean athena) {
        Athena = athena;
    }

}

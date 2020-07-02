package it.polimi.ingsw.model;

/**
 * Constraint is the class responsible to keep trace of Hypnus and Athena.
 * If the Athena flag is true, all opposing players cannot go up to the next level, else Athena didn't move up or isn't playing.
 * If Hypnus is present (his flag is set to true), opposing workers cannot move their highest worker.
 */
public class Constraint {
    private boolean Athena;
    private boolean Hypnus;

    /**
     * Class constructor, as default all is set to false, meaning that Athena and Hypnus are not playing
     */
    public Constraint() {
        Athena = false;
        Hypnus = false;
    }

    /**
     * @return : true if Athena is present and she moved up in her last turn
     */
    public boolean athenaBlocks() {
        return Athena;
    }

    /**
     * @return : true if hypnus is playing
     */
    public boolean hypnusBlocks() {
        return Hypnus;
    }

    /**
     * @param hypnusIsPlaying : true if hypnus is playing
     */
    public void setHypnus(boolean hypnusIsPlaying) {
        Hypnus = hypnusIsPlaying;
    }

    /**
     * @param athenaMovedUp : true if Athena moved up in her last turn
     */
    public void setAthena(boolean athenaMovedUp) {
        Athena = athenaMovedUp;
    }

}

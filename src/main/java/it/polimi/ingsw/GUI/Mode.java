package it.polimi.ingsw.GUI;

/**
 * It contains all the possible choices a player can take to start playing.
 */


public enum Mode {
    /**
     * New game mode, the player becomes the challenger, waiting for friends to play with by sharing a gameID.
     */

    NEW_GAME("A"),

    /**
     * Chosen when a gameID is shared among players.
     */

    EXISTING_MATCH("B"),

    /**
     * If chosen, the player wait for others on the internet to join him/her.
     */

    RANDOM_MATCH("C"),

    /**
     * A default value for Mode variables.
     */
    DEFAULT("default");


    /**
     * String that represents the made choice.
     */

    private final String mode;

    /**
     * It builds the enum as described before.
     * @param mode is the chosen mode by the player
     */

    Mode(String mode){
        this.mode = mode;
    }

    /**
     * It converts a String value to a Mode value.
     * @param mode string typed/clicked by a player to choose a specific mode
     * @return Mode
     */

    public static Mode fromText(String mode){
        for (Mode m : Mode.values()){
            if (m.mode.equals(mode)) return m;
        }
        return DEFAULT;
    }
}

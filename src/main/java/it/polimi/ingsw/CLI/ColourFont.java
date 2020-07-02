package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.God;

/**
 * It contains the colours used by the CLI.
 */
public enum ColourFont {

    ANSI_GREEN_BACKGROUND("\033[48;5;41m"),
    ANSI_BLUE_BACKGROUND("\033[48;5;25m"),
    ANSI_LEVEL1("\033[48;5;241m"),
    ANSI_LEVEL2("\033[48;5;248m"),
    ANSI_LEVEL3("\033[48;5;255m"),
    ANSI_DOME("\033[48;5;25m"),
    ANSI_MENU_BACKGROUND("\033[48;5;188m"),
    ANSI_WORKER("\033[48;5;31m"),

    ANSI_WHITE_TEXT("\033[38;5;255m"),
    ANSI_BLACK_TEXT("\033[38;5;16m"),



    ANSI_BOLD("\u001b[1m");


    /**
     * It is the escape code that reset the background/foreground colour
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * It is the escape code of the given colour.
     */
    private final String escape;

    ColourFont(String escape){
        this.escape = escape;
    }

    @Override
    public String toString(){
        return escape;
    }


    /**
     * It returns a colour given a god.
     * @param god is the god we want to colour
     * @return the escape colour of the colour.
     */
    public static String getGodColour(God god){
        String colour;
        switch (god) {
            case APOLLO:
                colour = "\033[38;5;45m";
                break;
            case ARTEMIS:
                colour = "\033[38;5;189m";
                break;
            case ATHENA:
                colour = "\033[38;5;30m";
                break;
            case ATLAS:
                colour = "\033[38;5;33m";
                break;
            case CHARON:
                colour = "\033[38;5;58m";
                break;
            case DEMETER:
                colour = "\033[38;5;154m";
                break;
            case HEPHAESTUS:
                colour = "\033[38;5;160m";
                break;
            case HYPNUS:
                colour = "\033[38;5;105m";
                break;
            case MINOTAUR:
                colour = "\033[38;5;130m";
                break;
            case PAN:
                colour = "\033[38;5;28m";
                break;
            case POSEIDON:
                colour = "\033[38;5;25m";
                break;
            case PROMETHEUS:
                colour = "\033[38;5;202m";
                break;
            case TRITON:
                colour = "\033[38;5;116m";
                break;
            case ZEUS:
                colour = "\033[38;5;220m";
                break;
            default:
                colour = "\033[38;5;231m";
                break;
        }
        return colour;
    }

}

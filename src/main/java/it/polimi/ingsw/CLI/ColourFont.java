package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.God;

public enum ColourFont {
    //FOREGROUND COLOR: "\033[38;5;<COLOR>m"
    //BACKGROUND COLOR: "\033[48;5;<COLOR>m"

    ANSI_GREEN_BACKGROUND("\033[48;5;41m"),
    ANSI_BLUE_BACKGROUND("\033[48;5;25m"),
    ANSI_LEVEL1("\033[48;5;241m"),
    ANSI_LEVEL2("\033[48;5;248m"),
    ANSI_LEVEL3("\033[48;5;255m"),
    ANSI_DOME("\033[48;5;25m"),
    ANSI_MENU_BACKGROUND("\033[48;5;188m"),
    ANSI_WORKER("\033[48;5;31m"),

    ANSI_RED_TEXT("\033[38;5;124m"),
    ANSI_ICE_TEXT("\033[38;5;189m"),
    ANSI_GOLD_TEXT("\033[38;5;214m"),
    ANSI_WHITE_TEXT("\033[38;5;255m"),
    ANSI_BLACK_TEXT("\033[38;5;16m"),

    ANSI_BLACK_BACKGROUND("\033[48;5;16m"),
    ANSI_RED_BACKGROUND("\u001B[41m"),
    ANSI_YELLOW_BACKGROUND("\u001B[43m"),
    ANSI_PURPLE_BACKGROUND("\u001B[45m"),
    ANSI_CYAN_BACKGROUND("\u001B[46m"),
    ANSI_WHITE_BACKGROUND("\u001B[47m"),


    ANSI_BOLD("\u001b[1m"),
    ANSI_UNDERLINE(" \u001b[4m"),
    ANSI_REVERSED("\u001b[7m");

    public static final String ANSI_RESET = "\u001B[0m";

    private String escape;

    ColourFont(String escape){
        this.escape = escape;
    }

    public String getEscape(){
        return escape;
    }

    @Override
    public String toString(){
        return escape;
    }


    //TODO:setta i colori nella view delle varie divinità
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
                colour = "\033[38;5;20m";
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
                colour = "\033[38;5;52m";
                break;
            case PAN:
                colour = "\033[38;5;22m";
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

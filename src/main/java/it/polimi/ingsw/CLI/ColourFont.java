package it.polimi.ingsw.CLI;

public enum ColourFont {
    //FOREGROUND COLOR: "\033[38;5;<COLOR>m"
    //BACKGROUND COLOR: "\033[48;5;<COLOR>m"

    ANSI_GREEN_BACKGROUND("\033[48;5;35m"),
    ANSI_BLUE_BACKGROUND("\033[48;5;18m"),
    ANSI_LEVEL1("\033[48;5;245m"),
    ANSI_LEVEL2("\033[48;5;250m"),
    ANSI_LEVEL3("\033[48;5;255m"),
    ANSI_DOME("\033[48;5;25m"),
    ANSI_MENU_BACKGROUND("\033[48;5;188m"),

    ANSI_WHITE_TEXT("\033[38;5;255m"),
    ANSI_BLACK_TEXT("\033[38;5;16m"),

    ANSI_BLACK_BACKGROUND("\033[48;5;16m"),
    ANSI_RED_BACKGROUND("\u001B[41m"),
    ANSI_YELLOW_BACKGROUND("\u001B[43m"),
    ANSI_PURPLE_BACKGROUND("\u001B[45m"),
    ANSI_CYAN_BACKGROUND("\u001B[46m"),
    ANSI_WHITE_BACKGROUND("\u001B[47m"),

    ANSI_BOLD("\u001b[1m"),
    ANSI_UNDERLINE(" \u001b[4m");

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

}

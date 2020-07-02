package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.SerializableLiteGame;

/**
 * CLIPrinter contains the method used to print a game table
 */
public class CLIPrinter {
    CommandLineGame referenceCli;

    /**
     * Constructor class for the CLIPrinter
     * @param commandLineGame the CommandLineGame class to reference
     */
    public CLIPrinter(CommandLineGame commandLineGame){
        this.referenceCli = commandLineGame;
    }

    /**
     * It prints on mirror the gametable from the litegame
     */
    public synchronized void  buildGameTable(SerializableLiteGame serializableLiteGame){
        String[][] gameTable = serializableLiteGame.getTable();
        System.out.println("                                                             ");
        System.out.println("                                  1        2        3        4        5                " +  ColourFont.ANSI_BOLD+"  KEYS  "+ColourFont.ANSI_RESET);
        System.out.println("                              + = = = ++ = = = ++ = = = ++ = = = ++ = = = +            " + "  - GROUND LEVEL: " + ColourFont.ANSI_GREEN_BACKGROUND + "    " + ColourFont.ANSI_RESET );
        for (int i = 0; i < 5; i++){
            buildTableRow(serializableLiteGame, gameTable[i],i+1);
        }
    }

    /**
     * It prints on mirror a single row of the gametable from a litegame.
     * @param serializableLiteGame: the serializableLiteGame to reference
     * @param serializableLiteGameRow: the row of spaces to print
     * @param row is the number of the row to print.
     */
    void buildTableRow(SerializableLiteGame serializableLiteGame, String[] serializableLiteGameRow, int row){
        String[] space1 = buildGameSpace(serializableLiteGame, serializableLiteGameRow[0],row-1,0);
        String[] space2 = buildGameSpace(serializableLiteGame, serializableLiteGameRow[1],row-1,1);
        String[] space3 = buildGameSpace(serializableLiteGame, serializableLiteGameRow[2],row-1,2);
        String[] space4 = buildGameSpace(serializableLiteGame, serializableLiteGameRow[3],row-1,3);
        String[] space5 = buildGameSpace(serializableLiteGame, serializableLiteGameRow[4],row-1,4);
        String[] newRow;
        if (row == 1){
            newRow = new String[]{
                    "                              "+space1[0]+space2[0]+space3[0]+space4[0]+space5[0]+"            "+"  - FIRST LEVEL:  " + ColourFont.ANSI_LEVEL1 + "    " + ColourFont.ANSI_RESET + " x"+serializableLiteGame.getLevel1(),
                    "                           "+row+"  "+space1[1]+space2[1]+space3[1]+space4[1]+space5[1]+"            "+"  - SECOND LEVEL: " + ColourFont.ANSI_LEVEL2 + "    " + ColourFont.ANSI_RESET + " x"+serializableLiteGame.getLevel2(),
                    "                              "+space1[2]+space2[2]+space3[2]+space4[2]+space5[2]+"            "+"  - THIRD LEVEL:  " + ColourFont.ANSI_LEVEL3 + "    " + ColourFont.ANSI_RESET + " x"+serializableLiteGame.getLevel3(),
                    "                              "+space1[3]+space2[3]+space3[3]+space4[3]+space5[3]+"            "+"  - DOME:         " + ColourFont.ANSI_DOME + "    " + ColourFont.ANSI_RESET + " x"+serializableLiteGame.getDome()
            };
        }

        else if (row == 2) {
            String yourPlayer = " (YOU)";
            String name1 = "none";
            String name2 = "none";
            String name3 = "none";

            if (serializableLiteGame.getName1() != null ) {
                name1 = serializableLiteGame.getName1();
                if ( name1.equals(referenceCli.getNickname())) name1 = name1.concat(yourPlayer);
            }
            if (serializableLiteGame.getName2() != null ) {
                name2 = serializableLiteGame.getName2();
                if ( name2.equals(referenceCli.getNickname())) name2 = name2.concat(yourPlayer);
            }
            if (serializableLiteGame.getName3() != null ) {
                name3 = serializableLiteGame.getName3();
                if ( name3.equals(referenceCli.getNickname())) name3 = name3.concat(yourPlayer);
            }

            if (serializableLiteGame.getName3() != null) {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - PLAYER A: " + ColourFont.getGodColour(serializableLiteGame.getGod1()) + name1 + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2] + "            " + "  - PLAYER B: " + ColourFont.getGodColour(serializableLiteGame.getGod2()) + name2 + ColourFont.ANSI_RESET,
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3] + "            " + "  - PLAYER C: " + ColourFont.getGodColour(serializableLiteGame.getGod3()) + name3 + ColourFont.ANSI_RESET
                };
            }
            else {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - PLAYER A: " + ColourFont.getGodColour(serializableLiteGame.getGod1()) + name1 + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2] + "            " + "  - PLAYER B: " + ColourFont.getGodColour(serializableLiteGame.getGod2()) + name2 + ColourFont.ANSI_RESET,
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3],
                };
            }
        }

        else if (row == 3){
            if (serializableLiteGame.getName3() != null) {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - CURRENT WORKER: " + ColourFont.ANSI_WORKER + "    " + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3] + "            " + "  - GOD A: " + ColourFont.getGodColour(serializableLiteGame.getGod1()) + serializableLiteGame.getGod1() + ColourFont.ANSI_RESET,
                };
            }
            else {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - CURRENT WORKER: " + ColourFont.ANSI_WORKER + "    " + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3] + "            " + "  - GOD A: " + ColourFont.getGodColour(serializableLiteGame.getGod1()) + serializableLiteGame.getGod1() + ColourFont.ANSI_RESET,
                };
            }
        }

        else if (row == 4) {
            if (serializableLiteGame.getName3() != null) {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0] + "            " + "  - GOD B: " + ColourFont.getGodColour(serializableLiteGame.getGod2()) + serializableLiteGame.getGod2() + ColourFont.ANSI_RESET,
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - GOD C: "  + ColourFont.getGodColour(serializableLiteGame.getGod3()) + serializableLiteGame.getGod3() + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3],
                };
            }
            else {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0] + "            " + "  - GOD B: " + ColourFont.getGodColour(serializableLiteGame.getGod2()) + serializableLiteGame.getGod2() + ColourFont.ANSI_RESET,
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1],
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3],
                };
            }
        }
        else {
            newRow = new String[]{
                    "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                    "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1],
                    "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                    "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3],
            };
        }
        for (String s : newRow){
            System.out.println(s);
        }
    }

    String[] buildGameSpace(SerializableLiteGame serializableLiteGame, String spaceFromLiteGame, int row, int col){
        ColourFont background = ColourFont.ANSI_MENU_BACKGROUND;
        ColourFont foreground = ColourFont.ANSI_WHITE_TEXT;
        ColourFont chosen = ColourFont.ANSI_GREEN_BACKGROUND;
        String dome = "";
        String reset = ColourFont.ANSI_RESET;
        String player;
        String[] gameSpace;
        char[] spaceAnalyzer = spaceFromLiteGame.toCharArray();

        if (spaceAnalyzer[1] == '0') {
            background = ColourFont.ANSI_GREEN_BACKGROUND;
            chosen = background;
        }
        else if (spaceAnalyzer[1] == '1') {
            background = ColourFont.ANSI_LEVEL1;
            chosen = background;
        }
        else if (spaceAnalyzer[1] == '2') {
            background = ColourFont.ANSI_LEVEL2;
            chosen = background;
        }
        else if (spaceAnalyzer[1] == '3') {
            background = ColourFont.ANSI_LEVEL3;
            chosen = background;
            foreground = ColourFont.ANSI_BLACK_TEXT;
        }
        if (spaceAnalyzer[2] == 'D') {
            dome = ColourFont.ANSI_DOME.toString();
        }
        switch(spaceAnalyzer[0]){
            case 'A':
                player = "(A)";
                break;

            case 'B':
                player = "(B)";
                break;

            case 'C':
                player = "(C)";
                break;

            default:
                player = "   ";
                break;
        }
        player = ColourFont.ANSI_BLACK_TEXT+player+ColourFont.ANSI_BLACK_TEXT;
        if (serializableLiteGame.getCurrWorker()[0] == row && serializableLiteGame.getCurrWorker()[1] == col){
            chosen = ColourFont.ANSI_WORKER;
        }

        if(chosen == ColourFont.ANSI_WORKER) {
            gameSpace = new String[]{
                    "|" + chosen + foreground.toString() + " " + dome + "     " + chosen + " " + reset + "|",
                    "|" + chosen + foreground.toString() + " " + dome + " " + background + player + chosen.toString() + dome + " " + chosen + " " + reset + "|",
                    "|" + chosen + foreground.toString() + " " + dome + "     " + chosen + " " + reset + "|",
                    "+ = = = +"
            };
        }
        else{
            gameSpace = new String[]{
                    "|" + background + foreground.toString() + " " + dome + "     " + background + " " + reset + "|",
                    "|" + background + foreground.toString() + " " + dome + " " + background + dome + player + dome + " " + background + " " + reset + "|",
                    "|" + background + foreground.toString() + " " + dome + "     " + background+ " " + reset + "|",
                    "+ = = = +"
            };
        }
        return gameSpace;
    }
}

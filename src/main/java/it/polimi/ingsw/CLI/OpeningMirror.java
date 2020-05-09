package it.polimi.ingsw.CLI;

import it.polimi.ingsw.GUI.GraphicalGame;



import java.util.Scanner;

public class OpeningMirror {

    /**
     * It establishes whether CLI or GUI is used by the player
     */

    private Scanner in = new Scanner(System.in);
    private String lookAndFeel = "default";
    private GraphicalGame GGame;
    private CommandLineGame CLGame;

    public void userInterfaceSetup(){
        while(!lookAndFeel.equals("A") && !lookAndFeel.equals("B")) {
            System.out.println(ColourFont.ANSI_PROVA + "Questa è una prova colore\n");
            System.out.println(ColourFont.ANSI_CYAN_BACKGROUND.toString()+ColourFont.ANSI_BOLD.toString() + "\n  Hello stranger!\n  In order to guarantee a better game experience, you can choose\n");
            System.out.println(ColourFont.ANSI_RESET + ColourFont.ANSI_CYAN_BACKGROUND.toString()+" - A) ADVANCED GAME");
            System.out.println("      If you're looking for a more intuitive gameplay\n");                   //Description of Advanced Game
            System.out.println(" - B) RETRO\' GAME");
            System.out.println("      If you want to experience a vintage arcade style \n");                   //Description of Retro' Game
            lookAndFeel = in.nextLine();
            lookAndFeel = lookAndFeel.toUpperCase();
            if(!lookAndFeel.equals("A") && !lookAndFeel.equals("B")) System.out.println("  Please retry\n\n");
        }

        if (lookAndFeel.equals("A")) {
            GGame = new GraphicalGame();
            GGame.startGUI();
        }
        else {
            CLGame = new CommandLineGame();
            CLGame.startCLI();
        }

    }
}

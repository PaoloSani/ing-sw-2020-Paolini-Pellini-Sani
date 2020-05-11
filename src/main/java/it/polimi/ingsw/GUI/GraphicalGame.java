package it.polimi.ingsw.GUI;

import it.polimi.ingsw.CLI.ColourFont;
import it.polimi.ingsw.CLI.CommandLineGame;

import javax.swing.*;

public class GraphicalGame extends JFrame {

    public void startGUI(){
        System.out.println(ColourFont.ANSI_CYAN_BACKGROUND + "  Not already implemented version" + ColourFont.ANSI_RESET);
        new CommandLineGame().runCLI();
    }
}

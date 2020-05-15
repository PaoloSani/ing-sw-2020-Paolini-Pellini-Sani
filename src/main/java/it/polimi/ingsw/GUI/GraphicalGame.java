package it.polimi.ingsw.GUI;

import it.polimi.ingsw.CLI.ColourFont;
import it.polimi.ingsw.CLI.CommandLineGame;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;

public class GraphicalGame extends Application {

    public void startGUI(){
        System.out.println(ColourFont.ANSI_CYAN_BACKGROUND + "  Not already implemented version" + ColourFont.ANSI_RESET);
        new CommandLineGame().runCLI();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}

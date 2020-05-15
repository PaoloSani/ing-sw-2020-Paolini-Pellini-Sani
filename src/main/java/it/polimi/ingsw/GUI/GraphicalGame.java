package it.polimi.ingsw.GUI;

import it.polimi.ingsw.CLI.ColourFont;
import it.polimi.ingsw.CLI.CommandLineGame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class GraphicalGame {

    public TextField lblName;
    public Button btnNext;

    public void startGUI(){
        System.out.println(ColourFont.ANSI_CYAN_BACKGROUND + "  Not already implemented version" + ColourFont.ANSI_RESET);
        new CommandLineGame().runCLI();
    }

    public void nextPage(ActionEvent actionEvent) {
    }
}

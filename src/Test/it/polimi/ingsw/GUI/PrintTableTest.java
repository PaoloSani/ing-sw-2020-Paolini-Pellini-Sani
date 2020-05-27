package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PrintTableTest {
    Game game = new Game();
    GUIHandler guiHandler = new GUIHandler();


    @Test
    public void printStartingGameTable(){
        game.getSpace(0,1).setHeight(2);
        game.getSpace(1,0).setHeight(2);
        game.getSpace(1,1).setHeight(1);
        game.getSpace(0,2).setHeight(1);
        game.getSpace(2,0).setHeight(1);
        game.getSpace(0,0).setHeight(3);
        game.getSpace(2,3).setHeight(1);
        game.getSpace(3,1).setHeight(3);
        game.getSpace(3,2).setHeight(2);
        game.getSpace(2,3).setHeight(2);
        game.getSpace(3,0).setHeight(4);
        game.getSpace(3,3).setHeight(1);
        game.getSpace(2,4).setHeight(1);
        game.getSpace(4,4).setDome();
        Player player1 = new Player("Rick", God.PAN,game);
        Player player2 = new Player("Paul", God.ZEUS,game);
        Player player3 = new Player("Giuse", God.HEPHAESTUS,game);
        game.setPlayers(player1, player2, player3);
        Worker w = new Worker(player1);
        game.getSpace(0,0).setWorker(new Worker(player1));
        game.getSpace(0,1).setWorker(new Worker(player1));
        game.getSpace(2,3).setWorker(new Worker(player2));
        w.setSpace(game.getSpace(1,4));
        game.getSpace(3,1).setWorker(new Worker(player3));
        game.setCurrWorker(w);
        game.refreshLiteGame();

        guiHandler.setNumOfPlayers(3);
        guiHandler.setSerializableLiteGame(game.getLiteGame().makeSerializable());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUIScenes/table.fxml"));

        try {

            Parent root = fxmlLoader.load();
            Stage nextStage = new Stage();
            nextStage.setScene(new Scene(root));
            nextStage.setTitle("SANTORINI");
            nextStage.setResizable(false);
            nextStage.setOnCloseRequest(event -> System.exit(0));
            nextStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TableWindow tableWindow = fxmlLoader.getController();
        tableWindow.setNewSLT(game.getLiteGame().makeSerializable());
        try {
            tableWindow.buildGameTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
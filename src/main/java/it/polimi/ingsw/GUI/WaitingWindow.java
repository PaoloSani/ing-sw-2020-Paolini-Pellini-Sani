package it.polimi.ingsw.GUI;

import it.polimi.ingsw.server.Message;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitingWindow extends GameWindow implements Initializable{
    public Label waitLabel;
    public Label IDLabel;
    public Button nextButton;

    public Stage stage = new Stage();
    public Label waitingLabelCards;
    private String toLoad;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

            if (guiHandler.getMode() == Mode.NEW_GAME) {
                guiHandler.createNewGame();
                String stringID = guiHandler.readString();
                int gameID = Integer.parseInt(stringID);
                guiHandler.setGameID(gameID);
                waitLabel.setText("WAITING FOR OTHER PLAYERS");
                waitLabel.setVisible(true);
                IDLabel.setText("THE GAME ID IS: " + guiHandler.getGameID());
                IDLabel.setVisible(true);
            } else if (guiHandler.getMode() == Mode.RANDOM_MATCH) {
                guiHandler.randomMatch();
                waitLabel.setText("WAITING FOR OTHER PLAYERS");
                waitLabel.setVisible(true);
                String messageFromServer = guiHandler.readString();
                if ( messageFromServer.contains("You are")) {
                    guiHandler.setMode(Mode.NEW_GAME);
                    IDLabel.setText("YOU ARE THE CHALLENGER");
                    IDLabel.setVisible(true);
                }
            } else if (guiHandler.getMode() == Mode.EXISTING_MATCH) {
                if (guiHandler.getMessageFromServer().equals(Message.BEGIN.toString())) {
                    waitLabel.setText("THE CHALLENGER IS CHOOSING THE GODS");
                    waitLabel.setVisible(true);
                }
                else if (guiHandler.getMessageFromServer().equals(Message.WAIT.toString())){
                    waitLabel.setVisible(false);
                    waitingLabelCards.setVisible(true);
                }
            }

        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                if (guiHandler.readString().contains("Game has started")) {
                    if (guiHandler.getMode() == Mode.NEW_GAME) {
                        return "/GUIScenes/challengerWindow.fxml";
                    } else {
                        waitLabel.setVisible(false);
                        waitingLabelCards.setVisible(true);
                        String messageFromFrontend = guiHandler.readString();
                        while (!messageFromFrontend.contains("[")){
                            messageFromFrontend = guiHandler.readString();
                        }
                        guiHandler.setMessageFromFrontend(messageFromFrontend);
                        return "/GUIScenes/chooseGodWindow.fxml";
                    }
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            toLoad = task.getValue();
            if (toLoad != null) {
                guiHandler.loadFXMLFile(nextButton, stage, toLoad);
                task.cancel();
            }
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}

package it.polimi.ingsw.GUI;

import it.polimi.ingsw.util.Message;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * It is the window where the player waits for other players to join the match.
 */

public class WaitingWindow extends GameWindow implements Initializable{

    /**
     * It shows the message of waiting.
     */
    @FXML
    public Label waitLabel;

    /**
     * It shows the gameID of the match, or tells the player that she/he is the challenger.
     */
    @FXML
    public Label IDLabel;

    /**
     * It is the stage which succeeds the current one.
     */
    public Stage stage = new Stage();

    /**
     * It is visible if players are choosing their cards.
     */
    @FXML
    public Label waitingLabelCards;

    /**
     * It is the pane of the current window.
     */
    @FXML
    public AnchorPane waitingPane;

    /**
     * It is the path of the FXML file to load when the waiting is finished.
     */
    private String toLoad;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        guiHandler.setCurrPane(waitingPane);

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
            String messageFromServer = guiHandler.getMessageFromServer();
            if (messageFromServer.equals(Message.BEGIN.toString())) {
                waitLabel.setText("THE CHALLENGER IS CHOOSING THE GODS");
                waitLabel.setVisible(true);
            }
            else if (messageFromServer.equals(Message.WAIT.toString())){
                waitLabel.setVisible(false);
                waitingLabelCards.setVisible(true);
                waitingLabelCards.setText("WAITING FOR OTHER PLAYERS");

            }
        }

        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                String messageFromServer = guiHandler.readString();
                if (messageFromServer.contains("Game has started")) {
                    if (guiHandler.getMode() == Mode.NEW_GAME) {
                        return "/GUIScenes/challengerWindow.fxml";
                    } else {
                        if ( guiHandler.getMode() == Mode.EXISTING_MATCH){
                            String[] array = messageFromServer.split("are");
                            if ( array[1].split(",").length == 2 )
                                guiHandler.setNumOfPlayers(2);
                            else guiHandler.setNumOfPlayers(3);
                        }
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
                guiHandler.loadFXMLFile(waitingPane, stage, toLoad);
                task.cancel();
            }
        });

        Thread thread = new Thread(task);
        thread.start();
    }
}

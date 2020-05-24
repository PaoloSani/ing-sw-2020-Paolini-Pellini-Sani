package it.polimi.ingsw.GUI;

import it.polimi.ingsw.server.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExistingGameWindow extends GameWindow {
    public TextField IDField;
    public Button nextButton;
    public Label waitLabel;

    private Stage nextStage = new Stage();


    public void goFurther(ActionEvent actionEvent) {
        int gameID;
        boolean validGameID = false;
        String messageFromServer = "none";

        gameID = Integer.parseInt(IDField.getText());
        guiHandler.setGameID(gameID);
        guiHandler.playExistingMatch();
        messageFromServer = guiHandler.readString();

        if (messageFromServer.equals(Message.BEGIN.toString())){
            IDField.setVisible(false);
            nextButton.setVisible(false);
            waitLabel.setText("THE CHALLENGER IS CHOOSING THE GODS");
            waitLabel.setVisible(true);

                Platform.runLater( () ->
                {
                    String message = guiHandler.readString();
                    if(message.contains("Game has started")) {
                        if (guiHandler.getMode() == Mode.NEW_GAME) {
                            guiHandler.loadFXMLFile(nextButton, nextStage, "/GUIScenes/challengerWindow.fxml");
                        } else {
                            guiHandler.loadFXMLFile(nextButton, nextStage, "/GUIScenes/waitingWindow.fxml");
                        }
                    }
                }
                );

        } else if (messageFromServer.equals(Message.WAIT.toString())){
            IDField.setVisible(false);
            nextButton.setVisible(false);
            waitLabel.setText("WAITING FOR AN OTHER PLAYER");
            waitLabel.setVisible(true);

            Platform.runLater( () ->
                    {
                        String message = guiHandler.readString();
                        if(message.contains("Game has started")) {
                            if (guiHandler.getMode() == Mode.NEW_GAME) {
                                guiHandler.loadFXMLFile(nextButton, nextStage, "/GUIScenes/challengerWindow.fxml");
                            } else {
                                guiHandler.loadFXMLFile(nextButton, nextStage, "/GUIScenes/waitingWindow.fxml");
                            }
                        }
                    }
            );

        } else if (messageFromServer.equals(Message.INVALID_ID.toString())){
            IDField.setText("PLEASE, TYPE A VALID ID");
        }

    }
}

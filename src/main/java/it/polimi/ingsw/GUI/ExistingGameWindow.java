package it.polimi.ingsw.GUI;

import it.polimi.ingsw.server.Message;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExistingGameWindow extends GameWindow {
    public TextField IDField;
    public Button nextButton;

    private Stage nextStage = new Stage();


    public void goFurther(ActionEvent actionEvent) {
        int gameID;
        String messageFromServer;

        gameID = Integer.parseInt(IDField.getText());
        guiHandler.setGameID(gameID);
        guiHandler.playExistingMatch();
        guiHandler.setMessageFromServer(guiHandler.readString());
        messageFromServer = guiHandler.getMessageFromServer();

        if (messageFromServer.equals(Message.BEGIN.toString())){
            guiHandler.loadFXMLFile(nextButton, nextStage, "/GUIScenes/waitingWindow.fxml");

        } else if (messageFromServer.equals(Message.WAIT.toString())){
            guiHandler.loadFXMLFile(nextButton, nextStage, "/GUIScenes/waitingWindow.fxml");

        } else if (messageFromServer.equals(Message.INVALID_ID.toString())){
            IDField.setText("PLEASE, TYPE A VALID ID");
        }

    }
}

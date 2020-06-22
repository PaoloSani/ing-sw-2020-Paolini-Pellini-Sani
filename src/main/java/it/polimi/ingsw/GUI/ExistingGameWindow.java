package it.polimi.ingsw.GUI;

import it.polimi.ingsw.util.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * It is the window where the player types a gameID ot play an existing match with friends.
 */
public class ExistingGameWindow extends GameWindow {

    /**
     * It is the field to fill in with a gameID.
     */
    @FXML
    public TextField IDField;

    /**
     * It is the button to press to confirm the gameID.
     */
    @FXML
    public Button nextButton;

    /**
     * It is the stage which succeeds the current one.
     */
    private final Stage nextStage = new Stage();


    /**
     * It sets the gameID typed by the player and sends it to the server. If the ID is not valid, the player must retype it.
     * @param actionEvent
     */
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

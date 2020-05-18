package it.polimi.ingsw.GUI;

import it.polimi.ingsw.server.Message;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExistingGameWindow extends GameWindow {
    public TextField IDField;
    public Button nextButton;

    private Stage nextStage = new Stage();


    public void goFurther(ActionEvent actionEvent) {
        int gameID;
        boolean validGameID = false;
        String messageFromServer = "none";

        gameID = Integer.parseInt(IDField.getText());
        guiHandler.setGameID(gameID);
        guiHandler.playExistingMatch();
        messageFromServer = guiHandler.readString();

        if (messageFromServer.equals(Message.BEGIN)){
            guiHandler.loadFXMLFile(nextButton, nextStage, "/GUIScenes/beginningMatchWindow.fxml");
        } else if (messageFromServer.equals(Message.WAIT)){
            guiHandler.loadFXMLFile(nextButton, nextStage, "/GUIScenes/waitingWindow.fxml");
        } else if (messageFromServer.equals(Message.INVALID_ID)){
            IDField.setText("PLEASE, TYPE A VALID ID");
        }

    }
}

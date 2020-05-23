package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public  class NicknameWindow extends GameWindow {

    @FXML
    public TextField fieldNickname;

    @FXML
    public Label adviseLabel;

    @FXML
    private Button nextButton;

    private String nickname;
    private Stage nextStage = new Stage();
    private boolean nicknameAccepted = false;

    @FXML
    public void goFurther(ActionEvent actionEvent) {

        nickname = fieldNickname.getText().toUpperCase();
        nicknameAccepted = guiHandler.askNameAvailability(nickname);
        if (nicknameAccepted) {
            guiHandler.loadFXMLFile(nextButton,nextStage,"/GUIScenes/modeWindow.fxml");
        }
        else {
            fieldNickname.clear();
            adviseLabel.setText("Please retype a valid nickname");
        }
    }
}

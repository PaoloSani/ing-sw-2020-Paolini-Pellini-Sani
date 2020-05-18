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
    private Stage modeStage = new Stage();
    private boolean nicknameAccepted = false;

    @FXML
    public void goFurther(ActionEvent actionEvent) {

        nickname = fieldNickname.getText().toUpperCase();
        nicknameAccepted = guiHandler.askNameAvailability(nickname);
        if (nicknameAccepted) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUIScenes/modeWindow.fxml"));
            try {
                Parent root = fxmlLoader.load();
                modeStage.setScene(new Scene(root));
                modeStage.setTitle("SANTORINI");
                modeStage.setResizable(false);
                modeStage.setOnCloseRequest(event -> System.exit(0));
                Stage toClose = (Stage) nextButton.getScene().getWindow();
                toClose.close();
                modeStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else adviseLabel.setText("Please retype");
    }
}

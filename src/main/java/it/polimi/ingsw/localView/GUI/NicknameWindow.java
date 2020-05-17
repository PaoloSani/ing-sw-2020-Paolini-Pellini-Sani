package it.polimi.ingsw.localView.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NicknameWindow {

    @FXML
    public TextField fieldNickname;

    @FXML
    private Button nextButton;

    private String nickname;
    private Stage modeStage = new Stage();

    @FXML
    public void goFurther(ActionEvent actionEvent) {

        nickname = fieldNickname.getText().toUpperCase();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUIScenes/modeWindow.fxml"));
        try {
            Parent root = fxmlLoader.load();
            modeStage.setScene(new Scene(root));
            modeStage.setTitle("SANTORINI");
            modeStage.setResizable(false);
            modeStage.setOnCloseRequest( event -> System.exit(0));
            Stage toClose = (Stage) nextButton.getScene().getWindow();
            toClose.close();
            modeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

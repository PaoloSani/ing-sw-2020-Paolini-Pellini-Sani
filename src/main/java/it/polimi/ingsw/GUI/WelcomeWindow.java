package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeWindow {

    @FXML
    private Button nextButton;

    private Stage welcomeStage = new Stage();

    public void goFurther(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUIScenes/nicknameWindow.fxml"));
        try {
            Parent root = fxmlLoader.load();
            welcomeStage.setScene(new Scene(root));
            welcomeStage.setTitle("SANTORINI");
            welcomeStage.setResizable(false);
            welcomeStage.setOnCloseRequest( event -> System.exit(0));
            welcomeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void pressButton(MouseEvent mouseEvent) {

    }
}

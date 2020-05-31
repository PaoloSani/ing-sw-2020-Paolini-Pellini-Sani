package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeWindow extends GameWindow {

    @FXML
    private Button nextButton;

    private Stage nextStage = new Stage();

    public void goFurther(ActionEvent actionEvent) {

        boolean connectionAlive = guiHandler.setClientConnection();
        if (connectionAlive) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUIScenes/nicknameWindow.fxml"));
            try {
                Parent root = fxmlLoader.load();
                nextStage.setScene(new Scene(root));
                nextStage.setTitle("SANTORINI");
                nextStage.setResizable(false);
                nextStage.setOnCloseRequest(event -> System.exit(0));
                Stage toClose = (Stage) nextButton.getScene().getWindow();
                toClose.close();
                nextStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ImageView imageView = new ImageView(new Image("/Backgrounds/Odyssey-Circe-scaled.png"));
            guiHandler.setErrorImage(imageView);
            imageView.setVisible(false);
        }
    }
}

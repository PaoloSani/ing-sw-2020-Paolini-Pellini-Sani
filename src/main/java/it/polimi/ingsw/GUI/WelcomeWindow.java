package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * It describes every graphical object implemented in the starting window of the application.
 */

public class WelcomeWindow extends GameWindow {

    /**
     * It is the button bound to the method goFurther. It lets the stage and the scene change, if clicked.
     */

    @FXML
    private Button nextButton;

    /**
     * It is the stage who follows the current stage when the latter gets closed.
     */

    private final Stage nextStage = new Stage();

    /**
     * It changes the current scene and the current stage by clicking on nextButton.
     * @param actionEvent generated by a click on nextButton
     */

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
        }
    }
}

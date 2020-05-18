package it.polimi.ingsw.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUILauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/GUIScenes/welcomeWindow.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("SANTORINI");
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.setResizable(false);
        stage.show();
    }

    public static void launchGUI(){
        launch();
    }


}

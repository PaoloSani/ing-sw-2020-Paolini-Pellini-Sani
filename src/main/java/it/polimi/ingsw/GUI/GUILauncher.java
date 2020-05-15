package it.polimi.ingsw.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUILauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("SANTORINI: ADVANCED");
        Button button = new Button();
        StackPane root = new StackPane();
        root.getChildren().add(button);
        stage.setScene(new Scene(root, 300, 250));
        stage.show();


    }

    public static void main(String[] args){ launch(args); }
}

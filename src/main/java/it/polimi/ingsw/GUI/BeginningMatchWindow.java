package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class BeginningMatchWindow extends GameWindow implements Initializable {
    public Label waitLabel;
    public Label IDLabel;
    public Button nextButton;

    public Stage stage = new Stage();
    private String toLoad;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String messageFromFrontEnd = guiHandler.readString();
                if( guiHandler.getMode() == Mode.NEW_GAME ) {
                    guiHandler.setClientMessage(God.valueOf(messageFromFrontEnd));
                }
                guiHandler.setSerializableLiteGame(guiHandler.readSerializableLG());
                return null;
            }
        };
        task.setOnSucceeded( event -> {
            guiHandler.loadFXMLFile(nextButton, stage , "/GUIScenes/table.fxml");
        });
        new Thread(task).start();
    }
}

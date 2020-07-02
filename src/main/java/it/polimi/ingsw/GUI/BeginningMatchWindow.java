package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * It is the waiting window before the beginning of the match.
 */

public class BeginningMatchWindow extends GameWindow implements Initializable {

    /**
     * It shows the message of waiting.
     */
    @FXML
    public Label waitLabel;

    /**
     * It shows the gameID of the match, or tells the player that she/he is the challenger.
     */
    @FXML
    public Label IDLabel;

    /**
     * It is the stage that succeeds the current one.
     */
    public Stage stage = new Stage();

    /**
     * It is visible if players are choosing their cards.
     */
    @FXML
    public Label waitingLabelCards;
    /**
     * It is the pane of the current window.
     */
    @FXML
    public AnchorPane beginningMatchPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                waitingLabelCards.setVisible(true);
                String messageFromFrontEnd = guiHandler.readString();
                if( guiHandler.getMode() == Mode.NEW_GAME ) {
                    guiHandler.setClientMessage(God.valueOf(messageFromFrontEnd));
                    guiHandler.setSerializableLiteGame(guiHandler.readSerializableLG());
                }
                else guiHandler.setSerializableLiteGame(guiHandler.readSerializableLG());
                return null;
            }
        };
        task.setOnSucceeded( event -> {
            guiHandler.loadFXMLFile(beginningMatchPane, stage , "/GUIScenes/table.fxml");
            task.cancel();
        });

        new Thread(task).start();
    }
}

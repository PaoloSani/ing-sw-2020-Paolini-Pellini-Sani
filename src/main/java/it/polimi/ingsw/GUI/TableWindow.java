package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TableWindow extends GameWindow implements Initializable {
    public Label waitLabel;
    public Label IDLabel;
    public Button nextButton;

    public Stage stage = new Stage();
    private String toLoad;

    private SerializableLiteGame  newSLG;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String messageFromFrontEnd = guiHandler.readString();
                if( messageFromFrontEnd.contains("Placing") ) placingWorkers();


                //guiHandler.setSerializableLiteGame(guiHandler.readSerializableLG());
                return null;
            }
        };
        task.setOnSucceeded( event -> {
            guiHandler.loadFXMLFile(nextButton, stage , "/GUIScenes/table.fxml");
        });
        new Thread(task).start();
    }

    private void placingWorkers() {
        String messageFromFrontEnd = "none";
        while (!messageFromFrontEnd.equals("Placing workers")) {
            messageFromFrontEnd = guiHandler.readString();
            System.out.println("  " + messageFromFrontEnd);
            if (messageFromFrontEnd.contains("Wait")) {
                guiHandler.setSerializableLiteGame(guiHandler.readSerializableLG());
                buildGameTable();
            }
        }
        boolean validPlacing = false;
        while (!validPlacing) {
            guiHandler.getClientMessage().setSpace1(getSpaceFromClient());
            guiHandler.getClientMessage().setSpace2(getSpaceFromClient());
            guiHandler.getClientConnection().send(guiHandler.getClientMessage());
            newSLG = guiHandler.readSerializableLG();
            if (!newSLG.equalsSLG(guiHandler.getSerializableLiteGame())) {
                validPlacing = true;
                guiHandler.setSerializableLiteGame() = newSLG;
            }
            buildGameTable();
            if (!validPlacing) System.out.println("  Please retype two correct spaces!");
        }
    }
}


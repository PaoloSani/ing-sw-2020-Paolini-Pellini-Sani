package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TableWindow extends GameWindow implements Initializable {
    public Button button00;
    public Button button01;
    public Button button02;
    public Button button03;
    public Button button04;
    public Button button10;
    public Button button11;
    public Button button12;
    public Button button13;
    public Button button14;
    public Button button20;
    public Button button21;
    public Button button22;
    public Button button23;
    public Button button24;
    public Button button30;
    public Button button31;
    public Button button32;
    public Button button33;
    public Button button34;
    public Button button40;
    public Button button41;
    public Button button42;
    public Button button43;
    public Button button44;
    public ImageView image00;
    public ImageView image01;
    public ImageView image02;
    public ImageView image03;
    public ImageView image04;
    public ImageView image10;
    public ImageView image11;
    public ImageView image12;
    public ImageView image13;
    public ImageView image14;
    public ImageView image20;
    public ImageView image21;
    public ImageView image22;
    public ImageView image23;
    public ImageView image24;
    public ImageView image30;
    public ImageView image31;
    public ImageView image32;
    public ImageView image33;
    public ImageView image34;
    public ImageView image40;
    public ImageView image41;
    public ImageView image42;
    public ImageView image43;
    public ImageView image44;

    private SerializableLiteGame newSLG;

    public void doSomething(ActionEvent actionEvent) {

    }

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

    public void placingWorker() {
        String messageFromFrontEnd = "none";
        while (!messageFromFrontEnd.equals("Placing workers")) {
            messageFromFrontEnd = guiHandler.readString();
            System.out.println("  " + messageFromFrontEnd);
            if (messageFromFrontEnd.contains("Wait")) {
                guiHandler.setSerializableLiteGame(guiHandler.getSerializableLiteGame());
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
                guiHandler.setSerializableLiteGame(newSLG);
            }
            buildGameTable();
            if (!validPlacing) System.out.println("  Please retype two correct spaces!");

        }
    }
}

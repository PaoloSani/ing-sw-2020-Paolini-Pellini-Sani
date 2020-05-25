package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.SerializableLiteGame;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class TableWindow extends GameWindow implements Initializable {
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
    public GridPane gameTable;
    private int[] coordinates;
    private SerializableLiteGame newSLG;

    public void doSomething(ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();
        String coordToSplit = clickedButton.getText();
        String[] coordToParse = coordToSplit.split("-");
        coordinates = new int[]{Integer.parseInt(coordToParse[0]),Integer.parseInt(coordToParse[1])};
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (int row = 0; row < 5; row++){
            for (int col = 0; col <5; col++){
                Button button = new Button();
                button.setOpacity(1.0);
                button.setText(row+"-"+col);
                button.setPrefSize(82,80);
                button.setOnAction(this::doSomething);
                gameTable.add(button,col,row);
            }
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                placingWorker();
                guiHandler.setSerializableLiteGame(guiHandler.readSerializableLG());
                return null;
            }
        };
        task.setOnSucceeded( event -> {
           //guiHandler.loadFXMLFile(nextButton, stage , "/GUIScenes/table.fxml");
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void placingWorker() {
        String messageFromFrontEnd = "none";
        while (!messageFromFrontEnd.equals("Placing workers")) {
            messageFromFrontEnd = guiHandler.readString();
            System.out.println("  " + messageFromFrontEnd);
            if (messageFromFrontEnd.contains("Wait")) {
                guiHandler.setSerializableLiteGame(guiHandler.getSerializableLiteGame());
            }
        }
        boolean validPlacing = false;
        while (!validPlacing) {
            while(coordinates == null);
            System.out.println("space1 ok");
            guiHandler.getClientMessage().setSpace1(coordinates);
            coordinates = null;
            while(coordinates == null);
            System.out.println("space2 ok");
            guiHandler.getClientMessage().setSpace2(coordinates);
            guiHandler.getClientConnection().send(guiHandler.getClientMessage());
            newSLG = guiHandler.readSerializableLG();
            if (!newSLG.equalsSLG(guiHandler.getSerializableLiteGame())) {
                validPlacing = true;
                guiHandler.setSerializableLiteGame(newSLG);
            }
            if (!validPlacing) System.out.println("  Please retype two correct spaces!");

        }
    }

}

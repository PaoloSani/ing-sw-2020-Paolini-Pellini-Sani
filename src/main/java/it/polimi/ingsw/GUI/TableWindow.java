package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

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
    public Label messageLabel;

    private int[] coordinates1 = null;
    private int[] coordinates2 = null;
    private SerializableLiteGame newSLG;
    private CountDownLatch countDownLatch;
    private String messageFromServer;
    private String lastAction;
    private boolean answerYes;

    public void mouseClicking(ActionEvent actionEvent) {
        if ( !messageLabel.getText().contains("Wait") ) {
            Button clickedButton = (Button) actionEvent.getSource();
            String coordToSplit = clickedButton.getText();
            String[] coordToParse = coordToSplit.split("-");
            if (coordinates1 == null)
                coordinates1 = new int[]{Integer.parseInt(coordToParse[0]), Integer.parseInt(coordToParse[1])};
            else if (coordinates2 == null)
                coordinates2 = new int[]{Integer.parseInt(coordToParse[0]), Integer.parseInt(coordToParse[1])};
            countDownLatch.countDown();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        for (int row = 0; row < 5; row++){
            for (int col = 0; col <5; col++){
                Button button = new Button();
                button.setOpacity(1.0);
                button.setText(row+"-"+col);
                button.setPrefSize(82,80);
                button.setOnAction(this::mouseClicking);
                gameTable.add(button,col,row);
            }
        }

        countDownLatch = new CountDownLatch(1);

        Thread currThread;

        Task<String> readStringTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                String messageFromFrontEnd = guiHandler.readString();
                return messageFromFrontEnd;
            }
        };

        readStringTask.setOnSucceeded( event -> {
            messageLabel.setText(readStringTask.getValue());
        });

        Task<Void> getSpaceClickedTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if((guiHandler.getClientMessage().getSpace1() == null) || (guiHandler.getClientMessage().getSpace2() != null)) {
                    System.out.println("space1 ok");
                    guiHandler.getClientMessage().setSpace1(coordinates1.clone());
                    coordinates1 = null;
                } else if (guiHandler.getClientMessage().getSpace2() == null){
                    System.out.println("space2 ok");
                    guiHandler.getClientMessage().setSpace1(coordinates2.clone());
                }
                countDownLatch = new CountDownLatch(1);
                return null;
            }
        };

        getSpaceClickedTask.setOnSucceeded( event -> {
            if ( lastAction.equals("Build") ){
                if ( guiHandler.getClientMessage().getGod() == God.ATLAS ){

                }
            }
        });

        /*Task<String> readChoiceTask = new Task<String>() {
            @Override
            protected String call() throws Exception {

                return ;
            }
        };*/



        currThread = new Thread(readStringTask);
        currThread.start();
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
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("space1 ok");
            guiHandler.getClientMessage().setSpace1(coordinates1.clone());
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("space2 ok");
            guiHandler.getClientMessage().setSpace2(coordinates2.clone());
            guiHandler.getClientConnection().send(guiHandler.getClientMessage());
            coordinates1 = null;
            newSLG = guiHandler.readSerializableLG();
            if (!newSLG.equalsSLG(guiHandler.getSerializableLiteGame())) {
                validPlacing = true;
                guiHandler.setSerializableLiteGame(newSLG);
            }
            if (!validPlacing) System.out.println("  Please retype two correct spaces!");

        }
    }

}

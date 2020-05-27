package it.polimi.ingsw.GUI;

import it.polimi.ingsw.CLI.ColourFont;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

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
    private boolean answerYes;
    private boolean endOfTheGame = false;
    String lastAction = "none";


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
        countDownLatch = new CountDownLatch(1);
        Thread currThread;

        for (int row = 0; row < 5; row++){
            for (int col = 0; col <5; col++){
                Button button = new Button();
                button.setOpacity(0.0);
                button.setText(row+"-"+col);
                button.setPrefSize(82,80);
                button.setOnAction(this::mouseClicking);
                gameTable.add(button,col,row);
            }
        }

        Task<String> readStringTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                String messageFromFrontEnd = guiHandler.readString();
                return messageFromFrontEnd;
            }
        };

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

        readStringTask.setOnSucceeded( event -> {
            messageLabel.setText(readStringTask.getValue());
            if ( messageLabel.getText().contains("Wait") ){
                Thread newThread = new Thread(readStringTask);
            }
            else if ( messageLabel.getText().contains("Placing Workers")){
                Thread newThread = new Thread(getSpaceClickedTask);
            }
            else if ( messageLabel.getText().contains("Next Action")){

            }
            else if ( messageLabel.getText().contains("Invalid Action") ){
                messageLabel.setText("Repeat your" + getLastAction());
            }
            else {
                //load della finestra finale
            }
        });

        Task<SerializableLiteGame> readLitegameTask = new Task<SerializableLiteGame>() {
            @Override
            protected SerializableLiteGame call() throws Exception {
                SerializableLiteGame newSLG = guiHandler.readSerializableLG();
                return newSLG;
            }
        };

        readLitegameTask.setOnSucceeded( event -> {
            newSLG = readLitegameTask.getValue();
        });




        getSpaceClickedTask.setOnSucceeded( event -> {
            if ( guiHandler.getClientMessage().getSpace2() == null ) {
                messageLabel.setText("Choose the second space!");
                Thread newThread = new Thread(getSpaceClickedTask);
            }
            else if ( getLastAction().equals("Build") ){

            }
            else {

            }
        });

        Task<String> readChoiceTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return "none";
            }
        };

        currThread = new Thread(readStringTask);
        currThread.start();


        while(!endOfTheGame) {
            boolean repeat = false;
            String messageToPrint = "none";

            while(!endOfTheGame) {
                messageFromFrontEnd = readString();
                if(messageFromFrontEnd.equals("Next action") ){
                    if ( !repeat ) {
                        if (lastAction.equals("none") || lastAction.equals("End")) {
                            lastAction = "Choose Worker";
                            messageToPrint = "  Select the worker you want to play with";
                        }

                        else if (lastAction.equals("Choose Worker")) {
                            if (god == God.CHARON) {
                                charonSwitch++;
                                messageToPrint = "  Please select a space(ROW-COL)";
                            }
                            else if (god == God.PROMETHEUS) {
                                System.out.println("  Do you want to use Prometheus power? (yes/no)");
                                if (in.nextLine().equalsIgnoreCase("YES")) {
                                    lastAction = "Prometheus Build";
                                    messageToPrint = "  Please select the space where you want to build (ROW-COL)";
                                }
                                else {
                                    lastAction = "Move";
                                    messageToPrint = "  Please select the space you want to occupy (ROW-COL)";
                                    moveCounter++;
                                }
                            } else {
                                lastAction = "Move";
                                moveCounter++;
                                messageToPrint = "  Please select the space you want to occupy (ROW-COL)";
                            }

                        } else if (lastAction.equals("Charon Switch") || lastAction.equals("Prometheus Build")){
                            lastAction = "Move";
                            moveCounter++;
                            messageToPrint = "  Please select the space you want to occupy (ROW-COL)";
                        }
                        else if (lastAction.equals("Move")) {
                            if ((god == God.ARTEMIS && moveCounter == 1) || ( god == God.TRITON && serializableLiteGame.isPerimetralSpace(lastSpace) )) {
                                System.out.println("  Do you want to move again? (yes/no)");
                                if (in.nextLine().equalsIgnoreCase("YES")) {
                                    lastAction = "Move";
                                    messageToPrint = "  Please select the space you want to occupy (ROW-COL)";
                                    moveCounter++;
                                } else {
                                    lastAction = "Build";
                                    buildCounter++;
                                    messageToPrint = "  Please select the space where you want to build (ROW-COL)";
                                }
                            } else {
                                buildCounter++;
                                lastAction = "Build";
                                messageToPrint = "  Please select the space where you want to build (ROW-COL)";
                            }
                        } else if (lastAction.equals("Build")) {
                            if ( ((god == God.HEPHAESTUS || god == God.DEMETER) && buildCounter == 1)  ||     //se Efesto o Demetra e ha già fatto una sola build
                                    ( god == God.POSEIDON && buildCounter > 0 && buildCounter < 4 &&              // se Poseidone e ha già fatto una o più costruzioni (max 3)
                                            !Arrays.equals(firstWorker, serializableLiteGame.getCurrWorker()))           ){       // sta giocando con il suo secondo worker

                                System.out.println("  Do you want to build again? (yes/no)");
                                if (in.nextLine().equalsIgnoreCase("YES") ) {
                                    lastAction = "Build";
                                    messageToPrint = "  Please select the space where you want to build (ROW-COL)";
                                    buildCounter++;
                                }
                                else{
                                    moveCounter = 0;
                                    buildCounter = 0;
                                    messageToPrint = "  End of the turn";
                                    lastAction = "End";
                                }
                            } else {
                                moveCounter = 0;
                                buildCounter = 0;
                                messageToPrint = "  End of the turn";
                                lastAction = "End";
                            }
                        }
                    }
                    else repeat = false;

                    if( !lastAction.equals("End") ) {
                        System.out.println(messageToPrint);
                        lastSpace = getSpaceFromClient();
                        clientMessage.setSpace1(lastSpace);
                        if ( god == God.CHARON && charonSwitch == 1 ){
                            String space = serializableLiteGame.getStringValue(lastSpace[0], lastSpace[1]);
                            if ( !space.contains("V") ){
                                lastAction = "Charon Switch";
                            }
                            else {
                                lastAction = "Move";
                                moveCounter++;
                            }
                        }
                        if (lastAction.contains("Build")) {
                            if ( god == God.ATLAS ) {
                                System.out.println("  Do you want to build a dome? (yes/no)");
                                if(in.nextLine().equals("yes")) clientMessage.setLevelToBuild(4);
                                else clientMessage.setLevelToBuild(serializableLiteGame.getHeight(clientMessage.getSpace1())+1);
                            }
                            else clientMessage.setLevelToBuild(serializableLiteGame.getHeight(clientMessage.getSpace1())+1);
                            if ( buildCounter == 1 ) {
                                firstWorker = serializableLiteGame.getCurrWorker();
                            }
                        }
                    }
                    clientMessage.setAction(lastAction);
                    clientConnection.send(clientMessage);
                }
                //Siamo in caso in cui o abbiamo vinto o abbiamo perso
                else if(!messageFromFrontEnd.contains("Wait")){
                    endOfTheGame = true;
                }

                else System.out.println(messageFromFrontEnd);

                serializableLiteGame = readSerializableLG();
                buildGameTable();

                if ( messageFromFrontEnd.equals("Next action") && !lastAction.equals("End") ) {
                    messageFromFrontEnd = readString();

                    if ( messageFromFrontEnd.equals("Invalid action") ) {
                        repeat = true;
                        System.out.println("  " + messageFromFrontEnd);
                    }
                    else charonSwitch = 0;
                }
            }
            System.out.println(messageFromFrontEnd);
            serializableLiteGame = readSerializableLG();
            buildGameTable();
        }
        */

    }

    private void buildGameTable() {
        gameTable.getChildren().removeIf(imageView -> imageView instanceof ImageView);
        for( int i = 0; i < 5; i++){
            for( int j = 0; j < 5; j++ ){
                buildGameSpace(i,j);
            }
        }
    }

    private void buildGameSpace(int i, int j) {
        char[] spaceToPrint;
        ImageView building = new ImageView(), worker = new ImageView(), currWorker = new ImageView();
        spaceToPrint = newSLG.getTable()[i][j].toCharArray();


        if (spaceToPrint[1] == '0' && spaceToPrint[2] == 'D' ) building.setImage(new Image("/Table/4.png"));
        else if (spaceToPrint[1] == '1') {
            if (spaceToPrint[2] == 'D') {
                building.setImage(new Image("/Table/1+4.png"));
            }
            else building.setImage(new Image("/Table/1.png"));

        }
        else if (spaceToPrint[1] == '2') {
            if (spaceToPrint[2] == 'D') {
                building.setImage(new Image("/Table/1+2+4.png"));
            }
            else building.setImage(new Image("/Table/1+2.png"));

        }
        else if (spaceToPrint[1] == '3') {
            if (spaceToPrint[2] == 'D') {
                building.setImage(new Image("/Table/1+2+3+4.png"));
            }
            else building.setImage(new Image("/Table/1+2+3.png"));
        }
        switch(spaceToPrint[0]){
            case 'A':
                worker.setImage(new Image("/Table/male3"));
                break;

            case 'B':
                worker.setImage(new Image("/Table/male5"));
                break;

            case 'C':
                worker.setImage(new Image("/Table/male1"));
                break;

            default:
                worker.setImage(null);
                break;
        }

        if (newSLG.getCurrWorker()[0] == i && newSLG.getCurrWorker()[1] == j){
            currWorker.setImage(new Image("/Table/playermoveindicator_blue.png"));
        }

        gameTable.add(building, j , i );
        gameTable.add(worker, j , i );
        gameTable.add(currWorker, j , i );

    }

    private String getLastAction() {
        return lastAction;
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

package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.Integer.parseInt;

public class TableWindow extends GameWindow implements Initializable {
    public GridPane gameTable;
    public Label messageLabel;
    public Label player1label;
    public Label player2label;
    public Label player3label;
    public ImageView worker1image;
    public ImageView worker2image;
    public ImageView worker3image;
    public Label god1label;
    public Label god2label;
    public Label god3label;
    public Label level1label;
    public Label level2label;
    public Label level3label;
    public Label domeLabel;
    public ImageView podium;
    public ImageView label;
    public ImageView endingImage;
    public ImageView goldenGlow;
    public ImageView winningPlayer;
    public Label winningLabel;
    public ImageView background;
    public ImageView player2Turn;
    public ImageView player3Turn;
    public ImageView player1Turn;
    public Pane endingPane;
    private SerializableLiteGame newSLG;
    private SerializableLiteGame muuSLG;
    private boolean endOfTheGame = false;
    private String lastAction = "none";
    private String messageFromFrontEnd = "none";
    private BlockingQueue<Object> clientChoices = new LinkedBlockingDeque<>();


    public void mouseClicking(ActionEvent actionEvent) {
        if ( !messageLabel.getText().contains("Wait") ) {
            Button clickedButton = (Button) actionEvent.getSource();
            String coordToSplit = clickedButton.getText();
            String[] coordToParse = coordToSplit.split("-");
            clientChoices.add(new int[]{Integer.parseInt(coordToParse[0]), Integer.parseInt(coordToParse[1])});
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        guiHandler.setErrorImage(endingImage);


        String nameOfMyPlayer;
        if ( guiHandler.getClientMessage().getName().equals(guiHandler.getSerializableLiteGame().getName1() )) {
            nameOfMyPlayer = guiHandler.getClientMessage().getName() + " (YOU)";
            player1label.setText(nameOfMyPlayer);
            player2label.setText(guiHandler.getSerializableLiteGame().getName2());
            player1Turn.setImage(new Image("/Decorations/frame_yellow.png"));
            player2Turn.setImage(new Image("/Decorations/frame_white.png"));
        }
        else if ( guiHandler.getClientMessage().getName().equals(guiHandler.getSerializableLiteGame().getName2() )) {
            nameOfMyPlayer = guiHandler.getClientMessage().getName() + " (YOU)";
            player2label.setText(nameOfMyPlayer);
            player1label.setText(guiHandler.getSerializableLiteGame().getName1());
            player1Turn.setImage(new Image("/Decorations/frame_white.png"));
            player2Turn.setImage(new Image("/Decorations/frame_yellow.png"));
        }

        god1label.setText(guiHandler.getSerializableLiteGame().getGod1().toString());
        god2label.setText(guiHandler.getSerializableLiteGame().getGod2().toString());

        if (guiHandler.getNumOfPlayers() == 3){
            if ( guiHandler.getClientMessage().getName().equals(guiHandler.getSerializableLiteGame().getName3() )) {
                nameOfMyPlayer = guiHandler.getClientMessage().getName() + " (YOU)";
                player3label.setText(nameOfMyPlayer);
                player1label.setText(guiHandler.getSerializableLiteGame().getName1());
                player2label.setText(guiHandler.getSerializableLiteGame().getName2());
                player1Turn.setImage(new Image("/Decorations/frame_white.png"));
                player2Turn.setImage(new Image("/Decorations/frame_white.png"));
                player3Turn.setImage(new Image("/Decorations/frame_yellow.png"));
            }
            else {
                player3label.setText(guiHandler.getSerializableLiteGame().getName3());
                player3Turn.setImage(new Image("/Decorations/frame_white.png"));
            }
            god3label.setText(guiHandler.getSerializableLiteGame().getGod3().toString());
            player3Turn.setFitHeight(148.0);
            player3Turn.setFitWidth(59.0);
            player3Turn.setRotate(90.0);
            player3Turn.setLayoutX(70.0);
            player3Turn.setLayoutX(45.0);
        }
        else{
            player3label.setVisible(false);
            god3label.setVisible(false);
            worker3image.setVisible(false);
        }

        /*player1Turn.setFitHeight(148.0);
        player1Turn.setFitWidth(59.0);
        //player1Turn.setRotate(90.0);
        player1Turn.setLayoutX(70.0);
        player1Turn.setLayoutX(45.0);
        player2Turn.setFitHeight(148.0);
        player2Turn.setFitWidth(59.0);
        player2Turn.setLayoutX(70.0);
        player2Turn.setLayoutX(104.0);
        player3Turn.setFitHeight(148.0);
        player3Turn.setFitWidth(59.0);
        player3Turn.setLayoutX(70.0);
        player3Turn.setLayoutX(159.0);*/
        player1Turn.setVisible(false);
        player2Turn.setVisible(true);
        player3Turn.setVisible(false);

        currThread = new Thread(this::runGUI);
        currThread.start();
    }

    private void runGUI() {
        God god = guiHandler.getClientMessage().getGod();
        int moveCounter =0, buildCounter = 0;
        boolean repeat = false;
        int [] lastSpace = new int[]{5,5};
        int[] firstWorker = new int[]{5,5};
        int charonSwitch = 0;
        String messageToPrint = "none";

        Platform.runLater( () ->{
            Scene currScene = messageLabel.getScene();
            currScene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
                if ( key.getCode() == KeyCode.D ) {
                    clientChoices.add(KeyCode.D.toString());
                } else if ( key.getCode() == KeyCode.B ) {
                    clientChoices.add(KeyCode.B.toString());
                } else if ( key.getCode() == KeyCode.E ) {
                    clientChoices.add(KeyCode.E.toString());
                }
            });
        });




        placeWorkers();
        while(!endOfTheGame) {
            try {
                messageFromFrontEnd = guiHandler.readString();
                if (messageFromFrontEnd.equals("Next action")) {
                    if (!repeat) {
                        if (lastAction.equals("none") || lastAction.equals("End")) {
                            lastAction = "Choose Worker";
                            messageToPrint = "Select the worker you want to play with";
                        } else if (lastAction.equals("Choose Worker")) {
                            if (god == God.CHARON) {
                                charonSwitch++;
                                messageToPrint = "Please select a space";
                            } else if (god == God.PROMETHEUS) {
                                setMessageLabel("Move or press B to build");
                                if ("B".equals(clientChoices.take())) {
                                    lastAction = "Prometheus Build";
                                    messageToPrint = "Please select the space where you want to build";
                                } else {
                                    lastAction = "Move";
                                    messageToPrint = "Please select the space you want to occupy";
                                    moveCounter++;
                                }
                            } else {
                                lastAction = "Move";
                                moveCounter++;
                                messageToPrint = "Please select the space you want to occupy";
                            }

                        } else if (lastAction.equals("Charon Switch") || lastAction.equals("Prometheus Build")) {
                            lastAction = "Move";
                            moveCounter++;
                            messageToPrint = "Please select the space you want to occupy";
                        } else if (lastAction.equals("Move")) {
                            if ((god == God.ARTEMIS && moveCounter == 1) || (god == God.TRITON && guiHandler.getSerializableLiteGame().isPerimetralSpace(lastSpace))) {
                                setMessageLabel("Move again or press B to build");
                                if ("B".equals(clientChoices.take())) {
                                    lastAction = "Build";
                                    buildCounter++;
                                    messageToPrint = "Please select the space where you want to build";
                                } else {
                                    lastAction = "Move";
                                    messageToPrint = "Please select the space you want to occupy";
                                    moveCounter++;
                                }
                            } else {
                                buildCounter++;
                                lastAction = "Build";
                                messageToPrint = "Please select the space where you want to build";
                            }
                        } else if (lastAction.equals("Build")) {
                            if (((god == God.HEPHAESTUS || god == God.DEMETER) && buildCounter == 1) ||     //se Efesto o Demetra e ha già fatto una sola build
                                    (god == God.POSEIDON && buildCounter > 0 && buildCounter < 4 &&              // se Poseidone e ha già fatto una o più costruzioni (max 3)
                                            !Arrays.equals(firstWorker, guiHandler.getSerializableLiteGame().getCurrWorker()))) {       // sta giocando con il suo secondo worker

                                setMessageLabel("Build again or press E to end your turn");
                                if ("E".equals(clientChoices.take())) {
                                    moveCounter = 0;
                                    buildCounter = 0;
                                    messageToPrint = "End of the turn";
                                    lastAction = "End";
                                } else {
                                    lastAction = "Build";
                                    messageToPrint = "Please select the space where you want to build";
                                    buildCounter++;
                                }
                            } else {
                                moveCounter = 0;
                                buildCounter = 0;
                                messageToPrint = "End of the turn";
                                lastAction = "End";
                            }
                        }
                    } else repeat = false;

                    if (!lastAction.equals("End")) {
                        setMessageLabel(messageToPrint);
                        clientChoices.clear();
                        lastSpace = (int[]) clientChoices.take();
                        guiHandler.getClientMessage().setSpace1(lastSpace);
                        if (god == God.CHARON && charonSwitch == 1) {
                            String space = guiHandler.getSerializableLiteGame().getStringValue(lastSpace[0], lastSpace[1]);
                            if (!space.contains("V")) {
                                lastAction = "Charon Switch";
                            } else {
                                lastAction = "Move";
                                moveCounter++;
                            }
                        }
                        if (lastAction.contains("Build")) {
                            if (god == God.ATLAS) {
                                setMessageLabel("Press D to build a dome, else press B");
                                if ("D".equals(clientChoices.take())) guiHandler.getClientMessage().setLevelToBuild(4);
                                else
                                    guiHandler.getClientMessage().setLevelToBuild(guiHandler.getSerializableLiteGame().getHeight(guiHandler.getClientMessage().getSpace1()) + 1);
                            } else
                                guiHandler.getClientMessage().setLevelToBuild(guiHandler.getSerializableLiteGame().getHeight(guiHandler.getClientMessage().getSpace1()) + 1);
                            if (buildCounter == 1) {
                                firstWorker = guiHandler.getSerializableLiteGame().getCurrWorker();
                            }
                        }
                    }
                    guiHandler.getClientMessage().setAction(lastAction);
                    guiHandler.getClientConnection().send(guiHandler.getClientMessage());
                }
                //Siamo in caso in cui o abbiamo vinto o abbiamo perso
                else if ( !messageFromFrontEnd.contains("Wait") ) {
                    endOfTheGame = true;
                }
                else setMessageLabel(messageFromFrontEnd);

                if ( !endOfTheGame ) {
                    guiHandler.setSerializableLiteGame(guiHandler.readSerializableLG());
                    buildGameTable();
                }

                if (messageFromFrontEnd.equals("Next action") && !lastAction.equals("End")) {
                    messageFromFrontEnd = guiHandler.readString();

                    if (messageFromFrontEnd.equals("Invalid action")) {
                        repeat = true;
                        setMessageLabel(messageFromFrontEnd);
                    } else charonSwitch = 0;
                }
            }
            catch ( InterruptedException e) {
                e.printStackTrace();
            }
        }
        setMessageLabel(messageFromFrontEnd);
        Platform.runLater( () ->
        {


            background.setOpacity(0.5);
            endingPane.setVisible(true);
            for ( Node imageView: gameTable.getChildren()){
                if( imageView instanceof ImageView) imageView.setOpacity(0.5);
            }
            showWinner();
            if (messageFromFrontEnd.contains("You won")) {
                endingImage.setVisible(true);
                endingImage.setImage(new Image("Backgrounds/winningWindow.PNG"));
            }
            else {
                endingImage.setVisible(true);
                endingImage.setImage(new Image("Backgrounds/losingWindow.PNG"));
            }

        });
    }

    private void showWinner() {
        String winner = guiHandler.getSerializableLiteGame().getCurrPlayer();
        goldenGlow.setVisible(true);
        podium.setVisible(true);
        winningPlayer.setVisible(true);
        label.setVisible(true);
        winningLabel.setVisible(true);
        winningLabel.setText( winner + " won the match!");
        God winningGod ;
        if ( winner.equals(guiHandler.getSerializableLiteGame().getName1()))
            winningGod= guiHandler.getSerializableLiteGame().getGod1();
        else if ( winner.equals(guiHandler.getSerializableLiteGame().getName2()))
            winningGod= guiHandler.getSerializableLiteGame().getGod2();
        else winningGod= guiHandler.getSerializableLiteGame().getGod3();

        switch (winningGod) {
            case APOLLO: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Apollo.png"));
                break;
            }
            case ARTEMIS: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Artemis.png"));
                break;
            }
            //
            case ATHENA: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Athena.png"));
                break;
            }
            case ATLAS: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Atlas.png"));
                break;
            }
            //
            case CHARON: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Charon.png"));
                break;
            }
            //
            case DEMETER: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Demeter.png"));
                break;
            }
            case HEPHAESTUS: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Hephaestus.png"));
                break;
            }
            //
            case HYPNUS: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Hypnus.png"));
                break;
            }

            case MINOTAUR: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Minotaur.png"));
                break;
            }
            case MORTAL: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Mortal.png"));
                break;
            }
            case PAN: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Pan.png"));
                break;
            }
            case POSEIDON: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Poseidon.png"));
                break;
            }
            case PROMETHEUS: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Prometheus.png"));
                break;
            }
            case TRITON: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Triton.png"));
                break;
            }
            case ZEUS: {
                winningPlayer.setImage(new Image("PodiumAvatar/podium-characters-Zeus.png"));
                break;
            }
        }
        TimerTask task = new TimerTask()
        {
            public void run()
            {

            }

        };
        Timer timer = new Timer();
        timer.schedule(task,10000l);
    }

    public void buildGameTable() {
        Platform.runLater(() ->{
            gameTable.getChildren().removeIf(imageView -> imageView instanceof ImageView);
            for( int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    try {
                        buildGameSpace(i, j);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            level1label.setText("level 1: " + guiHandler.getSerializableLiteGame().getLevel1());
            level2label.setText("level 2: " + guiHandler.getSerializableLiteGame().getLevel2());
            level3label.setText("level 3: " + guiHandler.getSerializableLiteGame().getLevel3());
            domeLabel.setText("dome: " + guiHandler.getSerializableLiteGame().getDome());
            if (guiHandler.getSerializableLiteGame().getCurrPlayer().equals(guiHandler.getSerializableLiteGame().getName1())){
                player1Turn.setVisible(true);
                player2Turn.setVisible(false);
                player3Turn.setVisible(false);
            }
            else if (guiHandler.getSerializableLiteGame().getCurrPlayer().equals(guiHandler.getSerializableLiteGame().getName2())){
                player1Turn.setVisible(false);
                player2Turn.setVisible(true);
                player3Turn.setVisible(false);
            }
            else if (guiHandler.getSerializableLiteGame().getCurrPlayer().equals(guiHandler.getSerializableLiteGame().getName3())){
                player1Turn.setVisible(false);
                player2Turn.setVisible(false);
                player3Turn.setVisible(true);
            }

        });
    }

    public void buildGameSpace(int i, int j) throws FileNotFoundException {
        char[] spaceToPrint;
        ImageView building = new ImageView(), worker = new ImageView(), currWorker = new ImageView();

        spaceToPrint = guiHandler.getSerializableLiteGame().getTable()[i][j].toCharArray();


        if (spaceToPrint[1] == '0' && spaceToPrint[2] == 'D' ) building.setImage(new Image("/Table/4.png"));
        else if (spaceToPrint[1] == '1') {
            if (spaceToPrint[2] == 'D') {
                building.setImage(new Image("/Table/1+4.png"));
            }
            else {
                building.setImage(new Image("/Table/1.png"));
            }

        }
        else if (spaceToPrint[1] == '2') {
            if (spaceToPrint[2] == 'D') {
                building.setImage(new Image("/Table/1+2+4.png"));

            } else {
                building.setImage(new Image("/Table/1+2.png"));
            }
        }
        else if (spaceToPrint[1] == '3') {
            if (spaceToPrint[2] == 'D') {
                building.setImage(new Image("/Table/1+2+3+4.png"));

            } else {
                building.setImage(new Image("/Table/1+2+3.png"));
            }
        }
        switch(spaceToPrint[0]){
            case 'A':
                worker.setImage(new Image("/Table/male3.png"));
                break;

            case 'B':
                worker.setImage(new Image("/Table/male5.png"));
                break;

            case 'C':
                worker.setImage(new Image("/Table/male1.png"));
                break;

            default:
                break;
        }

        if (guiHandler.getSerializableLiteGame().getCurrWorker()[0] == i && guiHandler.getSerializableLiteGame().getCurrWorker()[1] == j){
            currWorker.setImage( new Image("/Backgrounds/playermoveindicator_blue.png"));
        }

        building.setMouseTransparent(true);
        worker.setMouseTransparent(true);
        currWorker.setMouseTransparent(true);

        gameTable.add(building, j , i );
        gameTable.add(worker, j , i );
        gameTable.add(currWorker, j , i );

    }

    private String getLastAction() {
        return lastAction;
    }

    public void placeWorkers() {
        messageFromFrontEnd = "none";

        while (!messageFromFrontEnd.equals("Placing workers")) {
            messageFromFrontEnd = guiHandler.readString();
            setMessageLabel(messageFromFrontEnd);
            if (messageFromFrontEnd.contains("Wait")) {
                guiHandler.setSerializableLiteGame(guiHandler.readSerializableLG());
                muuSLG = guiHandler.getSerializableLiteGame();
                buildGameTable();
            }
        }

        boolean validPlacing = false;
        while (!validPlacing) {
            try {
                clientChoices.clear();
                guiHandler.getClientMessage().setSpace1((int[])clientChoices.take());
                guiHandler.getClientMessage().setSpace2((int[])clientChoices.take());
                guiHandler.getClientConnection().send(guiHandler.getClientMessage());
                newSLG = guiHandler.readSerializableLG();
                if (!newSLG.equalsSLG(guiHandler.getSerializableLiteGame())) {
                    validPlacing = true;
                    guiHandler.setSerializableLiteGame(newSLG);
                }
                buildGameTable();
                if (!validPlacing) setMessageLabel("Please retype two correct spaces!");
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setMessageLabel(String messageFromFrontEnd) {
        Platform.runLater( () -> {
            messageLabel.setText(messageFromFrontEnd);
        });
    }

    public void setNewSLT(SerializableLiteGame serializableLiteGame) {
        newSLG = serializableLiteGame;
    }
}

package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.util.Duration;

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

/**
 * It is the window where the match is actually played.
 */

public class TableWindow extends GameWindow implements Initializable {

    /**
     * It represent the game-table as a gridpane.
     */
    @FXML
    public GridPane gameTable;

    /**
     * It shows the message sent by the server for playing.
     */
    @FXML
    public Label messageLabel;

    /**
     * It shows player1's name.
     */
    @FXML
    public Label player1label;
    /**
     * It shows player2's name.
     */
    @FXML
    public Label player2label;
    /**
     * It shows player3's name.
     */
    @FXML
    public Label player3label;
    /**
     * It shows player1's worker image.
     */
    @FXML
    public ImageView worker1image;

    /**
     * It shows player2's worker image.
     */
    @FXML
    public ImageView worker2image;

    /**
     * It shows player3's worker image.
     */
    @FXML
    public ImageView worker3image;

    /**
     * It shows player1's god.
     */
    @FXML
    public Label god1label;

    /**
     * It shows player2's god.
     */
    @FXML
    public Label god2label;

    /**
     * It shows player3's god.
     */
    @FXML
    public Label god3label;

    /**
     * It shows the number of level1 blocks.
     */
    @FXML
    public Label level1label;

    /**
     * It shows the number of level2 blocks.
     */
    @FXML
    public Label level2label;

    /**
     * It shows the number of level3 blocks.
     */
    @FXML
    public Label level3label;

    /**
     * It shows the number of dome blocks.
     */
    @FXML
    public Label domeLabel;

    /**
     * It shows the basement of the podium for the winning player.
     * It is shown at the end of the game.
     */
    @FXML
    public ImageView podium;

    /**
     * It is the background of winningLabel.
     * It is shown at the end of the game.
     */
    @FXML
    public ImageView labelImage;

    /**
     * It is the background shown at the end of the game.
     */
    @FXML
    public ImageView endingImage;

    /**
     * It is the background of the winning player at the end of the game.
     */
    @FXML
    public ImageView goldenGlow;

    /**
     * It shows winner's god on screen. It is shown at the end of the game.
     */
    @FXML
    public ImageView winningPlayer;

    /**
     * It contains winner's nickname. It is shown at the end of the game.
     */
    @FXML
    public Label winningLabel;

    /**
     * It shows the background of the entire window.
     */
    @FXML
    public ImageView background;

    /**
     * It is shown if it's player2's turn.
     */
    @FXML
    public ImageView player2Turn;

    /**
     * It is shown if it's player3's turn.
     */
    @FXML
    public ImageView player3Turn;

    /**
     * It is shown if it's player1's turn.
     */
    @FXML
    public ImageView player1Turn;

    /**
     * It is the pane shown at the end of the game.
     */
    @FXML
    public Pane endingPane;
    /**
     * It is the pane of the current window.
     */
    @FXML
    public AnchorPane tablePane;

    /**
     * It is the SerializableLiteGame read by the GUIHandler.
     * Used to understand if the table has been modified correctly or not during placingWorker().
     */
    private SerializableLiteGame newSLG;

    /**
     * It establish if the game is finished or not.
     */
    private boolean endOfTheGame = false;

    /**
     * It saves the last action performed by the player.
     */
    private String lastAction = "none";

    /**
     * It contains the message sent by the server, to understand if the game is processing correctly.
     */
    private String messageFromFrontEnd = "none";

    /**
     * It contains the events requested by the player for playing.
     */
    private final BlockingQueue<Object> clientChoices = new LinkedBlockingDeque<>();

    /**
     * It saves on clientChoices the events requested by the player clicking with the mouse on a space.
     * It generates a graphical effect too.
     * @param actionEvent generated by clicking on spaces on the game table.
     */
    public void mouseClicking(ActionEvent actionEvent) {
        if ( !messageLabel.getText().contains("Wait") ) {
            Button clickedButton = (Button) actionEvent.getSource();
            String coordToSplit = clickedButton.getText();
            String[] coordToParse = coordToSplit.split("-");
            clientChoices.add(new int[]{Integer.parseInt(coordToParse[0]), Integer.parseInt(coordToParse[1])});


                PauseTransition showSelectedSpace = new PauseTransition(Duration.seconds(0.1));
                ImageView selectedSpace = new ImageView( new Image("/Backgrounds/playermoveindicator_red.png"));
                gameTable.add(selectedSpace, Integer.parseInt(coordToParse[1]), Integer.parseInt(coordToParse[0]));
                selectedSpace.setVisible(true);
                showSelectedSpace.setOnFinished( e -> {
                    selectedSpace.setVisible(false);
                });

                showSelectedSpace.play();

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread currThread;
        guiHandler.setCurrPane(tablePane);

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

        //guiHandler.setErrorImage(endingImage);

        String nameOfMyPlayer;
        if ( guiHandler.getClientMessage().getName().equals(guiHandler.getSerializableLiteGame().getName1() )) {
            nameOfMyPlayer = guiHandler.getClientMessage().getName();
            player1label.setText(nameOfMyPlayer);
            player2label.setText(guiHandler.getSerializableLiteGame().getName2());
            player1Turn.setImage(new Image("/Decorations/frame_yellow.png"));
            player2Turn.setImage(new Image("/Decorations/frame_white.png"));
        }
        else if ( guiHandler.getClientMessage().getName().equals(guiHandler.getSerializableLiteGame().getName2() )) {
            nameOfMyPlayer = guiHandler.getClientMessage().getName();
            player2label.setText(nameOfMyPlayer);
            player1label.setText(guiHandler.getSerializableLiteGame().getName1());
            player1Turn.setImage(new Image("/Decorations/frame_white.png"));
            player2Turn.setImage(new Image("/Decorations/frame_yellow.png"));
        }

        god1label.setText(guiHandler.getSerializableLiteGame().getGod1().toString());
        god2label.setText(guiHandler.getSerializableLiteGame().getGod2().toString());

        if (guiHandler.getNumOfPlayers() == 3){
            if ( guiHandler.getClientMessage().getName().equals(guiHandler.getSerializableLiteGame().getName3() )) {
                nameOfMyPlayer = guiHandler.getClientMessage().getName();
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
        }
        else{
            player3label.setVisible(false);
            god3label.setVisible(false);
            worker3image.setVisible(false);
        }

        player1Turn.setVisible(false);
        player2Turn.setVisible(true);
        player3Turn.setVisible(false);

        level1label.setText("level 1: " + guiHandler.getSerializableLiteGame().getLevel1());
        level2label.setText("level 2: " + guiHandler.getSerializableLiteGame().getLevel2());
        level3label.setText("level 3: " + guiHandler.getSerializableLiteGame().getLevel3());
        domeLabel.setText("dome: " + guiHandler.getSerializableLiteGame().getDome());

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
        Object choice = null;

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
                    String build = "Build";
                    String move = "Move";
                    if (!repeat) {
                        if (lastAction.equals("none") || lastAction.equals("End")) {
                            lastAction = "Choose Worker";
                            messageToPrint = "Select the worker you want to play with";
                        } else if (lastAction.equals("Choose Worker")) {
                            if (god == God.CHARON) {
                                charonSwitch++;
                                messageToPrint = "Please select a space";
                            }
                            else if (god == God.PROMETHEUS) {
                                setMessageLabel("Move or press B to build");
                                choice = clientChoices.take();
                                if (choice instanceof String ) {
                                    if ( "B".equals((String)choice) ) {
                                        lastAction = "Prometheus Build";
                                    }
                                    else{
                                        choice = null;
                                        lastAction = move;
                                    }
                                } else {
                                    lastAction = move;
                                }
                            } else {
                                lastAction = move;
                            }

                        } else if (lastAction.equals("Charon Switch") || lastAction.equals("Prometheus Build")) {
                            lastAction = move;
                        } else if (lastAction.equals(move)) {
                            if ( (god == God.ARTEMIS && moveCounter == 1) || (god == God.TRITON && guiHandler.getSerializableLiteGame().isPerimetralSpace(lastSpace))) {
                                setMessageLabel("Move again or press B to build");
                                choice = clientChoices.take();
                                if ( choice instanceof String ) {
                                    if ( choice.equals("B") ) {
                                        lastAction = build;
                                    }
                                    else {
                                        choice = null;
                                        lastAction = move;
                                    }
                                } else {
                                    lastAction = move;
                                }
                            }
                            else {
                                lastAction = build;
                            }
                        } else if (lastAction.equals(build)) {
                            if (  ( god == God.DEMETER && buildCounter == 1  )                   ||     //se Efesto o Demetra e ha già fatto una sola build
                                    (  god == God.HEPHAESTUS && buildCounter == 1 && guiHandler.getSerializableLiteGame().getHeight(lastSpace) < 3 ) ||
                                  (god == God.POSEIDON && buildCounter > 0 && buildCounter < 4                        &&     // se Poseidone e ha già fatto una o più costruzioni (max 3)
                                     !Arrays.equals(firstWorker, guiHandler.getSerializableLiteGame().getCurrWorker())) ) {  // sta giocando con il suo secondo worker

                                setMessageLabel("Build again or press E to end your turn");
                                choice = clientChoices.take();
                                if ( choice instanceof String ) {
                                    if ( choice.equals("E") ) {
                                        lastAction = "End";
                                        moveCounter = 0;
                                        buildCounter = 0;
                                        messageToPrint = "End of the turn";
                                    }
                                    else {
                                        choice = null;
                                        lastAction = build;
                                    }
                                } else {
                                    lastAction = build;
                                }
                            } else {
                                moveCounter = 0;
                                buildCounter = 0;
                                messageToPrint = "End of the turn";
                                lastAction = "End";
                            }
                        }
                    }
                    if (!lastAction.equals("End")) {

                        if ( lastAction.equals(move) && !repeat ){
                            messageToPrint = "Please select the space you want to occupy";
                            moveCounter++;
                        }
                        else if ( lastAction.contains(build) && !repeat ){
                            messageToPrint = "Please select the space where you want to build";
                            buildCounter++;
                        }

                        if ( repeat ){
                            if(lastAction.equals(move) ) messageToPrint = "Please select a valid space you want to occupy";
                            else if ( lastAction.equals(build) ) messageToPrint = "Please select a valid space where you want to build";
                        }

                        setMessageLabel(messageToPrint);
                        clientChoices.clear();
                        Object selection;
                        if(  choice != null && choice instanceof int[] ) lastSpace = (int[]) choice;
                        else {
                            selection = clientChoices.take();
                            while ( !  (selection instanceof int[]) ){
                                selection = clientChoices.take();
                            }
                            lastSpace = (int[]) selection;
                        }
                        choice = null;
                        guiHandler.getClientMessage().setSpace1(lastSpace);

                        if ( god == God.CHARON && charonSwitch == 1 ) {
                            String space = guiHandler.getSerializableLiteGame().getStringValue(lastSpace[0], lastSpace[1]);
                            if (!space.contains("V")) {
                                lastAction = "Charon Switch";
                            } else {
                                lastAction = move;
                            }
                        }
                        if ( lastAction.contains(build) ) {
                            String space = guiHandler.getSerializableLiteGame().getStringValue(lastSpace[0], lastSpace[1]);
                            if ( god == God.ATLAS && !space.contains("3") ) {
                                setMessageLabel("Press D to build a dome, else press B");
                                if ("D".equals(clientChoices.take())) guiHandler.getClientMessage().setLevelToBuild(4);
                                else guiHandler.getClientMessage().setLevelToBuild(guiHandler.getSerializableLiteGame().getHeight(guiHandler.getClientMessage().getSpace1()) + 1);
                            } else guiHandler.getClientMessage().setLevelToBuild(guiHandler.getSerializableLiteGame().getHeight(guiHandler.getClientMessage().getSpace1()) + 1);

                            if ( buildCounter == 1 && god == God.POSEIDON ) {
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
                    }
                    else if ( messageFromFrontEnd.contains("You won")){
                        endOfTheGame = true;
                    }
                    else{
                        charonSwitch = 0;
                        repeat = false;
                    }
                }
            }
            catch ( InterruptedException e) {
                e.printStackTrace();
            }
        }
        setMessageLabel(messageFromFrontEnd);
        Platform.runLater( () ->
        {
            PauseTransition hidePodium = new PauseTransition(Duration.seconds(10));
            background.setOpacity(0.5);
            endingPane.setVisible(true);
            for ( Node imageView: gameTable.getChildren()){
                if( imageView instanceof ImageView) imageView.setOpacity(0.5);
            }
            showWinner();

            hidePodium.setOnFinished(e -> {
                endingPane.setVisible(false);
                if (messageFromFrontEnd.contains("You won")) {
                    endingImage.setVisible(true);
                    endingImage.setImage(new Image("Backgrounds/winningWindow.PNG"));
                }
                else {
                    endingImage.setVisible(true);
                    endingImage.setImage(new Image("Backgrounds/losingWindow.PNG"));
                }
            });
            hidePodium.play();
        });
    }

    /**
     * It sets endingPane visible.
     */
    private void showWinner() {
        String winner = guiHandler.getSerializableLiteGame().getCurrPlayer();
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

    }

    /**
     * It designs the general view of the game-table, modified with blocks' image and workers' image.
     */
    private void buildGameTable() {
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

    /**
     * It design the general view of a single space of the game-table.
     * @param row represents the row of the gridpane.
     * @param col represents the column of the gridpane.
     * @throws FileNotFoundException
     */
    private void buildGameSpace(int row, int col) throws FileNotFoundException {
        char[] spaceToPrint;
        ImageView building = new ImageView(), worker = new ImageView(), currWorker = new ImageView();

        spaceToPrint = guiHandler.getSerializableLiteGame().getTable()[row][col].toCharArray();


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
                building.setImage(new Image("/Table/1+2+3.png"));
        }
        else if ( spaceToPrint[1] == '4' ) {
            building.setImage(new Image("/Table/1+2+3+4.png"));
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

        if ( guiHandler.getSerializableLiteGame().getCurrWorker() != null ) {
            if (guiHandler.getSerializableLiteGame().getCurrWorker()[0] == row && guiHandler.getSerializableLiteGame().getCurrWorker()[1] == col) {
                currWorker.setImage(new Image("/Backgrounds/playermoveindicator_blue.png"));
            }
        }

        building.setMouseTransparent(true);
        worker.setMouseTransparent(true);
        currWorker.setMouseTransparent(true);

        gameTable.add(building, col , row );
        gameTable.add(worker, col , row );
        gameTable.add(currWorker, col , row );

    }

    /**
     * It is called at the beginning of the game. It let the player choose where to place his/her workers.
     */
    public void placeWorkers() {
        messageFromFrontEnd = "none";

        while (!messageFromFrontEnd.equals("Placing workers")) {
            messageFromFrontEnd = guiHandler.readString();
            setMessageLabel(messageFromFrontEnd);
            if (messageFromFrontEnd.contains("Wait")) {
                guiHandler.setSerializableLiteGame(guiHandler.readSerializableLG());
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
                if (!validPlacing) {
                    setMessageLabel("Please retype two correct spaces!");
                    guiHandler.readString();
                }
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

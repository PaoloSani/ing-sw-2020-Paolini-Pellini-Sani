package it.polimi.ingsw.GUI;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.client.SettingGameMessage;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.util.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * It is the interface of GUI towards and backwards network.
 * It is responsible of every message which is sent on the net and received from it.
 */

public class GUIHandler implements Observer<MessageHandler> {

    /**
     * It represents the client's connection to the net. It let the GUI send messages on the internet
     */
    private ClientConnection clientConnection;

    /**
     * It is the message which is going to be sent on the internet, to communicate client's decisions.
     */
    private ClientMessage clientMessage;

    /**
     * It handles all the messages which come from the internet.
     */
    private MessageHandler messageHandler;

    /**
     * If true, it lets the method readString() terminate.
     */
    private boolean updateString = false;

    /**
     * If true, it lets the method readSerializableLG() terminate.
     */
    private boolean updateLG = false;

    /**
     * It is the nickname chosen by the client to play the game.
     */
    private String nickname;

    /**
     * It is a message sent by the server, which communicates the current state of the game process.
     */
    private String messageFromServer;

    /**
     * It is the number of players playing the same match of the client. Client is counted too.
     */
    private int numOfPlayers;

    /**
     * It is the message sent by the client to start a game.
     */
    private SettingGameMessage settingGameMessage;

    /**
     * It is the gameID associated to client's match .
     */
    private int gameID;

    /**
     * It is the mode chosen by the client to play the game.
     */
    private Mode mode;

    /**
     * It is the list of gods chosen by the challenger.
     * If the client is the challenger, this list will be filled by the client itself and sent with the method sendChallengerMessage().
     * Else, it will be sent on the internet by the challenger and read through the method readString() by the client's GUI.
     */
    private List<God> gods = new ArrayList<>();

    /**
     * It contains useful information about the match, as players' names, players' gods and game-table.
     * It is not modifiable.
     */
    private SerializableLiteGame serializableLiteGame;



    //Inutili!!
   /*private ImageView errorImage;*/
    private AnchorPane currPane;


    public GUIHandler(){
        this.messageHandler = new MessageHandler(this);
        try {
            String filename = "/server-settings.txt";
            BufferedReader settings = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
            String ip = settings.readLine();
            ip = ip.replaceAll("\\s+","");
            ip = ip.split(":")[1];
            String port = settings.readLine();
            port = port.replaceAll("\\s+","");
            port = port.split(":")[1];
            this.clientConnection = new ClientConnection(ip,Integer.parseInt(port), this.messageHandler);
            this.clientMessage = new ClientMessage();
            settingGameMessage = new SettingGameMessage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * It opens a new stage, loading a new scene based on the FXML file "fileToLoad". It closes the previous stage.
     * @param node An element of the previous stage. It is needed to close that one.
     * @param nextStage It is the stage to set.
     * @param fileToLoad FXML file to load on the scene.
     */
    public synchronized void loadFXMLFile(Node node, Stage nextStage, String fileToLoad){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fileToLoad));
        try {
            Parent root = fxmlLoader.load();
            nextStage.setScene(new Scene(root));
            nextStage.setTitle("SANTORINI");
            nextStage.setResizable(false);
            nextStage.setOnCloseRequest(event -> System.exit(0));
            Stage toClose = (Stage) node.getScene().getWindow();
            toClose.close();
            nextStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It establishes the connection with the server.
     * @return true if the server is active, false otherwise.
     */
    public boolean setClientConnection(){
        new Thread (clientConnection::run).start();
        messageFromServer = readString();
        if (messageFromServer.equals("Welcome, server ready!\n")) return true;
        else return false;
    }

    /**
     * It asks the server if the chosen nickname is available.
     * @param nickname It is the nickname chosen by the client.
     * @return true if the nickname is available, false otherwise.
     */
    public boolean askNameAvailability(String nickname){
        if (nickname.equals("")) return false;
        clientConnection.send(nickname);
        messageFromServer = readString();
        if (messageFromServer.equals("Nickname accepted")){
            this.nickname = nickname;
            return true;
        }
        else return false;
    }

    /**
     * It reads a String message sent by the server.
     * If there are no messages, it waits until a message comes.
     * @return the read message.
     */
    public synchronized String readString(){
        while ( !updateString ){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String result = messageHandler.getString();
        updateString = false;
        messageHandler.setStringRead(false);
        return result;
    }

    /**
     * It reads a SerializableLiteGame message sent by the server.
     * If there are no messages, it waits until a message comes.
     * @return the read message.
     */
    public synchronized SerializableLiteGame readSerializableLG() {
        while ( !updateLG ){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updateLG = false;
        SerializableLiteGame result = messageHandler.getLiteGameFromServer();
        messageHandler.setLGRead(false);
        return result;
    }

    /**
     * It sets updateString or updateLG and notifies all sleeping threads.
     * @param message: message received from the observable instance
     */
    @Override
    public synchronized void update(MessageHandler message) {
        if ( message.isStringRead() ){
            updateString = true;
        }
        if ( message.isLGRead() ) updateLG = true;
        messageHandler = message;
        notifyAll();
    }

    public String getNickname(){
        return this.nickname;
    }

    public void setNumOfPlayers(int numToSet) {
        this.numOfPlayers = numToSet;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setGameID(int gameID){
        this.gameID = gameID;
    }

    public int getGameID(){
        return gameID;
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMessageFromServer(String message){
        this.messageFromServer = message;
    }

    public String getMessageFromServer(){
        return messageFromServer;
    }

    /**
     * It sets settingGameMessage if the chosen mode is NEW_GAME.
     */
    public void createNewGame() {
        settingGameMessage.setNumberOfPlayer(numOfPlayers);
        settingGameMessage.setCreatingNewGame(true);
        settingGameMessage.setPlayingExistingMatch(false);
        clientConnection.send(settingGameMessage);
    }

    /**
     * It sets settingGameMessage if the chosen mode is EXISTING_MATCH.
     */
    public void playExistingMatch() {
        settingGameMessage.setCreatingNewGame(false);
        settingGameMessage.setPlayingExistingMatch(true);
        settingGameMessage.setGameID(gameID);
        clientConnection.send(settingGameMessage);
    }

    /**
     * It sets settingGameMessage if the chosen mode is RANDOM_GAME.
     */
    public void randomMatch() {
        settingGameMessage.setPlayingExistingMatch(false);
        settingGameMessage.setCreatingNewGame(false);
        settingGameMessage.setNumberOfPlayer(numOfPlayers);
        clientConnection.send(settingGameMessage);
    }

    /**
     * It fills the attribute gods and modifies labels on screen, containing chosen gods' name.
     * @param currGod The god to be added to the list.
     * @param label1  First label on screen.
     * @param label2  Second label on screen.
     * @param label3  Third label on screen.
     */
    public void addGod(God currGod, Label label1, Label label2, Label label3) {
        if (gods.contains(currGod)) {
            gods.remove(currGod);
        }
        else if (numOfPlayers > gods.size()) {
            gods.add(currGod);
        }
        if (gods.size() == 0) label1.setVisible(false);

        else if (gods.size() == 1) {
            label1.setText(gods.get(0).toString());
            label1.setVisible(true);
            label2.setVisible(false);
        }
        else if(gods.size() == 2) {
            label1.setText(gods.get(0).toString());
            label2.setText(gods.get(1).toString());
            label2.setVisible(true);
            label3.setVisible(false);
        }
        else if (gods.size() == 3) {
            label1.setText(gods.get(0).toString());
            label2.setText(gods.get(1).toString());
            label3.setText(gods.get(2).toString());
            label3.setVisible(true);
        }

    }

    public List<God> getGods(){
        return gods;
    }

    /**
     * It sends the challengerMessage to the server.
     */
    public void sendChallengerMessage() {
        String[] challengerMessage;
        if (numOfPlayers == 2) {
            challengerMessage =
                    new String[]{gods.get(0).toString(), gods.get(1).toString()};
        } else {
            challengerMessage =
                    new String[]{gods.get(0).toString(), gods.get(1).toString(), gods.get(2).toString()};
        }
        clientConnection.send(challengerMessage);
    }

    /**
     * It parses the message containing all the gods chosen by the challenger.
     * @param message It contains the gods chosen by the challenger.
     */
    public void setMessageFromFrontend(String message) {
        String stringGods = message.replace(", ","-");
        stringGods = stringGods.replace("[","");
        stringGods = stringGods.replace("]","");
        String[] godArray = stringGods.split("-");
        for (int i = 0; i < godArray.length; i++){
            gods.add(God.valueOf(godArray[i]));
        }
    }

    /**
     * It sets the clientMessage to be sent to the server. If the client isn't the challenger, it sends this message too.
     * @param currGod It is the god chosen by the client.
     */
    public void setClientMessage(God currGod) {
        clientMessage.setName(nickname);
        clientMessage.setGod(currGod);
        if ( mode != Mode.NEW_GAME ) {
            clientConnection.send(clientMessage);
        }
    }

    public void setSerializableLiteGame(SerializableLiteGame serializableLiteGame) {
        this.serializableLiteGame = serializableLiteGame;
    }

    public ClientMessage getClientMessage() {
        return clientMessage;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public SerializableLiteGame getSerializableLiteGame() {
        return serializableLiteGame;
    }


    public AnchorPane getCurrPane() {
        return currPane;
    }

    public void setCurrPane(AnchorPane nextPane) {
        this.currPane = nextPane;
    }
}

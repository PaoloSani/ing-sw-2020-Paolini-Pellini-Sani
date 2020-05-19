package it.polimi.ingsw.GUI;

import it.polimi.ingsw.CLI.ColourFont;
import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.client.SettingGameMessage;
import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.util.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIHandler implements Observer<MessageHandler> {

    private final ClientConnection clientConnection;
    private MessageHandler messageHandler;
    private boolean updateString = false;
    private boolean updateLG = false;
    private String nickname;
    private String messageFromServer;
    private int numOfPlayers;
    private SettingGameMessage settingGameMessage;
    private int gameID;
    private Mode mode;


    public GUIHandler(){
        this.messageHandler = new MessageHandler(this);
        this.clientConnection = new ClientConnection("127.0.0.1",4702, this.messageHandler);
        settingGameMessage = new SettingGameMessage();
    }

    public void loadFXMLFile(Button nextButton, Stage nextStage, String fileToLoad){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fileToLoad));
        try {
            Parent root = fxmlLoader.load();
            nextStage.setScene(new Scene(root));
            nextStage.setTitle("SANTORINI");
            nextStage.setResizable(false);
            nextStage.setOnCloseRequest(event -> System.exit(0));
            Stage toClose = (Stage) nextButton.getScene().getWindow();
            toClose.close();
            nextStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean setClientConnection(){
        new Thread (clientConnection::run).start();
        messageFromServer = readString();
        if (messageFromServer.equals("Welcome, server ready!\n")) return true;
        else return false;
    }

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

    public void setGameID(int gameID){
        this.gameID = gameID;
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public Mode getMode() {
        return this.mode;
    }


    public void createNewGame() {
        settingGameMessage.setNumberOfPlayer(numOfPlayers);
        settingGameMessage.setCreatingNewGame(true);
        settingGameMessage.setPlayingExistingMatch(false);
        clientConnection.send(settingGameMessage);
    }

    public void playExistingMatch() {
        settingGameMessage.setCreatingNewGame(false);
        settingGameMessage.setPlayingExistingMatch(true);
        settingGameMessage.setGameID(gameID);
        clientConnection.send(settingGameMessage);
    }

    public void randomMatch() {
        settingGameMessage.setPlayingExistingMatch(false);
        settingGameMessage.setCreatingNewGame(false);
        settingGameMessage.setNumberOfPlayer(numOfPlayers);
        clientConnection.send(settingGameMessage);
    }
}

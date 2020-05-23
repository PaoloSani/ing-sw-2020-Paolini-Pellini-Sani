package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModeWindow extends GameWindow  {

    @FXML
    public Button newGameButton;
    @FXML
    public Button friendsButton;
    @FXML
    public Button randomGameButton;

    private Stage nextStage = new Stage();

    public void newGame(ActionEvent actionEvent) {
        System.out.println(guiHandler.getNickname());
        guiHandler.setMode(Mode.NEW_GAME);
        guiHandler.loadFXMLFile(newGameButton,nextStage,"/GUIScenes/newGameWindow.fxml");

    }

    public void playWithFriends(ActionEvent actionEvent) {
        System.out.println(guiHandler.getNickname());
        guiHandler.setMode(Mode.EXISTING_MATCH);
        guiHandler.loadFXMLFile(newGameButton,nextStage,"/GUIScenes/existingGameWindow.fxml");
    }

    public void randomGame(ActionEvent actionEvent) {
        System.out.println(guiHandler.getNickname());
        guiHandler.setMode(Mode.RANDOM_MATCH);
        guiHandler.loadFXMLFile(newGameButton,nextStage,"/GUIScenes/newGameWindow.fxml");
    }
}

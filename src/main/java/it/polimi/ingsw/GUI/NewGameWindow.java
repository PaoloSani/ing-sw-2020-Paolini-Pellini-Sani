package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class NewGameWindow extends GameWindow {
    public RadioButton twoPlayersButton;
    public RadioButton threePlayersButton;
    public Button nextButton;
    private Stage nextStage = new Stage();


    public void uncheckThreePlayersButton(ActionEvent actionEvent) {
        threePlayersButton.setSelected(false);
        guiHandler.setNumOfPlayers(2);
    }

    public void uncheckTwoPlayersButton(ActionEvent actionEvent) {
        twoPlayersButton.setSelected(false);
        guiHandler.setNumOfPlayers(3);
    }

    public void goFurther(ActionEvent actionEvent) {

        if (twoPlayersButton.isSelected() || threePlayersButton.isSelected()){
            if (guiHandler.getMode() == Mode.NEW_GAME) guiHandler.createNewGame();
            else if (guiHandler.getMode() == Mode.RANDOM_MATCH) guiHandler.randomMatch();
            guiHandler.loadFXMLFile(nextButton,nextStage,"/GUIScenes/waitingWindow.fxml");
            System.out.println(guiHandler.readString());
        }

    }
}

package it.polimi.ingsw.GUI;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class NewGameWindow extends GameWindow {
    public RadioButton twoPlayersButton;
    public RadioButton threePlayersButton;
    public Button nextButton;
    public Label waitLabel;
    public Label IDLabel;

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
            if (guiHandler.getMode() == Mode.NEW_GAME) {
                guiHandler.createNewGame();
                twoPlayersButton.setVisible(false);
                threePlayersButton.setVisible(false);
                nextButton.setVisible(false);
                waitLabel.setText("WAITING FOR OTHER PLAYERS");
                waitLabel.setVisible(true);
                IDLabel.setText("THE GAME ID IS: " + guiHandler.readString());
                IDLabel.setVisible(true);
            }
            else if (guiHandler.getMode() == Mode.RANDOM_MATCH) {
                guiHandler.randomMatch();
                twoPlayersButton.setVisible(false);
                threePlayersButton.setVisible(false);
                nextButton.setVisible(false);
                waitLabel.setText("WAITING FOR OTHER PLAYERS");
                waitLabel.setVisible(true);
                String messageFromServer = guiHandler.readString();
                System.out.println("  Server says: " + messageFromServer + "\n");
                if ( messageFromServer.contains("You are")) {
                    guiHandler.setMode(Mode.NEW_GAME);
                    IDLabel.setText("YOU ARE THE CHALLENGER");
                    IDLabel.setVisible(true);
                }
            }
                //TODO:impostare questo metodo con dei thread! (O loading)
                String message = guiHandler.readString();
                if(message.contains("Game has started")) {
                    if (guiHandler.getMode() == Mode.NEW_GAME){
                        guiHandler.loadFXMLFile(nextButton, nextStage, "/GUIScenes/challengerWindow.fxml");
                    }
                    else{
                        guiHandler.loadFXMLFile(nextButton,nextStage,"/GUIScenes/waitingWindow.fxml");
                    }
                }
        }
    }
}

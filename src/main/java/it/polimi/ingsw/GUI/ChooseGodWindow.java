package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseGodWindow extends GameWindow implements Initializable {
    public boolean isVisible = true;
    public Button backGodButton;
    public Button nextGodButton;
    public Label godLabel;
    public Label powerLabel;
    public Button chooseButton;
    public ImageView godCard;
    public ImageView chooseButtonImage;
    public ImageView backButtonImage;
    public ImageView nextButtonImage;
    private Stage nextStage = new Stage();
    private God currGod = guiHandler.getGods().get(0);

    public void backGod(ActionEvent actionEvent) {
        if (currGod == guiHandler.getGods().get(0)){
            if (guiHandler.getNumOfPlayers() == 3 && guiHandler.getGods().size() == 3) currGod = guiHandler.getGods().get(2);
            else currGod = guiHandler.getGods().get(1);
        } else if (currGod == guiHandler.getGods().get(1)){
            currGod = guiHandler.getGods().get(0);
        } else if (currGod == guiHandler.getGods().get(2)){
            currGod = guiHandler.getGods().get(1);
        }
        setGodImage();
    }

    public void nextGod(ActionEvent actionEvent) {
        if (currGod == guiHandler.getGods().get(0)){
            currGod = guiHandler.getGods().get(1);
        } else if (currGod == guiHandler.getGods().get(1)){
            if (guiHandler.getNumOfPlayers() == 3 && guiHandler.getGods().size() == 3) currGod = guiHandler.getGods().get(2);
            else currGod = guiHandler.getGods().get(0);
        } else if (currGod == guiHandler.getGods().get(2)){
            currGod = guiHandler.getGods().get(0);
        }
        setGodImage();
    }

    private void setGodImage() {
        switch (currGod){
            case APOLLO:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0043_god_and_hero_cards_0013_apollo.png"));
                break;
            }
            case ARTEMIS:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0054_god_and_hero_cards_0002_Artemis.png"));
                break;
            }
            //
            case ATHENA:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0052_god_and_hero_cards_0004_Athena.png"));
                break;
            }
            case ATLAS:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0053_god_and_hero_cards_0003_Atlas.png"));
                break;
            }
            //
            case CHARON:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0020_god_and_hero_cards_0036_charon.png"));
                break;
            }
            //
            case DEMETER:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0050_god_and_hero_cards_0006_Demeter.png"));
                break;
            }
            case HEPHAESTUS:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0009_god_and_hero_cards_0047_Hephaestus.png"));
                break;
            }
            //
            case HYPNUS:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0019_god_and_hero_cards_0037_Hypnus.png"));
                break;
            }
            case MINOTAUR:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0008_god_and_hero_cards_0048_Minotaur.png"));
                break;
            }
            case MORTAL:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0000_god_and_hero_cards_0057_HumanGord.png"));
                break;
            }
            case PAN:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0046_god_and_hero_cards_0010_Pan.png"));
                break;
            }
            case POSEIDON:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0045_god_and_hero_cards_0011_Poseidon.png"));
                break;
            }
            case PROMETHEUS:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
                break;
            }
            case TRITON:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0028_god_and_hero_cards_0028_triton.png"));
                break;
            }
            case ZEUS:{
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0014_god_and_hero_cards_0042_zeus.png"));
                break;
            }
        }
        godLabel.setText(currGod.toString());
        powerLabel.setText(currGod.getPower());
    }

    public void chooseGod(ActionEvent actionEvent) {
        guiHandler.setClientMessage(currGod);
        guiHandler.loadFXMLFile(chooseButton,nextStage,"/GUIScenes/beginningMatchWindow.fxml");
    }

    public void clickBack(MouseEvent mouseEvent) {
        backButtonImage.setImage(new Image("Buttons/btn_back_pressed.png"));
    }

    public void releaseBack(MouseEvent mouseEvent) {
        backButtonImage.setImage(new Image("Buttons/btn_back.png"));
    }

    public void clickNext(MouseEvent mouseEvent) {
        nextButtonImage.setImage(new Image("Buttons/btn_back_pressed.png"));
    }

    public void releaseNext(MouseEvent mouseEvent) {
        nextButtonImage.setImage(new Image("Buttons/btn_back.png"));
    }

    public void clickChoose(MouseEvent mouseEvent) {
        chooseButtonImage.setImage(new Image("Buttons/cm_btn_blue_pressed.png"));
    }

    public void releaseChoose(MouseEvent mouseEvent) {
        chooseButtonImage.setImage(new Image("Buttons/cm_btn_blue.png"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (guiHandler.getMode() == Mode.EXISTING_MATCH && guiHandler.getSerializableLiteGame().getName3() == null) guiHandler.setNumOfPlayers(2);
        else if (guiHandler.getMode() == Mode.EXISTING_MATCH && guiHandler.getSerializableLiteGame().getName3() != null) guiHandler.setNumOfPlayers(3);
        currGod = guiHandler.getGods().get(0);
        setGodImage();
    }
}

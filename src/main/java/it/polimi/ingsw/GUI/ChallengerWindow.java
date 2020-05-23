package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class ChallengerWindow extends GameWindow {
    public boolean isVisible = true;
    public Button backGodButton;
    public Button nextGodButton;
    public Label godLabel;
    public Label powerLabel;
    public Label playLabel;
    public Button chooseButton;
    public Button nextButton;
    public ImageView godCard;
    public ImageView chooseButtonImage;
    public ImageView playImage;
    public ImageView backButtonImage;
    public ImageView nextButtonImage;
    public Label firstGodLabel;
    public Label secondGodLabel;
    public Label thirdGodLabel;
    private God currGod = God.APOLLO;

    public void backGod(ActionEvent actionEvent) {
        switch (currGod){
            case APOLLO:{
                currGod = God.PROMETHEUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
                break;
            }
            case PROMETHEUS:{
                currGod = God.MINOTAUR;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0008_god_and_hero_cards_0048_Minotaur.png"));
                break;
            }
            case MINOTAUR:{
                currGod = God.APOLLO;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0043_god_and_hero_cards_0013_apollo.png"));
                break;
            }
        }
        godLabel.setText(currGod.toString());
        powerLabel.setText(currGod.getPower());
    }

    public void nextGod(ActionEvent actionEvent) {
        switch (currGod){
            case APOLLO:{
                currGod = God.MINOTAUR;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0008_god_and_hero_cards_0048_Minotaur.png"));
                break;
            }
            case MINOTAUR:{
                currGod = God.PROMETHEUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
                break;
            }
            case PROMETHEUS:{
                currGod = God.APOLLO;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0043_god_and_hero_cards_0013_apollo.png"));
                break;
            }
        }
        godLabel.setText(currGod.toString());
        powerLabel.setText(currGod.getPower());
    }

    public void chooseGod(ActionEvent actionEvent) {
        guiHandler.addGod(currGod);
        List<God> gods = guiHandler.getGods();
        int size = gods.size();
        switch (size){
            case 0:
                firstGodLabel.setVisible(false);
                break;
            case 1:
                firstGodLabel.setText(currGod.toString());
                firstGodLabel.setVisible(true);
                secondGodLabel.setVisible(false);
                break;
            case 2:
                secondGodLabel.setText(currGod.toString());
                secondGodLabel.setVisible(true);
                thirdGodLabel.setVisible(false);
                break;
            case 3:
                thirdGodLabel.setText(currGod.toString());
                thirdGodLabel.setVisible(true);
                break;
        }
        isVisible = (guiHandler.getNumOfPlayers() == size);
        if (isVisible){
            playImage.setVisible(true);
            playLabel.setVisible(true);
            nextButton.setVisible(true);
        }
    }

    public void goFurther(ActionEvent actionEvent) {
        System.out.println("OK");
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

    public void clickPlay(MouseEvent mouseEvent) {
        playImage.setImage(new Image("Buttons/cm_btn_blue_pressed.png"));
    }

    public void releaseChoose(MouseEvent mouseEvent) {
        chooseButtonImage.setImage(new Image("Buttons/cm_btn_blue.png"));
    }

    public void releasePlay(MouseEvent mouseEvent) {
        playImage.setImage(new Image("Buttons/cm_btn_blue.png"));
    }
}

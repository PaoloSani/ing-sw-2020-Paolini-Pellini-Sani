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
                currGod = God.ZEUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0014_god_and_hero_cards_0042_zeus.png"));
                break;
            }
            case ARTEMIS:{
                currGod = God.APOLLO;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0043_god_and_hero_cards_0013_apollo.png"));
                break;
            }
            //
            case ATHENA:{
                currGod = God.ARTEMIS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
                break;
            }
            case ATLAS:{
                currGod = God.ATHENA;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0052_god_and_hero_cards_0004_Athena.png"));
                break;
            }
            //
            case CHARON:{
                currGod = God.ATLAS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
                break;
            }
            //
            case DEMETER:{
                currGod = God.CHARON;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0050_god_and_hero_cards_0006_Demeter.png"));
                break;
            }
            case HEPHAESTUS:{
                currGod = God.DEMETER;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0050_god_and_hero_cards_0006_Demeter.png"));
                break;
            }
            //
            case HYPNUS:{
                currGod = God.HEPHAESTUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
                break;
            }
            case MINOTAUR:{
                currGod = God.HYPNUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0019_god_and_hero_cards_0037_Hypnus.png"));
                break;
            }
            case MORTAL:{
                currGod = God.MINOTAUR;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0008_god_and_hero_cards_0048_Minotaur.png"));
                break;
            }
            case PAN:{
                currGod = God.MORTAL;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0000_god_and_hero_cards_0057_HumanGord.png"));
                break;
            }
            case POSEIDON:{
                currGod = God.PAN;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0046_god_and_hero_cards_0010_Pan.png"));
                break;
            }
            case PROMETHEUS:{
                currGod = God.POSEIDON;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0045_god_and_hero_cards_0011_Poseidon.png"));
                break;
            }
            case TRITON:{
                currGod = God.PROMETHEUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
                break;
            }
            case ZEUS:{
                currGod = God.TRITON;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0028_god_and_hero_cards_0028_triton.png"));
                break;
            }
        }
        godLabel.setText(currGod.toString());
        powerLabel.setText(currGod.getPower());
    }

    public void nextGod(ActionEvent actionEvent) {
        switch (currGod) {
            case APOLLO: {
                currGod = God.ARTEMIS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0014_god_and_hero_cards_0042_zeus.png"));
                break;
            }
            case ARTEMIS: {
                currGod = God.ATHENA;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0052_god_and_hero_cards_0004_Athena.png"));
                break;
            }
            //
            case ATHENA: {
                currGod = God.ATLAS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
                break;
            }
            case ATLAS: {
                currGod = God.CHARON;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0052_god_and_hero_cards_0004_Athena.png"));
                break;
            }
            //
            case CHARON: {
                currGod = God.DEMETER;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0050_god_and_hero_cards_0006_Demeter.png"));
                break;
            }
            //
            case DEMETER: {
                currGod = God.HEPHAESTUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0050_god_and_hero_cards_0006_Demeter.png"));
                break;
            }
            case HEPHAESTUS: {
                currGod = God.HYPNUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0019_god_and_hero_cards_0037_Hypnus.png"));
                break;
            }
            //
            case HYPNUS: {
                currGod = God.MINOTAUR;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0008_god_and_hero_cards_0048_Minotaur.png"));
                break;
            }

            case MINOTAUR: {
                currGod = God.MORTAL;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0000_god_and_hero_cards_0057_HumanGord.png"));
                break;
            }
            case MORTAL: {
                currGod = God.PAN;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0046_god_and_hero_cards_0010_Pan.png"));
                break;
            }
            case PAN: {
                currGod = God.POSEIDON;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0045_god_and_hero_cards_0011_Poseidon.png"));
                break;
            }
            case POSEIDON: {
                currGod = God.PROMETHEUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0004_god_and_hero_cards_0052_Prometheus.png"));
                break;
            }
            case PROMETHEUS: {
                currGod = God.TRITON;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0028_god_and_hero_cards_0028_triton.png"));
                break;
            }
            case TRITON: {
                currGod = God.ZEUS;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0014_god_and_hero_cards_0042_zeus.png"));
                break;
            }
            case ZEUS: {
                currGod = God.APOLLO;
                godCard.setImage(new Image("FullGodAvatar/full_0000s_0043_god_and_hero_cards_0013_apollo.png"));
                break;
            }
        }
        godLabel.setText(currGod.toString());
        powerLabel.setText(currGod.getPower());
        }

    public void chooseGod(ActionEvent actionEvent) {
        guiHandler.addGod(currGod,firstGodLabel,secondGodLabel,thirdGodLabel);
        List<God> gods = guiHandler.getGods();
        isVisible = (guiHandler.getNumOfPlayers() == guiHandler.getGods().size());
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

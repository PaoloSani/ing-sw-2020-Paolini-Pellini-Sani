package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.God;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChallengerWindow extends GameWindow {
    public God[] chosenGods;
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
    }

    public void goFurther(ActionEvent actionEvent) {
    }
}

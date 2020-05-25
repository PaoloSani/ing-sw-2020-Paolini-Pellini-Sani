package it.polimi.ingsw.GUI;

import javafx.scene.control.Button;

public class tableSpace {

    Button button;
    int row;
    int col;

    tableSpace(int row, int col){
        button = new Button();
        button.setOpacity(0.0);
        button.setLayoutX(82);
        button.setLayoutY(80);
        this.row = row;
        this.col = col;
    }
}

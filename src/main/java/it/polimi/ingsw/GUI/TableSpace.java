package it.polimi.ingsw.GUI;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;

public class TableSpace {

    Button button;
    int row;
    int col;

    TableSpace(int row, int col){
        button = new Button();
        button.setOpacity(1.0);
        button.setText(row+"-"+col);
        button.setPrefSize(82,80);
        this.row = row;
        this.col = col;
    }

    public Button getButton() {
        return button;
    }
}

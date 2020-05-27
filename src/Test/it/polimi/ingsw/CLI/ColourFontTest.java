package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.God;
import org.junit.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ColourFontTest {

    @Test
    public void printGodColour(){
        for (God g : God.values()){
            System.out.println(ColourFont.getGodColour(g)+g);
        }
    }
}
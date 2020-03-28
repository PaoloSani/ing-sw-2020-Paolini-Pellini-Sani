package it.polimi.ingsw.Test;

import it.polimi.ingsw.model.Space;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaceTest {
    private Space space = new Space(4,4);

    @Test

    public void heightTest() {
        assertEquals(0, space.getHeight());
    }


}
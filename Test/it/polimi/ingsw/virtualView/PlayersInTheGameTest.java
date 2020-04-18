package it.polimi.ingsw.virtualView;


import it.polimi.ingsw.controller.BackEnd;
import it.polimi.ingsw.model.God;


import org.junit.Test;


import static org.junit.jupiter.api.Assertions.*;

public class PlayersInTheGameTest {
    BackEnd backEnd = new BackEnd();
    PlayersInTheGame test = new PlayersInTheGame(backEnd);

    @Test
    public void observerTest() {
        test.setGod1(God.APOLLO);
        test.setGod2(God.ATHENA);
        test.setGod3(God.MORTAl);
        test.setName1("Paolo");
        test.setName2("Giuseppe");
        test.setName3("Riccardo");

        //al termine del set di tutti e tre i nomi il metodo setName3 chiama checkNotifyThis che a sua volta invoca la notify agli osservatori
        //TODO: gestire il caso in cui ci sono solo due giocatori
        assertSame(backEnd.getCurrState(), backEnd.placingWorkers);
    }


}
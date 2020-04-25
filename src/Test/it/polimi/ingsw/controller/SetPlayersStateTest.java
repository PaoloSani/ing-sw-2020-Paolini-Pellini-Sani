package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class SetPlayersStateTest {
    private BackEnd backEnd;
    private Game game;
    private FrontEnd frontEnd;

    private GameMessage gameMessage;

    @Before
    public void setUp() {

        backEnd = new BackEnd();
        frontEnd = new FrontEnd();
        //setto il frontEnd perché faccia riferimento al mio backEnd
        frontEnd.setBackEnd(backEnd);
        game = backEnd.getGame();
        //il game message deve essere del frontEnd
        gameMessage = new GameMessage(frontEnd);

    }

    @Test
    public void settingPlayersTest (){
        //esegue la execute dello stato BuildState con un giocatore che esegue la execute di default
        //currPlayer's God -> Tritone
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo", God.TRITON, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.TRITON);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(false);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        gameMessage.notify(gameMessage);




        assertEquals(frontEnd.getLiteGame().getName1(),"giuseppe");
        assertEquals(frontEnd.getLiteGame().getName2(),"paolo");
        assertEquals(frontEnd.getLiteGame().getName3(),"riccardo");

        assertEquals(frontEnd.getLiteGame().getGod1(),God.CHARON);
        assertEquals(frontEnd.getLiteGame().getGod2(),God.TRITON);
        assertEquals(frontEnd.getLiteGame().getGod3(),God.ATHENA);

        assertNotNull(frontEnd.getLiteGame().getCurrWorker());

        assertEquals(frontEnd.getLiteGame().getLevel1(),22);
        assertEquals(frontEnd.getLiteGame().getLevel2(),18);
        assertEquals(frontEnd.getLiteGame().getLevel3(),14);
        assertEquals(frontEnd.getLiteGame().getDome(),18);

        assertNotNull(frontEnd.getLiteGame().getTable());
        assertTrue(!frontEnd.getLiteGame().isWinner());
    }
}
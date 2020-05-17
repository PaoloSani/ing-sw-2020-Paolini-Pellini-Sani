package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrometheusBuildStateTest {

    private BackEnd backEnd;
    private FrontEnd frontEnd;
    private Game game;

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
    public void executeTest(){
        backEnd.setPlayer2(new Player("riccardo", God.PROMETHEUS, game) );
        backEnd.setPlayer3(new Player("paolo", God.POSEIDON, game) );
        backEnd.setChallenger(new Player("giuseppe", God.APOLLO, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("riccardo");
        gameMessage.setName3("paolo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.PROMETHEUS);
        gameMessage.setGod3(God.POSEIDON);
        gameMessage.setGod1(God.APOLLO);

        gameMessage.setCharonSwitching(false);

        backEnd.setState(backEnd.chooseWorkerState);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        game.getLiteGame().addObservers(frontEnd);
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());

        backEnd.setCurrPlayer(backEnd.getPlayer2());
        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //Scrivo la posizione dove voglio costruire
        int[] toBuild = new int[]{1,2};
        gameMessage.setSpace1(toBuild);
        gameMessage.setLevel(1);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());
    }

    @Test
    public void invalidSpaceBuild(){
        backEnd.setPlayer2(new Player("riccardo", God.PROMETHEUS, game) );
        backEnd.setPlayer3(new Player("paolo", God.POSEIDON, game) );
        backEnd.setChallenger(new Player("giuseppe", God.APOLLO, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("riccardo");
        gameMessage.setName3("paolo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.PROMETHEUS);
        gameMessage.setGod3(God.POSEIDON);
        gameMessage.setGod1(God.APOLLO);

        gameMessage.setCharonSwitching(false);

        backEnd.setState(backEnd.chooseWorkerState);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        game.getLiteGame().addObservers(frontEnd);
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());

        backEnd.setCurrPlayer(backEnd.getPlayer2());
        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //Scrivo la posizione dove voglio costruire
        int[] toBuild = new int[]{1,4};
        gameMessage.setSpace1(toBuild);
        gameMessage.setLevel(1);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        int height = game.getSpace(1,4).getHeight();
        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());
        assertFalse(backEnd.getLastExecute());
        assertEquals(height, game.getSpace(1,4).getHeight());

    }

}
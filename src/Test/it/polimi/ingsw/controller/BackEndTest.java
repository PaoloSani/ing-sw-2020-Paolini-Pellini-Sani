package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

class BackEndTest {

    private BackEnd backEnd;
    private FrontEnd frontEnd;
    private Game game;
    private GameMessage gameMessage;



    @Before
    public void setUp() {

        backEnd = new BackEnd();
        frontEnd = new FrontEnd();
        game = new Game();
        gameMessage = new GameMessage(frontEnd);

    }

    @Test
    public void update3PlayersChallengerTest() {

        backEnd.setChallenger(new Player("Riccardo", God.ATHENA, game));
        backEnd.setPlayer2(new Player("Paolo", God.PROMETHEUS, game));
        backEnd.setPlayer3(new Player("Giuseppe", God.ZEUS, game));
        backEnd.setCurrPlayer(backEnd.getChallenger());

        assertSame(backEnd.getCurrPlayer(), backEnd.getChallenger());

        backEnd.updateCurrPlayer();

        assertSame(backEnd.getCurrPlayer(), backEnd.getPlayer2());

    }

    @Test
    public void update3PlayersPlayer2Test() {

        backEnd.setChallenger(new Player("Riccardo", God.ATHENA, game));
        backEnd.setPlayer2(new Player("Paolo", God.PROMETHEUS, game));
        backEnd.setPlayer3(new Player("Giuseppe", God.ZEUS, game));
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        assertSame(backEnd.getCurrPlayer(), backEnd.getPlayer2());

        backEnd.updateCurrPlayer();

        assertSame(backEnd.getCurrPlayer(), backEnd.getPlayer3());

    }

    @Test
    public void update3PlayersPlayer3Test() {

        backEnd.setChallenger(new Player("Riccardo", God.ATHENA, game));
        backEnd.setPlayer2(new Player("Paolo", God.PROMETHEUS, game));
        backEnd.setPlayer3(new Player("Giuseppe", God.ZEUS, game));
        backEnd.setCurrPlayer(backEnd.getPlayer3());

        assertSame(backEnd.getCurrPlayer(), backEnd.getPlayer3());

        backEnd.updateCurrPlayer();

        assertSame(backEnd.getCurrPlayer(), backEnd.getChallenger());

    }

    @Test
    public void update2PlayersChallengerTest() {

        backEnd.setChallenger(new Player("Riccardo", God.ATHENA, game));
        backEnd.setPlayer2(null);
        backEnd.setPlayer3(new Player("Giuseppe", God.ZEUS, game));
        backEnd.setCurrPlayer(backEnd.getChallenger());

        assertSame(backEnd.getCurrPlayer(), backEnd.getChallenger());

        backEnd.updateCurrPlayer();

        assertSame(backEnd.getCurrPlayer(), backEnd.getPlayer3());

    }

    @Test
    public void update2PlayersPlayer2Test() {

        backEnd.setChallenger(new Player("Riccardo", God.ATHENA, game));
        backEnd.setPlayer2(new Player("Paolo", God.PROMETHEUS, game));
        backEnd.setPlayer3(null);
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        assertSame(backEnd.getCurrPlayer(), backEnd.getPlayer2());

        backEnd.updateCurrPlayer();

        assertSame(backEnd.getCurrPlayer(), backEnd.getChallenger());

    }

    @Test
    public void update2PlayersPlayer3Test() {

        backEnd.setChallenger(null);
        backEnd.setPlayer2(new Player("Paolo", God.PROMETHEUS, game));
        backEnd.setPlayer3(new Player("Giuseppe", God.ZEUS, game));
        backEnd.setCurrPlayer(backEnd.getPlayer3());

        assertSame(backEnd.getCurrPlayer(), backEnd.getPlayer3());

        backEnd.updateCurrPlayer();

        assertSame(backEnd.getCurrPlayer(), backEnd.getPlayer2());

    }
}
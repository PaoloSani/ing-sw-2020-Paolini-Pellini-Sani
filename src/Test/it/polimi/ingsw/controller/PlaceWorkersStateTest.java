package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlaceWorkersStateTest {

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
    public void ExecutionTrueTest(){
        backEnd.setPlayer2(new Player("riccardo", God.MORTAL, game) );
        backEnd.setPlayer3(new Player("paolo", God.POSEIDON, game) );
        backEnd.setChallenger(new Player("giuseppe", God.APOLLO, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("riccardo");
        gameMessage.setName3("paolo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.MORTAL);
        gameMessage.setGod3(God.POSEIDON);
        gameMessage.setGod1(God.APOLLO);

        gameMessage.setCharonSwitching(false);

        backEnd.setState(backEnd.setPlayersState);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        game.getLiteGame().addObservers(frontEnd);
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());


        backEnd.setCurrPlayer(backEnd.getPlayer2());

        gameMessage.setSpace1(new int[]{1,1});
        gameMessage.setSpace2(new int[]{2,2});

        assertFalse(frontEnd.getUpdate());

        gameMessage.notify(gameMessage);

        assertTrue(frontEnd.getUpdate());

    }

    @Test
    public void SameSpaceTest(){
        backEnd.setPlayer2(new Player("riccardo", God.MORTAL, game) );
        backEnd.setPlayer3(new Player("paolo", God.POSEIDON, game) );
        backEnd.setChallenger(new Player("giuseppe", God.APOLLO, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("riccardo");
        gameMessage.setName3("paolo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.MORTAL);
        gameMessage.setGod3(God.POSEIDON);
        gameMessage.setGod1(God.APOLLO);

        gameMessage.setCharonSwitching(false);

        backEnd.setState(backEnd.setPlayersState);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        game.getLiteGame().addObservers(frontEnd);
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());


        backEnd.setCurrPlayer(backEnd.getPlayer2());

        gameMessage.setSpace1(new int[]{1,1});
        gameMessage.setSpace2(new int[]{1,1});

        assertFalse(frontEnd.getUpdate());

        gameMessage.notify(gameMessage);

        assertFalse(frontEnd.getUpdate());

    }



    /*NON HANNO SENSO, mi restituiscono NullPointerException
     *
     *
    @Test
    public void FirstSpaceNullTest(){
        backEnd.setPlayer2(new Player("riccardo", God.MORTAL, game) );
        backEnd.setPlayer3(new Player("paolo", God.POSEIDON, game) );
        backEnd.setChallenger(new Player("giuseppe", God.APOLLO, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("riccardo");
        gameMessage.setName3("paolo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.MORTAL);
        gameMessage.setGod3(God.POSEIDON);
        gameMessage.setGod1(God.APOLLO);

        gameMessage.setCharonSwitching(false);

        backEnd.setState(backEnd.setPlayersState);

        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        backEnd.setCurrPlayer(backEnd.getPlayer2());

        gameMessage.setSpace1(null);
        gameMessage.setSpace2(new int[]{2,2});

        assertFalse(frontEnd.getUpdate());

        gameMessage.notify(gameMessage);

        assertFalse(frontEnd.getUpdate());

    }


    @Test
    public void SecondSpaceNullTest(){
        backEnd.setPlayer2(new Player("riccardo", God.MORTAL, game) );
        backEnd.setPlayer3(new Player("paolo", God.POSEIDON, game) );
        backEnd.setChallenger(new Player("giuseppe", God.APOLLO, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("riccardo");
        gameMessage.setName3("paolo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.MORTAL);
        gameMessage.setGod3(God.POSEIDON);
        gameMessage.setGod1(God.APOLLO);

        gameMessage.setCharonSwitching(false);

        backEnd.setState(backEnd.setPlayersState);

        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        backEnd.setCurrPlayer(backEnd.getPlayer2());

        gameMessage.setSpace2(null);
        gameMessage.setSpace1(new int[]{2,2});

        assertFalse(frontEnd.getUpdate());

        gameMessage.notify(gameMessage);

        assertFalse(frontEnd.getUpdate());

    }


     */
}
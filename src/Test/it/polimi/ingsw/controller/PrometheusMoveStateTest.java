package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class PrometheusMoveStateTest {
    private BackEnd backEnd;
    private Game game;
    private FrontEnd frontEnd;

    private GameMessage gameMessage;



    @Before
    public void setUp() throws Exception {

        backEnd = new BackEnd();
        frontEnd = new FrontEnd();
        //setto il frontEnd perché faccia riferimento al mio backEnd
        frontEnd.setBackEnd(backEnd);
        game = backEnd.getGame();
        //il game message deve essere del frontEnd
        gameMessage = new GameMessage(frontEnd);
        backEnd.prometheusMoveState.reset();

    }
    //Se Prometeo prova a salire mi ritorna un false e non viene modificato il model
    @Test
    public void movingUp_or_Down() {
        //esegue la execute dello stato PrometheusMoveState con un giocatore che esegue la execute di default
        //currPlayer's God -> Tritone
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo",God.PROMETHEUS, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.PROMETHEUS);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era buildState così lo aggiorno con l'update
        // inutile nel nostro progetto, utile solo per i test!
        backEnd.setState(backEnd.prometheusBuildState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message un la posizione in cui voglio muovermi
        int[] spaceToMove =  {1,2};
        gameMessage.setSpace1(spaceToMove);
        //Imposto nella classe game del model che l'altezza della cella dove voglio muovermi è maggiore di quella in cui il worker è
        game.getSpace(1,2).setHeight(1);
        game.getSpace(1,1).setHeight(0);
        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        //sto simulando la notify del model litegame sul front end
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);
        assertFalse(backEnd.getLastExecute());
    }


    @Test
    public void movingTest() {
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

        //setto che lo stato precedente era buildState così lo aggiorno con l'update
        //inutile nel nostro progetto, utile solo per i test!
        backEnd.setState(backEnd.prometheusBuildState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message un la posizione in cui voglio muovermi
        int[] spaceToMove =  {1,2};
        gameMessage.setSpace1(spaceToMove);
        //Imposto nella classe game del model che l'altezza della cella dove voglio muovermi è uguale a quella della cella in cui è il mio worker
        game.getSpace(1,2).setHeight(0);
        game.getSpace(1,1).setHeight(0);
        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        //sto simulando la notify del model litegame sul front end
        assertFalse(frontEnd.getUpdate());

        //mando la notify al controller
        gameMessage.notify(gameMessage);
        //controllo che il metodo execute mi ha ritornato true
        assertTrue(backEnd.getLastExecute());
        //lo stato successivo deve essere build perchè ho eseguito la move di prometeo;
        //assertEquals(backEnd.buildState, backEnd.getCurrState());
        //dopo la mossa il worker si è spostato dalla cella precedente a quella successiva
        assertNull(game.getSpace(1,1).getWorker());
        assertEquals(backEnd.getCurrPlayer().getWorker1(),game.getSpace(1,2).getWorker());
        //controllo che il lie game corrisponda alla mia situazione
        assertEquals("B0N",frontEnd.getLiteGame().getStringValue(1,2));
        assertEquals("V0N",frontEnd.getLiteGame().getStringValue(1,1));

    }

    @Test //Sto passando una cella in cui voglio muovermi fuori dalla table
    public void nullSpaceMoving() {
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

        //setto che lo stato precedente era buildState così lo aggiorno con l'update
        // inutile nel nostro progetto, utile solo per i test!
        backEnd.setState(backEnd.prometheusBuildState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message un la posizione in cui voglio muovermi e la metto fuori dalla table
        int[] spaceToMove =  {10,10};;
        gameMessage.setSpace1(spaceToMove);
        //Imposto nella classe game del model che l'altezza della cella dove voglio muovermi è maggiore di quella in cui il worker è
        game.getSpace(1,1).setHeight(0);
        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        //sto simulando la notify del model litegame sul front end
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);
        assertFalse(backEnd.getLastExecute());
    }

}

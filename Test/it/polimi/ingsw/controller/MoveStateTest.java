package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.LiteGame;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveStateTest {

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
    public void testExecute()
    {
        //esegue la execute dello stato MoveState con un giocatore che esegue la execute di default
        //currPlayer's God -> Mortale
        //inizializzo i player
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

        backEnd.setState(backEnd.chooseWorkerState);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());


        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione del giocatore con cui voglio muovermi
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message la posizione in cui voglio muovermi e setto il level a 0
        int[] toBeOccupied = {1,2};
        gameMessage.setSpace1(toBeOccupied);
        gameMessage.setLevel(0);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la move e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());

        assertEquals(backEnd.getCurrState(),backEnd.moveState);

        gameMessage.notify(gameMessage);

        assertEquals(backEnd.getCurrState(), backEnd.buildState);

    }

    @Test
    public void artemisExecuteTest(){
        //esegue la execute dello stato MoveState con un giocatore che esegue la execute di default
        //currPlayer's God -> Artemis
        //inizializzo i player
        backEnd.setPlayer2(new Player("riccardo", God.ARTEMIS, game) );
        backEnd.setPlayer3(new Player("paolo", God.POSEIDON, game) );
        backEnd.setChallenger(new Player("giuseppe", God.APOLLO, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("riccardo");
        gameMessage.setName3("paolo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.ARTEMIS);
        gameMessage.setGod3(God.POSEIDON);
        gameMessage.setGod1(God.APOLLO);

        backEnd.setState(backEnd.chooseWorkerState);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());


        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione del giocatore con cui voglio muovermi
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message la posizione in cui voglio muovermi e setto il level a 0
        int[] toBeOccupied = {1,2};
        gameMessage.setSpace1(toBeOccupied);
        gameMessage.setLevel(0);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la move e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());

        //Devo inizializzarlo a false! Altrimenti mi falsa i risultati
        frontEnd.resetUpdate(); //Todo: correggere il resetUpdate in FrontEnd


        //Faccio sì che Artemide provi a tornare indietro
        toBeOccupied = new int[]{1,1};
        gameMessage.setSpace1(toBeOccupied);
        gameMessage.setLevel(0);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //Controllo che Artemide non sia tornata indietro
        assertFalse(frontEnd.getUpdate());

        //Faccio sì che Artemide faccia un'altra mossa (valida)
        toBeOccupied = new int[]{1,3};
        gameMessage.setSpace1(toBeOccupied);
        gameMessage.setLevel(0);

        //Controllo che Artemide non sia tornata indietro
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        assertTrue(frontEnd.getUpdate());


    }

    @Test
    public void tritonExecuteTest() {
        //esegue la execute dello stato MoveState con un giocatore che esegue la execute di default
        //currPlayer's God -> Triton
        //inizializzo i player
        backEnd.setPlayer2(new Player("riccardo", God.TRITON, game));
        backEnd.setPlayer3(new Player("paolo", God.POSEIDON, game));
        backEnd.setChallenger(new Player("giuseppe", God.APOLLO, game));

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("riccardo");
        gameMessage.setName3("paolo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.TRITON);
        gameMessage.setGod3(God.POSEIDON);
        gameMessage.setGod1(God.APOLLO);

        backEnd.setState(backEnd.chooseWorkerState);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione del giocatore con cui voglio muovermi
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1, 1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message la posizione in cui voglio muovermi e setto il level a 0
        int[] toBeOccupied = {0, 0};
        gameMessage.setSpace1(toBeOccupied);
        gameMessage.setLevel(0);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la move e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());

        //Devo inizializzarlo a false! Altrimenti mi falsa i risultati
        frontEnd.resetUpdate();


        // Secondo giro
        // Scrivo nel game message la posizione in cui voglio muovermi e setto il level a 0
        toBeOccupied = new int[]{0, 1};
        gameMessage.setSpace1(toBeOccupied);
        gameMessage.setLevel(0);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la move e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());

        //Devo inizializzarlo a false! Altrimenti mi falsa i risultati
        frontEnd.resetUpdate();

        // Terzo giro
        // Scrivo nel game message la posizione in cui voglio muovermi e setto il level a 0
        toBeOccupied = new int[]{0, 2};
        gameMessage.setSpace1(toBeOccupied);
        gameMessage.setLevel(0);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la move e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());

        //Devo inizializzarlo a false! Altrimenti mi falsa i risultati
        frontEnd.resetUpdate();

        // Quarto giro
        // Scrivo nel game message la posizione in cui voglio muovermi e setto il level a 0
        toBeOccupied = new int[]{0, 3};
        gameMessage.setSpace1(toBeOccupied);
        gameMessage.setLevel(0);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la move e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());

        //Devo inizializzarlo a false! Altrimenti mi falsa i risultati
        frontEnd.resetUpdate();

        // Quinto giro - ultimo!
        // Scrivo nel game message la posizione in cui voglio muovermi e setto il level a 0
        toBeOccupied = new int[]{1, 3};
        gameMessage.setSpace1(toBeOccupied);
        gameMessage.setLevel(0);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la move e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());
        /*assertEquals(backEnd.getCurrState(), backEnd.moveState);

        gameMessage.notify(gameMessage);

        assertEquals(backEnd.getCurrState(), backEnd.buildState);*/


    }
}
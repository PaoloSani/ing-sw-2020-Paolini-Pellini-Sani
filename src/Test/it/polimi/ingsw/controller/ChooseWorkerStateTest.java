package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChooseWorkerStateTest {
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

    }

    @Test
    public void executeTest() {
        //esegue la execute dello stato ChooseWorkerState
        //currPlayer's God -> TRITON
        //inizializzo i player
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game));
        backEnd.setPlayer2(new Player("paolo", God.TRITON, game));
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game));

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel CharonSwitchState
        gameMessage.setName1("giuseppe");
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setGod1(God.CHARON);
        gameMessage.setGod2(God.TRITON);
        gameMessage.setGod3(God.ATHENA);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era PlaceWorkerState così lo aggiorno con l'update
        backEnd.setState(backEnd.placeWorkersState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        //questo deve per forza di cose essere il challenger se voglio cambiare stato perché nello stato di PlaceWorkerState ciclo tre volte
        backEnd.setCurrPlayer(backEnd.getChallenger());

        //setto le posizioni del mio worker
        backEnd.getPlayer2().getWorker1().setSpace(game.getSpace(1, 1));
        backEnd.getPlayer2().getWorker2().setSpace(game.getSpace(2, 1));

        //scrivo nel game message la posizione del worker che voglio scegliere
        int[] spaceOfMyChoice = {1, 1};
        gameMessage.setSpace1(spaceOfMyChoice);


        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());
        int[] position = game.getLiteGame().getCurrWorker();
        assertArrayEquals(spaceOfMyChoice, position);
    }

    @Test
    public void hypnusBlockTest() {
        //esegue la execute dello stato ChooseWorkerState
        //currPlayer's God -> TRITON
        //inizializzo i player
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game));
        backEnd.setPlayer2(new Player("paolo", God.TRITON, game));
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game));

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel CharonSwitchState
        gameMessage.setName1("giuseppe");
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setGod1(God.CHARON);
        gameMessage.setGod2(God.TRITON);
        gameMessage.setGod3(God.HYPNUS);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era PlaceWorkerState così lo aggiorno con l'update
        backEnd.setState(backEnd.placeWorkersState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        //questo deve per forza di cose essere il challenger se voglio cambiare stato perché nello stato di PlaceWorkerState ciclo tre volte
        backEnd.setCurrPlayer(backEnd.getChallenger());

        //setto le posizioni del mio worker
        backEnd.getPlayer2().getWorker1().setSpace(game.getSpace(1, 1));
        backEnd.getPlayer2().getWorker2().setSpace(game.getSpace(2, 1));
        game.getSpace(1, 1).setHeight(2);
        game.getSpace(2, 1).setHeight(1);
        game.getConstraint().setHypnus(true);
        //scrivo nel game message la posizione del worker che voglio scegliere
        int[] spaceOfMyChoice = {1, 1};
        gameMessage.setSpace1(spaceOfMyChoice);


        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());
        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());

        assertEquals(backEnd.getCurrWorker().getSpace(), game.getSpace(2, 1));
        assertEquals(backEnd.getCurrPlayer().getOtherWorker(backEnd.getCurrPlayer().getWorker1()), backEnd.getPlayer2().getWorker2());
    }

    @Test
    public void executeOtherWorkerTest() {
        //esegue la execute dello stato ChooseWorkerState nel il worker da me richiesto non può muoversi
        //currPlayer's God -> TRITON
        //inizializzo i player
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );
        backEnd.setPlayer2(new Player("paolo", God.TRITON, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel CharonSwitchState
        gameMessage.setName1("giuseppe");
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setGod1(God.CHARON);
        gameMessage.setGod2(God.TRITON);
        gameMessage.setGod3(God.ATHENA);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era PlaceWorkerState così lo aggiorno con l'update
        backEnd.setState(backEnd.placeWorkersState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        //questo deve per forza di cose essere il challenger se voglio cambiare stato perché nello stato di PlaceWorkerState ciclo tre volte
        backEnd.setCurrPlayer(backEnd.getChallenger());

        //setto le posizioni dei miei worker (con il player 2 perché verrà cambiato non appena viene eseguito l'update nel controller)
        backEnd.getPlayer2().getWorker1().setSpace(game.getSpace(1,1));
        backEnd.getPlayer2().getWorker2().setSpace(game.getSpace(2,1));

        //scrivo nel game message la posizione del worker che voglio scegliere
        int[] spaceOfMyChoice = {1,1};
        gameMessage.setSpace1(spaceOfMyChoice);

        //setto la tabella sicchè il mio worker1 non possa muoversi
        backEnd.getGame().getSpace(0,0).setDome();
        backEnd.getGame().getSpace(0,1).setDome();
        backEnd.getGame().getSpace(0,2).setDome();
        backEnd.getGame().getSpace(1,0).setDome();
        backEnd.getGame().getSpace(1,2).setDome();
        backEnd.getGame().getSpace(2,0).setDome();
        backEnd.getGame().getSpace(2,2).setDome();



        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());
        int[] newPosition = {2,1};
        int[] position = game.getLiteGame().getCurrWorker();
        assertArrayEquals(newPosition, position);
    }

    @Test
    public void executeFailTest() {
        //esegue la execute dello stato ChooseWorkerState nessun worker del player può muoversi
        //currPlayer's God -> TRITON
        //inizializzo i player
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );
        backEnd.setPlayer2(new Player("paolo", God.TRITON, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel CharonSwitchState
        gameMessage.setName1("giuseppe");
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setGod1(God.CHARON);
        gameMessage.setGod2(God.TRITON);
        gameMessage.setGod3(God.ATHENA);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era PlaceWorkerState così lo aggiorno con l'update
        backEnd.setState(backEnd.placeWorkersState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());
        game.getLiteGame().addObservers(frontEnd);
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        //questo deve per forza di cose essere il challenger se voglio cambiare stato perché nello stato di PlaceWorkerState ciclo tre volte
        backEnd.setCurrPlayer(backEnd.getChallenger());

        //setto le posizioni dei miei worker (con il player 2 perché verrà cambiato non appena viene eseguito l'update nel controller)
        backEnd.getPlayer2().getWorker1().setSpace(game.getSpace(1,1));
        backEnd.getPlayer2().getWorker2().setSpace(game.getSpace(2,1));

        //scrivo nel game message la posizione del worker che voglio scegliere
        int[] spaceOfMyChoice = {1,1};
        gameMessage.setSpace1(spaceOfMyChoice);

        //setto la tabella sicchè il mio worker1 non possa muoversi
        backEnd.getGame().getSpace(0,0).setDome();
        backEnd.getGame().getSpace(0,1).setDome();
        backEnd.getGame().getSpace(0,2).setDome();
        backEnd.getGame().getSpace(1,0).setDome();
        backEnd.getGame().getSpace(1,2).setDome();
        backEnd.getGame().getSpace(2,0).setDome();
        backEnd.getGame().getSpace(2,2).setDome();

        //setto la tabella sicchè il worker2 non si possa muovere
        backEnd.getGame().getSpace(3,0).setDome();
        backEnd.getGame().getSpace(3,1).setDome();
        backEnd.getGame().getSpace(3,2).setDome();




        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        int[] position = game.getLiteGame().getCurrWorker();
        assertArrayEquals(null, position);
    }
}
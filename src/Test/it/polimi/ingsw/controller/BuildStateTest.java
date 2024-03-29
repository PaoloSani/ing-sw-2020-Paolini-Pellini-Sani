package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildStateTest {
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
    public void testExecute() {
        //esegue la execute dello stato BuildState con un giocatore che esegue la execute di default
        //currPlayer's God -> Tritone
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo", God.POSEIDON, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.POSEIDON);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era MoveState così lo aggiorno con l'update
        //inutile nel nostro progetto, utile solo per i test!
        backEnd.setState(backEnd.prometheusMoveState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));
        backEnd.getCurrPlayer().getWorker2().setSpace(game.getSpace(0,1));
        game.getSpace(0,1).setHeight(1);

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message un livello da costruire e la posizione in cui voglio costruire
        int[] spaceToBuild = {1,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

    }

    @Test
    public void demeterExecuteTest(){
        //esegue la execute dello stato BuildState con un giocatore che esegue la execute di DEMETER
        //currPlayer's God -> DEMETER
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo", God.DEMETER, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.DEMETER);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era MoveState così lo aggiorno con l'update
        backEnd.setState(backEnd.moveState);

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

        //scrivo nel game message un livello da costruire e la posizione in cui voglio costruire
        int[] spaceToBuild = {1,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);
        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        //ora eseguo la seconda costruzione, che non può essere sulla posizione della prima
        spaceToBuild = new int[]{2,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        //se provo a costruire una terza volta, non ho modifiche alla tabella
        spaceToBuild = new int[]{2,1};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertFalse(frontEnd.getUpdateModel());


        //infine testo il caso in cui io cambio stato a partire dalla build
        gameMessage.setLevel(0);

        gameMessage.notify(gameMessage);

        assertEquals(backEnd.getCurrState(), backEnd.chooseWorkerState);

    }

    @Test
    public void hephaestusExecuteTest(){
        //esegue la execute dello stato BuildState con un giocatore che esegue la execute di DEMETER
        //currPlayer's God -> HEPHAESTUS
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo", God.HEPHAESTUS, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.HEPHAESTUS);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era MoveState così lo aggiorno con l'update
        backEnd.setState(backEnd.moveState);

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

        //scrivo nel game message un livello da costruire e la posizione in cui voglio costruire
        int[] spaceToBuild = {1,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        //ora eseguo la seconda costruzione, che deve essere sulla posizione della prima e non essere una cupola
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(4);

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertFalse(frontEnd.getUpdateModel());

        //se provo a costruire una terza volta, non ho modifiche alla tabella
        spaceToBuild = new int[]{2,1};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertFalse(frontEnd.getUpdateModel());


        //infine testo il caso in cui io cambio stato a partire dalla build
        gameMessage.setLevel(0);

        gameMessage.notify(gameMessage);

        assertEquals(backEnd.getCurrState(), backEnd.buildState);
    }

    @Test
    public void hephaestusExecuteTest2(){
        //esegue la execute dello stato BuildState con un giocatore che esegue la execute di DEMETER
        //currPlayer's God -> HEPHAESTUS
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo", God.HEPHAESTUS, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.HEPHAESTUS);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era MoveState così lo aggiorno con l'update
        backEnd.setState(backEnd.moveState);

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

        //scrivo nel game message un livello da costruire e la posizione in cui voglio costruire
        int[] spaceToBuild = {1,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        //ora eseguo la seconda costruzione, che deve essere sulla posizione della prima e non essere una cupola
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(2);

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        //se provo a costruire una terza volta, non ho modifiche alla tabella
        spaceToBuild = new int[]{2,1};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertFalse(frontEnd.getUpdateModel());


        //infine testo il caso in cui io cambio stato a partire dalla build
        gameMessage.setLevel(0);

        gameMessage.notify(gameMessage);

        assertEquals(backEnd.getCurrState(), backEnd.chooseWorkerState);
    }

    @Test
    public void poseidonExecuteTest(){
        //esegue la execute dello stato BuildState con un giocatore che esegue la execute di default
        //currPlayer's God -> Tritone
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo", God.POSEIDON, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.POSEIDON);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era MoveState così lo aggiorno con l'update
        backEnd.setState(backEnd.moveState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));
        //setto la posizione del secondo Worker
        backEnd.getCurrPlayer().getWorker2().setSpace(game.getSpace(3,3));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message un livello da costruire e la posizione in cui voglio costruire
        int[] spaceToBuild = {1,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        //ora il worker corrente dovrebbe essere il worker2
        assertEquals(backEnd.getCurrWorker(), backEnd.getCurrPlayer().getWorker2());

        //costruisco tre volte con il worker2 sulla stessa cella
        spaceToBuild = new int[]{2,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);


        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        spaceToBuild = new int[]{2,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(2);


        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        spaceToBuild = new int[]{2,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(3);


        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        //provo una quarta volta, ma senza successo
        spaceToBuild = new int[]{2,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(4);


        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertFalse(frontEnd.getUpdateModel());

    }

    @Test
    public void poseidonExecuteTest2(){
        //esegue la execute dello stato BuildState con un giocatore che esegue la execute di default
        //currPlayer's God -> Tritone
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo", God.POSEIDON, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.POSEIDON);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era MoveState così lo aggiorno con l'update
        backEnd.setState(backEnd.moveState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));
        //setto la posizione del secondo Worker
        backEnd.getCurrPlayer().getWorker2().setSpace(game.getSpace(3,3));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message un livello da costruire e la posizione in cui voglio costruire
        int[] spaceToBuild = {1,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        //ora il worker corrente dovrebbe essere il worker2
        assertEquals(backEnd.getCurrWorker(), backEnd.getCurrPlayer().getWorker2());

        //costruisco tre volte con il worker2 sulla stessa cella
        spaceToBuild = new int[]{2,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);


        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        spaceToBuild = new int[]{2,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(2);


        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        spaceToBuild = new int[]{2,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(3);


        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

        //provo una quarta volta, ma senza successo
        spaceToBuild = new int[]{2,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(4);


        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertFalse(frontEnd.getUpdateModel());

    }


    @Test
    public void nextStateTest(){
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

        //setto che lo stato precedente era MoveState così lo aggiorno con l'update
        //inutile nel nostro progetto, utile solo per i test!
        backEnd.setState(backEnd.buildState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        backEnd.getPlayer2().getWorker1().setSpace(game.getSpace(1, 1));
        backEnd.getPlayer2().getWorker2().setSpace(game.getSpace(2, 1));
        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());
        backEnd.setToRemove(backEnd.getPlayer2());
        //scrivo nel game message un livello da costruire e la posizione in cui voglio costruire
        int[] spaceToBuild = {1,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());
        assertEquals( backEnd.removePlayerState,backEnd.getCurrState());
    }
}
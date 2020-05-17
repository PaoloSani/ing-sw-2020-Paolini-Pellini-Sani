package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharonSwitchStateTest {
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
        //esegue la execute dello stato CharonSwitchState
        //currPlayer's God -> CHARON
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo", God.TRITON, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel CharonSwitchState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.TRITON);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(true);

        //setto che lo stato precedente era ChooseWorkerState così lo aggiorno con l'update
        backEnd.setState(backEnd.chooseWorkerState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getChallenger());

        //setto le posizioni del mio worker
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // setto il worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo la posizione del worker che voglio switchare
        backEnd.getPlayer2().getWorker1().setSpace(game.getSpace(2,2));

        //scrivo nel game message la posizione del worker che voglio spostare
        int[] spaceToSwitch = {2,2};
        gameMessage.setSpace1(spaceToSwitch);


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
    public void notExecuteTest() {
        //esegue la execute dello stato CharonSwitchState e non ritorna niente per errore
        //currPlayer's God -> CHARON
        //inizializzo i player
        backEnd.setPlayer2(new Player("paolo", God.TRITON, game) );
        backEnd.setPlayer3(new Player("riccardo", God.ATHENA, game) );
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game) );

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel CharonSwitchState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.TRITON);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(true);

        //setto che lo stato precedente era ChooseWorkerState così lo aggiorno con l'update
        backEnd.setState(backEnd.chooseWorkerState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getChallenger());

        //setto le posizioni del mio worker
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo la posizione del worker che voglio switchare
        backEnd.getPlayer2().getWorker1().setSpace(game.getSpace(3,3));

        //scrivo nel game message la posizione del worker che voglio spostare
        int[] spaceToSwitch = {3,3};
        gameMessage.setSpace1(spaceToSwitch);


        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdateModel());

    }

}
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
    BuildState buildState;
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
        //TODO: inutile nel nostro progetto, utile solo per i test!
        backEnd.setState(backEnd.moveState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());

    }

    @Test
    public void testExecute() {
        //esegue la execute dello stato BuildState

        //setto la posizione del giocatore con cui voglio costruire
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));
        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //scrivo nel game message un livello da costruire e la posizione in cui voglio costruire
        int[] spaceToBuild = {1,2};
        gameMessage.setSpace1(spaceToBuild);
        gameMessage.setLevel(1);

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdate());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);

        //il programma esegue la build e infine manda la notifica al frontEnd

        //Il frontEnd è stato notificato
        //se fisso qua il breakpoint posso controllare che la tabella ricevuta sia giusta
        assertTrue(frontEnd.getUpdate());

    }
}
package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class WinStateTest {

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
    public void winning (){
        //esegue la execute dello stato ChooseWorkerState
        //currPlayer's God -> TRITON
        //inizializzo i player
        backEnd.setChallenger(new Player("giuseppe", God.CHARON, game));

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel CharonSwitchState
        gameMessage.setName1("giuseppe");
        //gameMessage.setName2("paolo");
        //gameMessage.setName3("riccardo");
        gameMessage.setGod1(God.CHARON);
        //gameMessage.setGod2(God.TRITON);
        //gameMessage.setGod3(God.ATHENA);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era PlaceWorkerState così lo aggiorno con l'update
        backEnd.setState(backEnd.removePlayerState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        //game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());
        //
        game.getLiteGame().setName1Test("giuseppe");
        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        //questo deve per forza di cose essere il challenger se voglio cambiare stato perché nello stato di PlaceWorkerState ciclo tre volte
        backEnd.setCurrPlayer(backEnd.getChallenger());

        //setto le posizioni del mio worker
        backEnd.getChallenger().getWorker1().setSpace(game.getSpace(1, 1));
        backEnd.getChallenger().getWorker2().setSpace(game.getSpace(2, 1));

        //scrivo nel game message la posizione del worker che voglio scegliere
        int[] spaceOfMyChoice = {1, 1};
        gameMessage.setSpace1(spaceOfMyChoice);


        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);
        assertTrue(game.getLiteGame().isWinner());
        backEnd.winState.reset();
        game.getLiteGame().setPlayer("giuseppe");
        assertEquals(backEnd.getCurrPlayer().getNickname(), game.getLiteGame().getCurrPlayer());
    }

    @Test
    public void stillPlaying (){
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
        backEnd.setState(backEnd.removePlayerState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());
        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        //questo deve per forza di cose essere il challenger se voglio cambiare stato perché nello stato di PlaceWorkerState ciclo tre volte
        backEnd.setCurrPlayer(backEnd.getChallenger());

        //setto le posizioni del mio worker
        backEnd.getChallenger().getWorker1().setSpace(game.getSpace(1, 1));
        backEnd.getChallenger().getWorker2().setSpace(game.getSpace(2, 1));

        //scrivo nel game message la posizione del worker che voglio scegliere
        int[] spaceOfMyChoice = {1, 1};
        gameMessage.setSpace1(spaceOfMyChoice);


        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        assertFalse(frontEnd.getUpdateModel());

        //Mando la notify al controller
        gameMessage.notify(gameMessage);
        assertEquals(backEnd.getCurrState(), backEnd.chooseWorkerState);

    }
}

package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.GameMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class RemovePlayerStateTest {
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
    public void removingPlayerTest(){
        //esegue la execute dello stato RemovePlayerState con un giocatore che esegue la execute di default
        //currPlayer's God -> Tritone
        //inizializzo i player
        Player player1 = new Player("giuseppe", God.CHARON, game);
        Player player2 = new Player("paolo", God.TRITON, game);
        Player player3 = new Player("riccardo", God.ATHENA, game);

        backEnd.setPlayer2(player2);
        backEnd.setPlayer3(player3);
        backEnd.setChallenger(player1);

        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setName3("riccardo");
        gameMessage.setName1("giuseppe");
        gameMessage.setGod2(God.TRITON);
        gameMessage.setGod3(God.ATHENA);
        gameMessage.setGod1(God.CHARON);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era chooseWorker così lo aggiorno con l'update
        //inutile nel nostro progetto, utile solo per i test!
        backEnd.setState(backEnd.chooseWorkerState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer2());

        //setto la posizione dei due workers del player 2 da eliminare
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));
        backEnd.getCurrPlayer().getWorker2().setSpace(game.getSpace(1,2));
        game.getSpace(1,2).setHeight(0);
        game.getSpace(1,1).setHeight(0);

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //setto il player 2 come giocatore da eliminare
        backEnd.setToRemove(backEnd.getPlayer2());

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        //sto simulando la notify del model litegame sul front end
        assertFalse(frontEnd.getUpdateModel());

        //mando la notify al controller
        gameMessage.notify(gameMessage);

        //controllo che siano stati eliminati i 2 workers e il player 2

        assertNull(game.getSpace(1,1).getWorker());
        assertNull(game.getSpace(1,2).getWorker());
        assertEquals("V0N",frontEnd.getLiteGame().getStringValue(1,2));
        assertEquals("V0N",frontEnd.getLiteGame().getStringValue(1,1));

        backEnd.setPlayer2(player2);


        //inizializzo il contenuto di GameMessage perché non sono passato dallo stato setPlayersState ma sono andato direttamente nel BuildState
        gameMessage.setName2("paolo");
        gameMessage.setGod2(God.TRITON);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era chooseWorker così lo aggiorno con l'update
        //inutile nel nostro progetto, utile solo per i test!
        backEnd.setState(backEnd.chooseWorkerState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getPlayer3());

        //setto la posizione dei due workers del player 2 da eliminare
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));
        backEnd.getCurrPlayer().getWorker2().setSpace(game.getSpace(1,2));
        game.getSpace(1,2).setHeight(0);
        game.getSpace(1,1).setHeight(0);

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //setto il player 2 come giocatore da eliminare
        backEnd.setToRemove(backEnd.getPlayer3());

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        //sto simulando la notify del model litegame sul front end
        assertFalse(frontEnd.getUpdateModel());

        //mando la notify al controller
        gameMessage.notify(gameMessage);

        assertNull(game.getSpace(1,1).getWorker());
        assertNull(game.getSpace(1,2).getWorker());
        assertEquals("V0N",frontEnd.getLiteGame().getStringValue(1,2));
        assertEquals("V0N",frontEnd.getLiteGame().getStringValue(1,1));

        gameMessage.setName3("riccardo");
        gameMessage.setGod3(God.ATHENA);

        gameMessage.setCharonSwitching(false);

        //setto che lo stato precedente era chooseWorker così lo aggiorno con l'update
        //inutile nel nostro progetto, utile solo per i test!
        backEnd.setState(backEnd.chooseWorkerState);

        //setto l'observer del litegame ( anche qui, lo faccio perché non sono passato da setPlayersState )
        game.getLiteGame().addObservers(frontEnd);

        //setto i giocatori nella classe LiteGame passando per il Game
        game.setPlayers(backEnd.getChallenger(), backEnd.getPlayer2(), backEnd.getPlayer3());
        frontEnd.setLiteGame(game.getLiteGame().cloneLG());

        //siccome non passo dallo stato placeWorkerState inizializzo il giocatore corrente
        backEnd.setCurrPlayer(backEnd.getChallenger());

        //setto la posizione dei due workers del player 2 da eliminare
        backEnd.getCurrPlayer().getWorker1().setSpace(game.getSpace(1,1));
        backEnd.getCurrPlayer().getWorker2().setSpace(game.getSpace(1,2));
        game.getSpace(1,2).setHeight(0);
        game.getSpace(1,1).setHeight(0);

        // lo setto come worker corrente perché non sono passato da ChooseWorkerState
        backEnd.setCurrWorker(backEnd.getCurrPlayer().getWorker1());

        //setto il player 2 come giocatore da eliminare
        backEnd.setToRemove(backEnd.getChallenger());

        //all'inizio il frontEnd non ha ricevuto nessuna notifica
        //sto simulando la notify del model litegame sul front end
        assertFalse(frontEnd.getUpdateModel());

        //mando la notify al controller
        gameMessage.notify(gameMessage);

        assertNull(game.getSpace(1,1).getWorker());
        assertNull(game.getSpace(1,2).getWorker());
        assertEquals("V0N",frontEnd.getLiteGame().getStringValue(1,2));
        assertEquals("V0N",frontEnd.getLiteGame().getStringValue(1,1));

    }
}

package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.*;
import org.junit.Test;

public class CommandLineGameTest {

    CommandLineGame cli = new CommandLineGame();
    Game game = new Game();

    @Test
    public void chosenWorkerTest(){
        Player player1 = new Player("Rick", God.MORTAL,game);
        Player player2 = new Player("Paul", God.MORTAL,game);
        Player player3 = new Player("Giuse", God.MORTAL,game);
        game.setPlayers(player1, player2, player3);
        Worker w = new Worker(player1);
        w.setSpace(game.getSpace(2,3));
        game.setCurrWorker(w);
        game.refreshLiteGame();
        cli.setLiteGame(game.getLiteGame().makeSerializable());
        cli.buildGameTable();
    }



    @Test
    public void exampleTableRow(){
        String[] testRow = new String[]{"V0D","A2N","B0N","A3N","V1N"};
        cli.buildTableRow(testRow,0);
    }

    @Test
    public void printStartingGameTable(){
        game.getSpace(0,1).setHeight(2);
        game.getSpace(1,0).setHeight(2);
        game.getSpace(1,1).setHeight(1);
        game.getSpace(0,2).setHeight(1);
        game.getSpace(2,0).setHeight(1);
        game.getSpace(0,0).setHeight(3);
        game.getSpace(2,3).setHeight(1);
        game.getSpace(3,1).setHeight(3);
        game.getSpace(3,2).setHeight(2);
        game.getSpace(2,3).setHeight(2);
        game.getSpace(3,0).setHeight(4);
        game.getSpace(3,3).setHeight(1);
        game.getSpace(2,4).setHeight(1);
        game.getSpace(4,4).setDome();
        Player player1 = new Player("Rick", God.MORTAL,game);
        Player player2 = new Player("Paul", God.MORTAL,game);
        Player player3 = new Player("Giuse", God.MORTAL,game);
        game.setPlayers(player1, player2, player3);
        Worker w = new Worker(player1);
        game.getSpace(0,0).setWorker(new Worker(player1));
        game.getSpace(0,1).setWorker(new Worker(player1));
        game.getSpace(2,3).setWorker(new Worker(player2));
        w.setSpace(game.getSpace(1,4));
        game.getSpace(3,1).setWorker(new Worker(player3));
        game.setCurrWorker(w);
        game.refreshLiteGame();
        cli.setNumOfPlayer(3);
        cli.setLiteGame(game.getLiteGame().makeSerializable());
        cli.buildGameTable();
    }

}
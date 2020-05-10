package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.*;
import org.junit.Test;

public class CommandLineGameTest {

    CommandLineGame cli = new CommandLineGame();
    Game game = new Game();

    @Test
    public void voidGroundGameSpace(){
        String testString = "V0N";
        String[] result = cli.buildGameSpace(testString);
        for (String s : result){
            System.out.println(s);
        }
    }

    @Test
    public void ALevel1GameSpace(){
        String testString = "A1N";
        String[] result = cli.buildGameSpace(testString);
        for (String s : result){
            System.out.println(s);
        }
    }

    @Test
    public void BLevel2GameSpace(){
        String testString = "B2N";
        String[] result = cli.buildGameSpace(testString);
        for (String s : result){
            System.out.println(s);
        }
    }

    @Test
    public void CLevel3GameSpace(){
        String testString = "C3N";
        String[] result = cli.buildGameSpace(testString);
        for (String s : result){
            System.out.println(s);
        }
    }

    @Test
    public void domedGameSpace(){
        String testString = "V0D";
        String[] result = cli.buildGameSpace(testString);
        for (String s : result){
            System.out.println(s);
        }
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
        game.getSpace(0,0).setWorker(new Worker(player1));
        game.getSpace(0,1).setWorker(new Worker(player1));
        game.getSpace(2,3).setWorker(new Worker(player2));
        game.getSpace(4,1).setWorker(new Worker(player3));
        game.getSpace(3,1).setWorker(new Worker(player3));
        game.refreshLiteGame();

        cli.setLiteGame(game.getLiteGame().makeSerializable());
        cli.buildGameTable();
    }

}
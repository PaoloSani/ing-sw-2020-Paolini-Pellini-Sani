package it.polimi.ingsw.controller;

import it.polimi.ingsw.model;
import it.polimi.ingsw.model.Challenger;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;


public class Controller {
    private Model model;
    private Player player1;
    private Player player2;
    private Challenger challenger;


    public Controller(Model model) {
        this.model = model;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setChallenger(Challenger challenger) {
        this.challenger = challenger;
    }

    public void setPlayers( String name1, String name2, String name3 ){
        /*
        String chosen = "";
        int number;

        //codice che sceglie random il challenger
        // chosen = ...;
        // number = ;

        if ( name1 == null  || name2 == null || name3 == null  ){
            setChallenger( new Challenger( chosen ));
            setPlayer1( new Player( name1 ));

        }
        else {
            setChallenger(new Challenger(chosen));
            setPlayer1(new Player(name1));
            setPlayer2(new Player(name3));
        }
        */
    }

    public void chooseCards(){
        String chosenCards[];

        // il challenger sceglie le carte
        if ( this.player2 != null ) {
            for ( int i = 0; i < 3; i++ ) {
                //chosenCards[i] = getChoice();
            }

            this.player1.setGodName( chosenCards[1] );
            this.player2.setGodName( chosenCards[2] );
            this.challenger.setGodName( chosenCards[3] );

        }
        else {
            for ( int i = 0; i < 2; i++ ) {
                //chosenCards[i] = getChoice();
            }
        }


    }

    public void game(){
        boolean EndOfTheGame = false;
        Player currPlayer;
        Worker currWorker;
        //setPlayers(name1, name2, name3);
        chooseCards();

        while( !EndOfTheGame ){
            EndOfTheGame
        }

    }

    public boolean playTurn( Player player ){
        //currWorker = ...;

    }
}

package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Challenger;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.Observer;
import it.polimi.ingsw.virtualView.FrontEnd;
import it.polimi.ingsw.virtualView.PlayersInTheGame;


public class Server {
    private Game game;
    /*private Player player1;
    private Player player2;
    private Challenger challenger;*/


    public Server(Game game) {
        this.game = game;
    }



 /*   public void chooseCards(){
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
        //placeWorkers();

        /*while( !EndOfTheGame ){
            EndOfTheGame
        }

    }

    //il metodo playTurn effettua il flusso di gioco del turno di un giocatore. In base alla divinità il flusso può essere diverso.
    //Il tipo ritornato è boolean, perché la move ritorna true nel caso sia stata effettuata una mossa vincente.
    public boolean playTurn( Player player ){
        //Scegli il worker
        Worker currWorker = chooseWorker(player);

        switch ( player.getGodName() ) {

            /*case "Charon":        charonPower()
                                    move()
                                    build()
                                    break;*/

            //case "Prometheus":    2 flussi distinti

            /*case "Artemis":       move()
                                    control()
                                    move()
                                    build()
                                    break;*/

            /*case "Triton":        move()
                                    while (casella sul perimetro) move()
                                    build()
                                    break;*/

            /*case "Demeter":       move()
                                    build()
                                    control()
                                    build()
                                    break;*/

            /*case "Poseidon":      move()
                                    build()
                                    3 build() dell'altro worker a terra (facoltativo)
                                    break;*/

            /*case "Default":       move()
                                    build()
                                    break;


        }
    }

    public Worker chooseWorker ( Player player ){
        Worker result;

        //result = getChoiceFromView(player.getWorker1(),player.getWorker2());

        if ( model.isFreeToMove(result) ){
            return result;
        }
        else {
            if( result.equals(player.getWorker1())){
                return player1.getWorker2();
            }
            else return player.getWorker1();
        }

    }
    */
            @Override
            public void update(PlayersInTheGame message) {
                game.setPlayers(message.getNickname1(),message.getNickname2(),message.getNickname3());
            }

}

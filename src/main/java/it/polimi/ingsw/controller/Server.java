package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Challenger;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.util.GameState;


public class Server {
    private Game game;
    private GameState currState;
    private Player player2;
    private Player player3;
    private Challenger challenger;
    Player currPlayer;

    public Player getPlayer2() {
        return player3;
    }

    public Player getPlayer3() {
        return player3;
    }

    public Challenger getChallenger() {
        return challenger;
    }

    public Game getGame() {
        return game;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setPlayer3(Player player3) {
        this.player3 = player3;
    }

    public void setChallenger(Challenger challenger) {
        this.challenger = challenger;
    }

    public void setCurrState(GameState currState) {
        this.currState = currState;
    }

    //metodi per la connessione
    public void setConnection() {
    }

    public void runGame() {
        currState = new SettingPlayers(this);

        while ( !(currState instanceof PlacingWorkers) ) ;

        currPlayer = player2;

        while ( !(currState instanceof Winning) ) {
            currState.execute(currPlayer);
            currState.changeState();

            //TODO : gestire rimozione giocatore e caso due giocatori
            if ( currState instanceof Building ) {  //alla fine del turno del giocatore cambio il giocatore corrente
                if ( ( player2.equals(currPlayer) && player3 != null ) || ( challenger.equals(currPlayer) && player2 == null ) ) {
                    currPlayer = player3;
                } else if ( ( player3.equals(currPlayer) && challenger != null ) || ( player2.equals(currPlayer) && player3 == null ) ) {
                    currPlayer = challenger;
                } else if ( ( challenger.equals(currPlayer) && player2 != null ) || ( player3.equals(currPlayer) && challenger == null ) ) {
                    currPlayer = player2;
                }
            }
        }
    }

}


}


 /*private Player player1;
    private Player player2;
    private Challenger challenger;


    public Server(Game game) {
        this.game = game;
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
        //placeWorkers();

        while( !EndOfTheGame ){
            EndOfTheGame
        }

    }

    //il metodo playTurn effettua il flusso di gioco del turno di un giocatore. In base alla divinità il flusso può essere diverso.
    //Il tipo ritornato è boolean, perché la move ritorna true nel caso sia stata effettuata una mossa vincente.
    public boolean playTurn( Player player ){
        //Scegli il worker
        Worker currWorker = chooseWorker(player);

        switch ( player.getGodName() ) {

            case "Charon":        charonPower()
                                    move()
                                    build()
                                    break;

//case "Prometheus":    2 flussi distinti

            case "Artemis":       move()
                                    control()
                                    move()
                                    build()
                                    break;

            case "Triton":        move()
                                    while (casella sul perimetro) move()
                                    build()
                                    break;

            case "Demeter":       move()
                                    build()
                                    control()
                                    build()
                                    break;

            case "Poseidon":      move()
                                    build()
                                    3 build() dell'altro worker a terra (facoltativo)
                                    break;

    case "Default":       move()
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
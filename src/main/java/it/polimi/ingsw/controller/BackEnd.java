package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Challenger;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.util.GameState;


public class BackEnd {
    private Game game;
    private GameState currState;
    private Player player2;
    private Player player3;
    private Challenger challenger;
    private Player currPlayer;
    private Worker currWorker;
    public final GameState setPlayersState = new SetPlayersState(this);
    public final GameState placeWorkersState = new PlaceWorkersState(this);
    public final GameState chooseWorkerState = new ChooseWorkerState(this);
    public final GameState moveState = new MoveState(this);
    public final GameState buildState = new BuildState(this);
    public final GameState removePlayerState = new RemovePlayerState(this);
    public final GameState charonSwitchState = new CharonSwitchState(this);
    public final GameState prometheusBuildState = new PrometheusBuildState(this);
    public final GameState prometheusMoveState = new PrometheusMoveState(this);
    public final GameState winState = new WinState(this);
    public final GameState usePowerState = new UsePowerState(this);

    public BackEnd() {
        this.game = new Game();
        this.currState = setPlayersState;
}

    public Player getCurrPlayer() {
        return currPlayer;
    }

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

    public GameState getCurrState() {
        return currState;
    }
    public Worker getCurrWorker() {
        return currWorker;
    }

    public void setCurrWorker(Worker currWorker) {
        this.currWorker = currWorker;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
        //se player2 == null devo noticare il giocatore della sconfitta
    }

    public void setPlayer3(Player player3) {
        this.player3 = player3;
        //se player3 == null devo noticare il giocatore della sconfitta (sempre che ci fosse collegamento e quindi tre giocatori)
    }

    public void setChallenger(Challenger challenger) {
        this.challenger = challenger;
        //se challenger == null devo noticare il giocatore della sconfitta
    }

    public void setCurrState(GameState currState) {
        this.currState = currState;
    }

    public void runGame() {

        currPlayer = player2;

        while ( !(currState instanceof WinState) ) {

            if( currState == this.removePlayerState )
                removePlayerState.execute();

        }

        //notifico il giocatore che ha vinto

    }

    public void updateCurrPlayer(){
        if ( ( player2.equals(currPlayer) && player3 != null ) || ( challenger.equals(currPlayer) && player2 == null ) ) {
            currPlayer = player3;
        } else if ( ( player3.equals(currPlayer) && challenger != null ) || ( player2.equals(currPlayer) && player3 == null ) ) {
            currPlayer = challenger;
        } else if ( ( challenger.equals(currPlayer) && player2 != null ) || ( player3.equals(currPlayer) && challenger == null ) ) {
            currPlayer = player2;
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
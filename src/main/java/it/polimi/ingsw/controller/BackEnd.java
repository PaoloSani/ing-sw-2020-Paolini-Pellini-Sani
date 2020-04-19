package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.util.Observer;
import it.polimi.ingsw.virtualView.GameMessage;


public class BackEnd implements Observer<GameMessage> {
    private Game game;
    private GameMessage gameMessage;
    private GameState currState;
    private Player player2;
    private Player player3;
    private Player challenger;
    private Player currPlayer;
    private Player toRemove;
    private Worker currWorker;
    private boolean lastExecute;
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

    public BackEnd() {
        this.game = new Game();
        this.currState = setPlayersState;
}

    public GameMessage getGameMessage() {
        return gameMessage;
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

    public Player getChallenger() {
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

    public void setChallenger(Player challenger) {
        this.challenger = challenger;
        //se challenger == null devo noticare il giocatore della sconfitta
    }

    public void setCurrState(GameState currState) {
        this.currState = currState;
    }

    public void setToRemove(Player toRemove) {
        this.toRemove = toRemove;
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

    public void changeState(){
        if (setPlayersState == currState) {
            if (gameMessage.getSpace1()[0] != -1) {
                currState.reset();
                currState = placeWorkersState; //Cambio lo stato solo se x1 non è negativo (come di default all'inizio del gioco)
            }
        }

        else if (placeWorkersState == currState) {
            if (currPlayer == challenger) {
                currState.reset();
                currState = chooseWorkerState; //Il challenger è l'ultimo giocatore che sceglie
            }
            this.updateCurrPlayer();
        }

        else if (chooseWorkerState == currState) {
            if (toRemove == null) {
                if (currPlayer.getGod() == God.CHARON && gameMessage.isCharonSwitching()){
                    currState.reset();
                    currState = charonSwitchState;
                }
                else if (currPlayer.getGod() == God.PROMETHEUS && gameMessage.getLevel() != 0){
                    currState.reset();
                    currState = prometheusBuildState;
                }
                else {
                    currState.reset();
                    currState = moveState;
                }
            }
            else {
                updateCurrPlayer();
                currState.reset();
                currState = removePlayerState;
            }

        }


        else if (charonSwitchState == currState) {
            currState.reset();
            currState = moveState;
        }

        else if (prometheusBuildState == currState) {
            currState.reset();
            currState = prometheusMoveState;
        }

        else if (prometheusMoveState == currState) {
            currState.reset();
            currState = buildState;
        }

        else if (moveState == currState) {
            if (!((currPlayer.getGod() == God.ARTEMIS || currPlayer.getGod() == God.TRITON) && gameMessage.getLevel() == 0))
            {
                currState.reset();
                currState = buildState;      // Nel client controlleremo quando è il momento di costruire con una build
            }
        }

        else if (buildState == currState) {
            if (!((currPlayer.getGod() == God.HEPHAESTUS || currPlayer.getGod() == God.POSEIDON || currPlayer.getGod() == God.DEMETER) && gameMessage.getLevel() != 0)) {
                {
                    currState.reset();
                    updateCurrPlayer();
                    currState = chooseWorkerState;
                }
            }
        }

        else if (removePlayerState == currState) {

            if (lastPlayerInTheGame(currPlayer)) {
                currState.reset();
                currState = winState;
            } else {
                currState.reset();
                currState = chooseWorkerState;
            }
        }
    }

    public boolean lastPlayerInTheGame(Player lastPlayer){
        if (lastPlayer == challenger && player2 == null && player3 == null) return true;
        else if (lastPlayer == player2 &&  challenger == null && player3 == null) return true;
        else return (lastPlayer == player3 && challenger == null && player2 == null); //False se la valutazione è falsa
    }

    @Override
    public void update(GameMessage gameMessage){
        this.gameMessage = gameMessage;
        if( this.lastExecute ) this.changeState(); // Se la scorsa esecuzione è sata negativa non posso cambiare stato
        lastExecute = currState.execute();
    }


    public Player getToRemove() {
        return toRemove;
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
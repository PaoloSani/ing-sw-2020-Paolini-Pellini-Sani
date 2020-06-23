package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.util.Observer;
import it.polimi.ingsw.virtualView.GameMessage;

/**
 * Class in which the current state of the FSM implemented through the pattern state is decided
 */

public class BackEnd implements Observer<GameMessage> {
    private Game game;
    private GameMessage gameMessage;
    private GameState currState;
    private Player challenger;
    private Player player2;
    private Player player3;
    private Player currPlayer;
    private Player toRemove; //serve per memorizzare il giocatore che non può muovere nessuna pedina, così che poi lo rimuovo dal gioco
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
        this.lastExecute = true;
    }

    public GameMessage getGameMessage() {
        return gameMessage;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public Player getPlayer2() {
        return player2;
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

    public void setToRemove(Player toRemove) {
        this.toRemove = toRemove;
    }


    public void updateCurrPlayer(){
        if ( ( ( player2 == currPlayer) && (player3 != null ) ) || ( (challenger == currPlayer) && ( player2 == null ) ) ) {
            currPlayer = player3;
        } else if ( ( ( player3 == currPlayer) && ( challenger != null ) ) || ( ( player2 == currPlayer ) && ( player3 == null ) ) ) {
            currPlayer = challenger;
        } else if ( ( ( challenger == currPlayer ) && ( player2 != null ) ) || ( ( player3 ==currPlayer) && ( challenger == null ) ) ) {
            currPlayer = player2;
        }
        game.setCurrPlayer(currPlayer);
    }

    /**
     * Method in which the next state of the FSM is chosen and set
     */

    public void changeState(){
        if (setPlayersState == currState) {
            if ( (gameMessage.getSpace1()[0] != -1 ) || (gameMessage.getSpace1() == null) ) {
                currPlayer = this.player2;      //perché il primo è il challenger
                game.setCurrPlayer(player2);
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
            if ( toRemove == null ) {
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

        else if ( buildState == currState ) {
            if ( toRemove != null ){
                updateCurrPlayer();
                currState.reset();
                currState = removePlayerState;
            }
            else if ( !((currPlayer.getGod() == God.HEPHAESTUS || currPlayer.getGod() == God.POSEIDON || currPlayer.getGod() == God.DEMETER) && gameMessage.getLevel() != 0) ) {
                {
                    currState.reset();
                    updateCurrPlayer();
                    currState = chooseWorkerState;
                }
            }
        }

        else if (removePlayerState == currState) {

            if ( lastPlayerInTheGame(currPlayer) ) {
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

    /**
     * Method that is executed after a notification from the virtual view
     * @param gameMessage Message that is received from the virtual view about the next status of the machine
     */
    @Override
    public void update(GameMessage gameMessage){

        this.gameMessage = gameMessage;
        if( this.lastExecute ) this.changeState(); // Se la scorsa esecuzione è sata negativa non posso cambiare stato
        lastExecute = currState.execute();
    }

    /**
     * Method that returns the player to be removed
     */
    public Player getToRemove() {
        return toRemove;
    }

    //////////////////////////////////////////////////////////////
    /////////////////// Metodi per i Test ////////////////////////
    //////////////////////////////////////////////////////////////

    public void setState(GameState state) {
        currState = state;
    }

    public boolean getLastExecute() {
        return lastExecute;
    }

    public void setCurrPlayer(Player player) {
        currPlayer = player;
    }
}

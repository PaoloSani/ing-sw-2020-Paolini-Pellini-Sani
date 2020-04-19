package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;

public class RemovePlayerState implements GameState {
    private BackEnd backEnd;

    public RemovePlayerState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    @Override
    public void changeState(GameState nextState) {
        backEnd.setCurrState(nextState);
    }

    //chiamato dal server: rimuove i worker del giocatore corrente.
    @Override
    public void execute() {
        backEnd.getCurrPlayer().getWorker1().getSpace().setWorker(null);     //svuoto la cella dalla pedina del worker1
        backEnd.getCurrPlayer().getWorker1().setSpace(null);                 //il worker non è più associato a nessuna cella
        backEnd.getCurrPlayer().getWorker2().getSpace().setWorker(null);     //stesso per il worker2
        backEnd.getCurrPlayer().getWorker2().setSpace(null);

        if ( backEnd.getPlayer3() == null || backEnd.getPlayer2() == null || backEnd.getChallenger() == null ){
            backEnd.updateCurrPlayer();
            changeState(backEnd.winState);
        }
        else{
            //setto a null il giocatore che ha perso, nel Server mi occuperò di notificare tale giocatore
            if ( backEnd.getChallenger() == backEnd.getCurrPlayer() )
                backEnd.setChallenger(null);
            else if ( backEnd.getPlayer2() == backEnd.getCurrPlayer() )
                backEnd.setPlayer2(null);
            else backEnd.setPlayer3(null);

        }
        // Tolgo giocatore che ha perso dal litegame e poi chiamo la notify dal model per la virtual view
    }

    //questo stato non funziona tramite update!
}

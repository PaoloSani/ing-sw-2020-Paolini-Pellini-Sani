package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;

public class RemovePlayerState implements GameState {
    private BackEnd backEnd;

    public RemovePlayerState(BackEnd backEnd) {
        this.backEnd = backEnd;
    }

    //chiamato dal server: rimuove i worker del giocatore corrente.
    @Override
    public boolean execute() {
        backEnd.getToRemove().getWorker1().getSpace().setWorker(null);     //svuoto la cella dalla pedina del worker1
        backEnd.getToRemove().getWorker1().setSpace(null);            //il worker non è più associato a nessuna cella
        backEnd.getToRemove().getWorker2().getSpace().setWorker(null);     //stesso per il worker2
        backEnd.getToRemove().getWorker2().setSpace(null);

        //setto a null il giocatore che ha perso, nel Server mi occuperò di notificare tale giocatore
        if ( backEnd.getChallenger() == backEnd.getToRemove() )
            backEnd.setChallenger(null);
        else if ( backEnd.getPlayer2() == backEnd.getToRemove() )
            backEnd.setPlayer2(null);
        else backEnd.setPlayer3(null);

        backEnd.getGame().refreshLiteGame();        //Aggiorno il GameLite
        backEnd.getGame().getLiteGame().notify(backEnd.getGame().getLiteGame());   //Notifico la VView
        return true;
    }

    @Override
    public void reset() {
        backEnd.setToRemove(null);
    }

    //questo stato non funziona tramite update!
}

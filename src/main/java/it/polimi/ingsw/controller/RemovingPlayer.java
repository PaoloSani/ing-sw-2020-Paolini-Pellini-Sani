package it.polimi.ingsw.controller;

import it.polimi.ingsw.util.GameState;
import it.polimi.ingsw.virtualView.PlayersInTheGame;

public class RemovingPlayer implements GameState {
    private Server server;

    public RemovingPlayer(Server server) {
        this.server = server;
    }

    @Override
    public void changeState(GameState nextState) {
        server.setCurrState(nextState);
    }

    //chiamato dal server: rimuove i worker del giocatore corrente.
    @Override
    public void execute() {
        server.getCurrPlayer().getWorker1().getSpace().setWorker(null);     //svuoto la cella dalla pedina del worker1
        server.getCurrPlayer().getWorker1().setSpace(null);                 //il worker non è più associato a nessuna cella
        server.getCurrPlayer().getWorker2().getSpace().setWorker(null);     //stesso per il worker2
        server.getCurrPlayer().getWorker2().setSpace(null);

        if ( server.getPlayer3() == null || server.getPlayer2() == null || server.getChallenger() == null ){
            server.updateCurrPlayer();
            changeState(server.winning);
        }
        else{
            //setto a null il giocatore che ha perso, nel Server mi occuperò di notificare tale giocatore
            if ( server.getChallenger() == server.getCurrPlayer() )
                server.setChallenger(null);
            else if ( server.getPlayer2() == server.getCurrPlayer() )
                server.setPlayer2(null);
            else server.setPlayer3(null);

            server.updateCurrPlayer();
            changeState(server.choosingWorkers);
        }

    }

    //questo stato non funziona tramite update!
}

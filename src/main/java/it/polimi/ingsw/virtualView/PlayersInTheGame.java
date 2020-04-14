package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.util.Observable;

public class PlayersInTheGame extends Observable {
    String name1;       // Challenger: sceglie le carte e gioca per ultimo
    String name2;       // Start Player: giocatore che gioca per primo il turno e pesca per primo la carta
    String name3;

    God god1;
    God god2;
    God god3;


}

package it.polimi.ingsw.model;

public class MoveMinotaur implements Move {
    /**
     * @param worker Worker performing the move
     * @param nextSpace Space where you want to move
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean execute( Worker worker, Space nextSpace ) {
        boolean result = true;
        int currH = worker.getSpace().getHeight();
        Game myGame = worker.getPlayer().getGame();


        // controllo il contenuto di nextSpace
        if (    myGame.invalidMoveSpace(nextSpace, worker.getSpace())                 ||     //la prossima cella è quella corrente
                nextSpace.isDomed()                                                   ||     //la prossima cella è una cupola
                //se Athena è true controllo che non si possa salire
                (worker.getPlayer().getGame().getConstraint().athenaBlocks() && (nextSpace.getHeight() - currH == 1)))

            result = false;

        //controllo condizione di vittoria prima di aggiornare le celle
        worker.getPlayer().isWinner(worker.getSpace(), nextSpace);


        //aggiorno la posizione del worker e le space precedente e corrente nella table
        if( result && nextSpace.getWorker() != null ){
            //sintassi metodo statico, l'ho reso statico cosi posso chiamarlo senza avere un'istanza model qui dentro, se la
            //cella in cui mi voglio spostare è occupata da un giocatore chiamo funzione minotaurPower che spinge il worker
            result =  myGame.minotaurPower(worker, nextSpace.getWorker());
        }

        if ( result ) {
            worker.getSpace().setWorker(null);      //setto il worker della space precedente a null (la svuoto)
            worker.setSpace(nextSpace);             //setto attributo space del worker con la space successiva e
                                                    //setto attributo worker nella nuova space con il valore del mio worker
        }
        return result;
    }
}

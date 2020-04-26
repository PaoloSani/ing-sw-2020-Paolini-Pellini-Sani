package it.polimi.ingsw.model;

public class MoveDefault implements Move {

    public boolean execute( Worker worker, Space nextSpace ) {
        boolean result = true;
        int currH = worker.getSpace().getHeight();

        //reset del boolean Athena nella classe costraint
        if( worker.getPlayer().getGod() == God.ATHENA ){
            worker.getPlayer().getGame().getConstraint().setAthena(false);
        }

        // controllo il contenuto di nextSpace
        if (    worker.getPlayer().getGame().invalidMoveSpace(nextSpace,worker.getSpace()) ||     //la prossima cella è quella corrente
                nextSpace.getWorker() != null                                         ||     //la prossima cella è occupata
                nextSpace.isDomed()                                                   ||     //la prossima cella è una cupola
                //se Athena è true controllo che non si possa salire
                (worker.getPlayer().getGame().getConstraint().athenaBlocks() && (nextSpace.getHeight() - currH == 1)))

            result = false;

        //controllo condizione di vittoria prima di aggiornare le celle
        worker.getPlayer().isWinner(worker.getSpace(), nextSpace);

        if ( result ) {
            //se sono atena e sono salito setto il flag atena a true
            if (worker.getPlayer().getGod() == God.ATHENA && (nextSpace.getHeight() - currH == 1)) {
                worker.getPlayer().getGame().getConstraint().setAthena(true);
            }

            //aggiorno la posizione del worker e le space precedente e corrente nella table
            worker.getSpace().setWorker(null);      //setto il worker della space precedente a null (la svuoto)
            worker.setSpace(nextSpace);             //setto attributo space del worker con la space successiva e
                                                    //setto attributo worker nella nuova space con il valore del mio worker
        }
        return result;
    }
}

package it.polimi.ingsw.model;

public class MoveDefault implements Move {

    public boolean execute( Worker worker, Space nextSpace ) throws IllegalSpaceException {
        int currX,currY,currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();
        boolean result;


        // controllo il contenuto di nextSpace
        if ( ( currX - nextSpace.getX() ) > 1 || ( currX - nextSpace.getX() ) < -1    ||     //riga non valida
                ( currY - nextSpace.getY() ) > 1 || ( currY - nextSpace.getY() ) < -1 ||     //colonna non valida
                nextSpace.getHeight() - currH <= 1                                    ||     //sale più di un livello
                currX == nextSpace.getX() && currY == nextSpace.getY()                ||     //la prossima cella è quella corrente
                nextSpace.getWorker() != null                                         ||     //la prossima cella è occupata
                nextSpace.getHeight() == 4                                            ||     //la prossima cella è una cupola
                (worker.getPlayer().getModel().getConstraint().athenaBlocks() && (nextSpace.getHeight() - currH == 1)))

            throw new IllegalSpaceException( "Space not accepted!" );

        result = worker.getPlayer().isWinner(worker.getSpace(), nextSpace);

        //aggiorno la posizione del worker e le space precedente e corrente nella table
        worker.getSpace().setWorker( null );      //setto il worker della space precedente a null (la svuoto)
        worker.setSpace( nextSpace );             //setto attributo space del worker con la space successiva
        worker.getSpace().setWorker( worker );    //setto attributo worker nella nuova space con il valore del mio worker

        return result;
    }
}

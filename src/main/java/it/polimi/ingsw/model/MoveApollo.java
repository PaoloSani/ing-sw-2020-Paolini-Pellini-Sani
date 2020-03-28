package it.polimi.ingsw.model;

public class MoveApollo implements Move {
    public boolean execute(Worker worker, Space nextSpace) throws IllegalSpaceException{
        int currX,currY,currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();
        boolean result;
        Worker oppWorker;

        //reset del boolean Athena nella classe costraint
        if( worker.getPlayer().getGodName() == "Athena" ){
            worker.getPlayer().getModel().getConstraint().setAthena(false);
        }

        // controllo il contenuto di nextSpace
        if (    nextSpace.getX() > 4 || nextSpace.getX() < 0                          ||
                nextSpace.getY() > 4 || nextSpace.getY() < 0                          ||     //space non appartenente alla tabella
                ( currX - nextSpace.getX() ) > 1 || ( currX - nextSpace.getX() ) < -1 ||     //riga non valida
                ( currY - nextSpace.getY() ) > 1 || ( currY - nextSpace.getY() ) < -1 ||     //colonna non valida
                nextSpace.getHeight() - currH > 1                                     ||     //sale più di un livello
                currX == nextSpace.getX() && currY == nextSpace.getY()                ||     //la prossima cella è quella corrente
                (nextSpace.getWorker() != null &&
                nextSpace.getWorker().getPlayer().equals(worker.getPlayer()))         ||     //la prossima cella è occupata da un worker alleato
                nextSpace.getHeight() == 4                                            ||     //la prossima cella è una cupola

                //se Athena è true controllo che non si possa salire
                (worker.getPlayer().getModel().getConstraint().athenaBlocks() && (nextSpace.getHeight() - currH == 1)))

            throw new IllegalSpaceException( "Space not accepted!" );

        //controllo condizione di vittoria prima di aggiornare le celle
        result = worker.getPlayer().isWinner(worker.getSpace(), nextSpace);

        if( nextSpace.getWorker() == null ) {                     //aggiorno la posizione del worker e le space precedente e corrente nella table
            worker.getSpace().setWorker(null);                    //setto il worker della space precedente a null (la svuoto)
            worker.setSpace(nextSpace);                           //setto attributo space del worker con la space successiva
            worker.getSpace().setWorker(worker);                  //setto attributo worker nella nuova space con il valore del mio worker
        }


        else{
            oppWorker = nextSpace.getWorker();        //memorizzo il worker avversario
            worker.getSpace().setWorker(oppWorker);   //Sposto nella mia cella il worker avversario
            oppWorker.setSpace(worker.getSpace());    //La nuova cella del worker avversario è la mia cella
            worker.setSpace(nextSpace);               //La mia nuova cella è la cella che aveva il worker avversario
            worker.getSpace().setWorker(worker);      //La cella dove io sono mi contieneèx
        }

        return result;
    }
}

package it.polimi.ingsw.model;

public class MoveMinotaur implements Move {
    public boolean execute( Worker worker, Space nextSpace ) throws IllegalSpaceException {
        int currX,currY,currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();
        boolean result;

        //reset del boolean Athena nella classe costraint
        if( worker.getPlayer().getGodName() == "Athena" ){
            worker.getPlayer().getModel().getConstraint().setAthena(false);
        }

        // controllo il contenuto di nextSpace
        if ( ( currX - nextSpace.getX() ) > 1 || ( currX - nextSpace.getX() ) < -1    ||     //riga non valida
                ( currY - nextSpace.getY() ) > 1 || ( currY - nextSpace.getY() ) < -1 ||     //colonna non valida
                nextSpace.getHeight() - currH <= 1                                    ||     //sale più di un livello
                currX == nextSpace.getX() && currY == nextSpace.getY()                ||     //la prossima cella è quella corrente
                //non mi interessa perché sposto muratore
                //nextSpace.getWorker() != null                                         ||     //la prossima cella è occupata
                nextSpace.getHeight() == 4                                            ||     //la prossima cella è una cupola
                //se Athena è true controllo che non si possa salire
                (worker.getPlayer().getModel().getConstraint().athenaBlocks() && (nextSpace.getHeight() - currH == 1)))

            throw new IllegalSpaceException( "Space not accepted!" );

        //controllo condizione di vittoria prima di aggiornare le celle
        result = worker.getPlayer().isWinner(worker.getSpace(), nextSpace);

        //se sono atena e sono salito setto il flag atena a true (CHIEDI SE SONO IL MINOTAURO NON POSSO ESSERE ATENA CODICE INUTILE?)
        //IN CASO AFFERMATIVO ELIMINARRE FRAMMENTO CODICE ANCHE IN MOVEAPOLLO
        if( worker.getPlayer().getGodName() == "Athena" && ( nextSpace.getHeight() - currH == 1 ) ){
            worker.getPlayer().getModel().getConstraint().setAthena(true);
        }

        //aggiorno la posizione del worker e le space precedente e corrente nella table
        if(nextSpace.getWorker() != null){
            //sintassi metodo statico, l'ho reso statico cosi posso chiamarlo senza avere un'istanza model qui dentro, se la
            //cella in cui mi voglio spostare è occupata da un giocatore chiamo funzione minotaurPower che spinge il worker
            try{
                Model.minotaurPower(worker, nextSpace.getWorker(),worker.getPlayer().getModel());
            }
            catch (IllegalSpaceException i){
                System.out.println("Input error!");
            }
        }
        worker.getSpace().setWorker( null );      //setto il worker della space precedente a null (la svuoto)
        worker.setSpace( nextSpace );             //setto attributo space del worker con la space successiva
        worker.getSpace().setWorker( worker );    //setto attributo worker nella nuova space con il valore del mio worker

        return result;
    }
}

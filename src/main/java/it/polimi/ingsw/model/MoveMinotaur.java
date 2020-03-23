package it.polimi.ingsw.model;

public class MoveMinotaur implements Move {
    public boolean execute(Worker worker, Space nextSpace) throws IllegalSpaceException{
        int currX,currY,currH,nextX,nextY;
        Player currP;
        Model currM;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();
        nextX = nextSpace.getX();
        nextY = nextSpace.getY();
        currP = worker.getPlayer();
        currM = currP.getModel();

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
                //Non posso spingere worker avversario indietro
                nextSpace.getWorker() != null                                         &&
                currX < 4 && currX > 0 && currY < 4 && currY > 0                      &&
                ( nextX == 0 || nextX == 4 || nextY == 0 || nextY == 4 )              ||
                //Worker che voglio muovere si trova in uno dei quattro angoli
                ( nextX == 4 && nextY ==4) || ( nextX == 4 && nextY ==4)              ||
                ( nextX == 0 && nextY ==4) || ( nextX == 4 && nextY ==0)              ||
                //Controllo sull'altezza
                nextSpace.getHeight() == 4                                            ||     //la prossima cella è una cupola
                //se Athena è true controllo che non si possa salire
                (worker.getPlayer().getModel().getConstraint().athenaBlocks() && (nextSpace.getHeight() - currH == 1)))

            throw new IllegalSpaceException( "Space not accepted!" );

        //controllo condizione di vittoria prima di aggiornare le celle
        result = worker.getPlayer().isWinner(worker.getSpace(), nextSpace);

        //Ho eliminato il caso in cui sono atena

        //aggiorno la posizione del worker e le space precedente e corrente nella table
        if(nextSpace.getWorker() != null){
            if( currX ==  nextX ){
                if( nextX > currX){
                        currP.moveWorker( nextSpace.getWorker(),currM.)
                }
            else{

                }
            }
        }


        worker.getSpace().setWorker( null );      //setto il worker della space precedente a null (la svuoto)
        worker.setSpace( nextSpace );             //setto attributo space del worker con la space successiva
        worker.getSpace().setWorker( worker );    //setto attributo worker nella nuova space con il valore del mio worker

        return result;
    }

}

package it.polimi.ingsw.model;

public class MoveMinotaur implements Move {
    public boolean execute( Worker worker, Space nextSpace ) {
        boolean result = true;
        int currX,currY,currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();
        Game myGame = worker.getPlayer().getGame();

        //reset del boolean Athena nella classe costraint
        if( worker.getPlayer().getGod() == God.ATHENA ){
            worker.getPlayer().getGame().getConstraint().setAthena(false);
        }

        // controllo il contenuto di nextSpace
        if (    nextSpace.getX() > 4 || nextSpace.getX() < 0                          ||
                nextSpace.getY() > 4 || nextSpace.getY() < 0                          ||     //space non appartenente alla tabella
                ( currX - nextSpace.getX() ) > 1 || ( currX - nextSpace.getX() ) < -1 ||     //riga non valida
                ( currY - nextSpace.getY() ) > 1 || ( currY - nextSpace.getY() ) < -1 ||     //colonna non valida
                nextSpace.getHeight() - currH > 1                                     ||     //sale più di un livello
                currX == nextSpace.getX() && currY == nextSpace.getY()                ||     //la prossima cella è quella corrente
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
            worker.setSpace(nextSpace);             //setto attributo space del worker con la space successiva
            worker.getSpace().setWorker(worker);    //setto attributo worker nella nuova space con il valore del mio worker
        }
        return result;
    }
}

package it.polimi.ingsw.model;

public class Model {
    private int level1;
    private int level2;
    private int level3;
    private int dome;
    private final static int constLevel1 = 22;
    private final static int constLevel2 = 18;
    private final static int constLevel3 = 14;
    private final static int constDome = 18;
    private Space[][] table;
    private final static int numMaxPlayers = 3;
    private God[] deck;


    public Model() {
        this.level1 = constLevel1;
        this.level2 = constLevel2;
        this.level3 = constLevel3;
        this.dome = constDome;
        this.setUpTable();
        this.setUpDeck();

    }

    public void setUpTable(){
        for ( int i = 0; i < 5; i++ ){

            for ( int j = 0; j < 5; j++ ){

                this.table[i][j] = new Space( i, j );
            }
        }
    }

    private void setUpDeck() {
    }

    private boolean isFreeToMove( Worker worker ){
        int currX,currY,currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();

        if( ( 0 <= currX && currX <=4 ) && ( 0 <= currY && currY <= 4 )){
            for( int i = currX - 1; i < currX + 2; i++ ){
                for( int j= currY - 1 ; j < currY + 2; j++ ){
                    if( !( i == currX && j == currY ) && ( i >= 0 && i <= 4 && j >= 0 && j <= 4 ) && table[i][j].getHeight() < 4 ){
                        if( table[i][j].getWorker() == null && ( table[i][j].getHeight() - currH <= 1 ) ) return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isFreeToBuild( Worker worker ){
        int currX,currY;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();

        if( ( 0 <= currX && currX <= 4 ) && ( 0 <= currY && currY <= 4 ) ){
            for( int i = currX - 1; i < currX + 2; i++ ){
                for( int j = currY - 1; j < currY + 2; j++ ){
                    if( !( i == currX && j == currY ) && ( i >= 0 && i <= 4 && j >= 0 && j <= 4 ) ){
                        if( table[i][j].getWorker() == null && ( table[i][j].getHeight() < 4 ) ) return true;
                    }
                }
            }
        }
        return false;
    }

    //la chiamata è eseguita a seguito della chiamata di isFreeToMove dal controller, quindi worker si può muovere

    private void moveWorker( Worker worker, Space nextSpace ) throws IllegalSpaceException {
        int currX,currY,currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();


        // controllo il contenuto di nextSpace
        if ( ( currX - nextSpace.getX() ) > 1 || ( currX - nextSpace.getX() ) < -1 ||     //riga non valida
             ( currY - nextSpace.getY() ) > 1 || ( currY - nextSpace.getY() ) < -1 ||     //colonna non valida
             nextSpace.getHeight() - currH <= 1                                    ||     //sale più di un livello
             currX == nextSpace.getX() && currY == nextSpace.getY()                ||     //la prossima cella è quella corrente
             nextSpace.getWorker() != null                                         ||     //la prossima cella è occupata
             nextSpace.getHeight() == 4                                             )     //la prossima cella è una cupola

            throw new IllegalSpaceException( "Space not accepted!" );

        //mossa vincente chiamata a notifywinning
        //if( nextSpace.getHeight() == 3 && currH == 2 ) ....

        //aggiorno la posizione del worker e le space precedente e corrente nella table
        worker.getSpace().setWorker( null );      //setto il worker della space precedente a null (la svuoto)
        worker.setSpace( nextSpace );             //setto attributo space del worker con la space successiva
        worker.getSpace().setWorker( worker );    //setto attributo worker nella nuova space con il valore del mio worker


    }

    private void buildSpace( Worker worker, Space space ) throws IllegalSpaceException {
        int currX,currY, newH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        newH = space.getHeight() + 1;

        //controllo se la space è valida
        if ( ( currX - space.getX() ) > 1 || ( currX - space.getX() ) < -1  ||     //riga non valida
             ( currY - space.getY() ) > 1 || ( currY - space.getY() ) < -1  ||     //colonna non valida
             currX == space.getX() && currY == space.getY()                 ||     //si costruisce sotto di sé
             space.getWorker() != null                                      ||     //la cella è occupata da un worker
             space.getHeight() == 4                                          )     //nella cella è già presente una cupola

            throw new IllegalSpaceException( "Space not accepted!" );

        //controllo l'altezza richiesta
        switch ( newH ) {

            case 1 :
                if( this.level1 > 0 ){                                             //controllo che il pezzo corrispondente sia disponibile
                    this.level1--;                                                 //decremento i pezzi del livello disponibili
                    space.setHeight( newH );                                       //setto la nuova altezza dello space
                }
                else throw new IllegalSpaceException( "Space not accepted!" );     // se il pezzo non è disponibile lancio l'eccezione per la costruzione in quella cella
                break;

            case 2 :
                if( this.level2 > 0 ){
                    this.level2--;
                    space.setHeight( newH );
                }
                else throw new IllegalSpaceException( "Space not accepted!" );
                break;

            case 3 :
                if( this.level3 > 0 ){
                    this.level3--;
                    space.setHeight( newH );
                }
                else throw new IllegalSpaceException( "Space not accepted!" );
                break;

            case 4 :
                if( this.dome > 0 ){
                    this.dome--;
                    space.setHeight( newH );
                }
                else throw new IllegalSpaceException( "Space not accepted!" );
                break;

            default: throw new IllegalSpaceException( "Space not accepted!" );

        }

    }

}

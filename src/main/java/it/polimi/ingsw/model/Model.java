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
        for ( int i=0; i<5; i++ ){

            for ( int j=0; j<5; j++ ){

                this.table[i][j] = new Space( i, j );
            }
        }
    }

    private void setUpDeck() {
    }

    private boolean isFreeToMove( Worker worker ){
        int cX,cY,cH;
        cX = worker.getSpace().getX();
        cY = worker.getSpace().getY();
        cH = worker.getSpace().getHeight();

        if( ( 0<=cX && cX<=4 ) && ( 0<=cY && cY<=4 )){
            for( int i = cX-1; i< cX+2; i++ ){
                for( int j= cY-1; j< cY+2; j++ ){
                    if( !( i==cX && j==cY ) && ( i>=0 && i<=4 && j>=0 && j<=4 ) && table[i][j].getHeight() < 4 ){
                        if( table[i][j].getWorker()==null && ( table[i][j].getHeight()-cH<=1 ) ) return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isFreeToBuild( Worker worker ){
        int cX,cY;
        cX = worker.getSpace().getX();
        cY = worker.getSpace().getY();

        if( ( 0<=cX && cX<=4 ) && ( 0<=cY && cY<=4 ) ){
            for( int i = cX-1; i< cX+2; i++ ){
                for( int j= cY-1; j< cY+2; j++ ){
                    if( !( i==cX && j==cY ) && ( i>=0 && i<=4 && j>=0 && j<=4 ) ){
                        if( table[i][j].getWorker()==null && ( table[i][j].getHeight()<4 ) ) return true;
                    }
                }
            }
        }
        return false;
    }

    //la chiamata è eseguita a seguito della chiamata di isFreeToMove dal controller, quindi worker si può muovere

    private void moveWorker( Worker worker, Space space ) throws IllegalSpaceException{
        int cX,cY,cH, cSpace;
        cX = worker.getSpace().getX();
        cY = worker.getSpace().getY();
        cH = worker.getSpace().getHeight();


        // controllo il contenuto di space
        if ( ( cX - space.getX() ) > 1 || ( cX - space.getX() ) < -1 ||     //riga valida
             ( cY - space.getY() ) > 1 || ( cY - space.getY() ) < -1 ||     //colonna valida
             space.getHeight()-cH <= 1                               ||     //sale al massimo di un livello
             cX == space.getX() && cY == space.getY()                ||     //la prossima cella non è quella corrente
             space.getWorker() != null                               ||     //la prossima cella non è occupata
             space.getHeight() == 4                                   )   //la prossima cella non è una cupola

            throw new IllegalSpaceException("Space not accepted!");

        //mossa vincente chiamata a notifywinning
        //if(space.getHeight()==3 && cH == 2) ....

        //aggiorno la posizione del worker e le space precedente e corrente nella table
        worker.getSpace().setWorker( null );      //setto il worker della space precedente a null (la svuoto)
        worker.setSpace(space);                 //setto attributo space del worker con la space successiva
        worker.getSpace().setWorker( worker );    //setto attributo worker nella nuova space con il valore del mio worker


    }

    private void buildSpace( Worker worker, Space space ) throws IllegalSpaceException {
        int cX,cY,cH, nH;
        cX = worker.getSpace().getX();
        cY = worker.getSpace().getY();
        nH = space.getHeight() + 1;

        //controllo se la space è valida
        if ( ( cX - space.getX() ) > 1 || ( cX - space.getX() ) < -1 ||     //riga valida
             ( cY - space.getY() ) > 1 || ( cY - space.getY() ) < -1 ||     //colonna valida
             cX == space.getX() && cY == space.getY()                ||     //non si costruisce sotto di sé
             space.getWorker() != null                               ||     //la cella non è occupata da un worker
             space.getHeight() == 4                                   )     //nella cella non è già presente una cupola

            throw new IllegalSpaceException("Space not accepted!");

        //controllo l'altezza richiesta
        switch ( nH ) {

            case 1 :
                if( this.level1 > 0 ){                                          //controllo che il pezzo corrispondente sia disponibile
                    this.level1--;                                              //decremento i pezzi del livello disponibili
                    space.setHeight( nH );                                      //setto la nuova altezza dello space
                }
                else throw new IllegalSpaceException( "Space not accepted!" );  // se il pezzo non è disponibile lancio l'eccezione per la costruzione in quella cella
                break;

            case 2 :
                if( this.level2 > 0 ){
                    this.level2--;
                    space.setHeight( nH );
                }
                else throw new IllegalSpaceException( "Space not accepted!" );
                break;

            case 3 :
                if( this.level3 > 0 ){
                    this.level3--;
                    space.setHeight( nH );
                }
                else throw new IllegalSpaceException( "Space not accepted!" );
                break;

            case 4 :
                if( this.dome > 0 ){
                    this.dome--;
                    space.setHeight( nH );
                }
                else throw new IllegalSpaceException( "Space not accepted!" );
                break;

            default: throw new IllegalSpaceException( "Space not accepted!" );

        }

    }

}

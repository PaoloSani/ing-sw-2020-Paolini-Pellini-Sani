package it.polimi.ingsw.model;

public class BuildDefault implements Build {
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

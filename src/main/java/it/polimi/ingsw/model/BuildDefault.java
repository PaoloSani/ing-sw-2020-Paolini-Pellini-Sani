package it.polimi.ingsw.model;

public class BuildDefault implements Build {

    public void execute( Worker worker, Space space, int level ) throws IllegalSpaceException {
        int currX,currY, newH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        newH = space.getHeight() + 1;

        //controllo se la space è valida
        if (    space.getX() > 4 || space.getX() < 0                           ||
                space.getY() > 4 || space.getY() < 0                           ||     //space non appartenente alla tabella
                ( currX - space.getX() ) > 1 || ( currX - space.getX() ) < -1  ||     //riga non valida
                ( currY - space.getY() ) > 1 || ( currY - space.getY() ) < -1  ||     //colonna non valida
                currX == space.getX() && currY == space.getY()                 ||     //si costruisce sotto di sé
                space.getWorker() != null                                      ||     //la cella è occupata da un worker
                space.isDomed()                                                ||     //nella cella è già presente una cupola
                newH != level                                                   )     //Controlla che l'altezza del livello da costruire sia giusto

            throw new IllegalSpaceException( "Space not accepted!" );

        //controllo l'altezza richiesta
        switch ( newH ) {

            case 1 :
                // level>0
                if( worker.getPlayer().getModel().getLevel1() > 0 ){                       //controllo che il pezzo corrispondente sia disponibile
                    //decremento i pezzi del livello disponibili, level1 --
                    worker.getPlayer().getModel().setLevel1( worker.getPlayer().getModel().getLevel1() - 1 );
                    space.setHeight( newH );                                          //setto la nuova altezza dello space
                }
                else throw new IllegalSpaceException( "Space not accepted!" );        // se il pezzo non è disponibile lancio l'eccezione per la costruzione in quella cella
                break;

            case 2 :
                if( worker.getPlayer().getModel().getLevel2() > 0 ){
                    worker.getPlayer().getModel().setLevel2( worker.getPlayer().getModel().getLevel2() - 1 );
                    space.setHeight( newH );
                }
                else throw new IllegalSpaceException( "Space not accepted!" );
                break;

            case 3 :
                if( worker.getPlayer().getModel().getLevel3() > 0 ){
                    worker.getPlayer().getModel().setLevel3( worker.getPlayer().getModel().getLevel3() - 1 );
                    space.setHeight( newH );
                }
                else throw new IllegalSpaceException( "Space not accepted!" );
                break;

            case 4 :
                if( worker.getPlayer().getModel().getDome() > 0 ){
                    worker.getPlayer().getModel().setDome( worker.getPlayer().getModel().getDome() - 1 );
                    space.setHeight( newH );
                }
                else throw new IllegalSpaceException( "Space not accepted!" );
                break;

            default: throw new IllegalSpaceException( "Space not accepted!" );

        }

    }



}

package it.polimi.ingsw.model;

public class BuildZeus implements Build {
    public boolean execute( Worker worker, Space space, int level ) {
        boolean result = true;
        int currX,currY, newH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        newH = space.getHeight() + 1;

        //controllo se la space è valida
        if (    space.getX() > 4 || space.getX() < 0                           ||
                space.getY() > 4 || space.getY() < 0                           ||     //space non appartenente alla tabella
                ( currX - space.getX() ) > 1 || ( currX - space.getX() ) < -1  ||     //riga non valida
                ( currY - space.getY() ) > 1 || ( currY - space.getY() ) < -1  ||     //colonna non valida
                //currX == space.getX() && currY == space.getY()               ||     può costruisce sotto di sé
                (space.getWorker() != null  && space.getWorker() != worker)    ||     //la cella è occupata da un worker
                space.isDomed()                                                ||     //nella cella è già presente una cupola
                newH != level                                                   )     //Controlla che l'altezza del livello da costruire sia giusto

            result = false;

        //controllo l'altezza richiesta
        if ( result ) {
            switch (newH) {

                case 1:
                    // level>0
                    if (worker.getPlayer().getGame().getLevel1() > 0) {                       //controllo che il pezzo corrispondente sia disponibile
                        //decremento i pezzi del livello disponibili, level1 --
                        worker.getPlayer().getGame().setLevel1(worker.getPlayer().getGame().getLevel1() - 1);
                        space.setHeight(newH);                                          //setto la nuova altezza dello space
                    } else
                        result = false;        // se il pezzo non è disponibile lancio l'eccezione per la costruzione in quella cella
                    break;

                case 2:
                    if (worker.getPlayer().getGame().getLevel2() > 0) {
                        worker.getPlayer().getGame().setLevel2(worker.getPlayer().getGame().getLevel2() - 1);
                        space.setHeight(newH);
                    } else result = false;
                    break;

                case 3:
                    if (worker.getPlayer().getGame().getLevel3() > 0) {
                        worker.getPlayer().getGame().setLevel3(worker.getPlayer().getGame().getLevel3() - 1);
                        space.setHeight(newH);
                    } else result = false;
                    break;

                case 4:
                    if (worker.getPlayer().getGame().getDome() > 0) {
                        worker.getPlayer().getGame().setDome(worker.getPlayer().getGame().getDome() - 1);
                        space.setHeight(newH);
                    } else result = false;
                    break;

                default:
                    result = false;

            }
        }
        return result;
    }




}


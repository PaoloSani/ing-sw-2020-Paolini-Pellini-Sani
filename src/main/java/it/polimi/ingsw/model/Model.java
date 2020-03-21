package it.polimi.ingsw.model;

public class Model {
    private int level1 ;
    private int level2 ;
    private int level3 ;
    private int dome ;
    private final static int cLevel1 = 22;
    private final static int cLevel2 = 18;
    private final static int cLevel3 = 14;
    private final static int cDome = 18;
    private Space[][] matrix;
    private final static int numMaxPlayers = 3;
    private God[] deck;


    public Model() {
        this.level1 = cLevel1;
        this.level2 = cLevel2;
        this.level3 = cLevel3;
        this.dome = cDome;
        this.setUpMatrix();
        this.setUpDeck();

    }

    public void setUpMatrix(){
        for (int i=0; i<5; i++){

            for (int j=0; j<5; j++){

                this.matrix[i][j] = new Space(i,j);
            }
        }
    }

    private void setUpDeck() {
    }

    private boolean isFreeToMove(Worker worker){
        int cX,cY,cH;
        cX = worker.getSpace().getX();
        cY = worker.getSpace().getY();
        cH = worker.getSpace().getHeight();

        if( (0<=cX && cX<=4) && (0<=cY && cY<=4 )){
            for(int i = cX-1; i< cX+2; i++ ){
                for(int j= cY-1; j< cY+2; j++ ){
                    if(!(i==cX && j==cY) && (i>=0 && i<=4 && j>=0 && j<=4) && matrix[i][j].getHeight() < 4 ){
                        if( matrix[i][j].getWorker()==null && (matrix[i][j].getHeight()-cH<=1)) return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isFreeToBuild(Worker worker){
        int cX,cY,cH;
        cX = worker.getSpace().getX();
        cY = worker.getSpace().getY();
        cH = worker.getSpace().getHeight();

        if( (0<=cX && cX<=4) && (0<=cY && cY<=4 )){
            for(int i = cX-1; i< cX+2; i++ ){
                for(int j= cY-1; j< cY+2; j++ ){
                    if(!(i==cX && j==cY) && (i>=0 && i<=4 && j>=0 && j<=4) ){
                        if( matrix[i][j].getWorker()==null && (matrix[i][j].getHeight()<4)) return true;
                    }
                }
            }
        }
        return false;
    }

    //la chiamata è eseguita a seguito della chiamata di isFreeToMove dal controller, quindi worker si può muovere

    private void moveWorker(Worker worker, Space space) throws IllegalSpaceException{
        int cX,cY,cH, cSpace;
        cX = worker.getSpace().getX();
        cY = worker.getSpace().getY();
        cH = worker.getSpace().getHeight();
        // controllo il contenuto di space


        if ( (cX - space.getX()) > 1 || (cX - space.getX()) < -1 ||
             (cY - space.getY()) > 1 || (cY- space.getY()) < -1  ||
             ( space.getHeight()-cH <= 1 )                       ||
             cX == space.getX() && cY == space.getY()            ||
             space.getWorker() != null                           ||
             space.getHeight() == 4)
            throw new IllegalSpaceException("Space not accepted!");

        //mossa vincente chiamata a notifywinning
        //if(space.getHeight()==3 && cH == 2) ....

        worker.getSpace().setWorker(null);      //setto il worker della space precedente a null (la svuoto)
        worker.setSpace(space);                 //setto attributo space del worker con la space successiva
        worker.getSpace().setWorker(worker);    //setto attributo worker nella nuova space con il valore del mio worker
    }

    private void buildSpace(Worker worker, Space space) throws IllegalSpaceException{
        int cX,cY,cH, nH;
        cX = worker.getSpace().getX();
        cY = worker.getSpace().getY();
        cH = worker.getSpace().getHeight();
        nH = space.getHeight() + 1;


        if ( (cX - space.getX()) > 1 || (cX - space.getX()) < -1 ||
             (cY - space.getY()) > 1 || (cY- space.getY()) < -1  ||
             cX == space.getX() && cY == space.getY()            ||
             space.getWorker() != null                           ||
             space.getHeight() == 4)

            throw new IllegalSpaceException("Space not accepted!");
        switch (nH){
            case 1 :
                if(this.level1 > 0){
                    this.level1--;
                    space.setHeight(nH);
                }
                else throw new IllegalSpaceException("Space not accepted!");
                break;
            case 2 :
                if(this.level2 > 0){
                    this.level2--;
                    space.setHeight(nH);
                }
                else throw new IllegalSpaceException("Space not accepted!");
                break;
            case 3 :
                if(this.level3 > 0){
                    this.level3--;
                    space.setHeight(nH);
                }
                else throw new IllegalSpaceException("Space not accepted!");
                break;
            case 4 :
                if(this.dome > 0){
                    this.dome--;
                    space.setHeight(nH);
                }
                else throw new IllegalSpaceException("Space not accepted!");
                break;
            default: throw new IllegalSpaceException("Space not accepted!");
        };
    }
}

package it.polimi.ingsw.Model;

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
}

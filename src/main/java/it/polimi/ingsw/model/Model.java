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
    private Constraint constraint;
    public String[] deck;


    public Model() {
        this.level1 = constLevel1;
        this.level2 = constLevel2;
        this.level3 = constLevel3;
        this.dome = constDome;
        this.setUpTable();
        this.setUpDeck();
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public int getLevel1() {
        return level1;
    }

    public void setLevel1(int level1) {
        this.level1 = level1;
    }

    public int getLevel2() {
        return level2;
    }

    public void setLevel2(int level2) {
        this.level2 = level2;
    }

    public int getLevel3() {
        return level3;
    }

    public void setLevel3(int level3) {
        this.level3 = level3;
    }

    public int getDome() {
        return dome;
    }

    public void setDome(int dome) {
        this.dome = dome;
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }


    public void setUpTable(){
        for ( int i = 0; i < 5; i++ ){

            for ( int j = 0; j < 5; j++ ){

                this.table[i][j] = new Space( i, j );
            }
        }
    }

    private void setUpDeck() {
        this.deck[0] = "Apollo";
        this.deck[1] = "Artemis";
        this.deck[2] = "Athena";
        this.deck[3] = "Atlas";
        this.deck[4] = "Charon";
        this.deck[5] = "Demeter";
        this.deck[6] = "Hephaestus";
        this.deck[7] = "Hypnus";
        this.deck[8] = "Minotaur";
        this.deck[9] = "Pan";
        this.deck[10] = "Poseidon";
        this.deck[11] = "Prometheus";
        this.deck[12] = "Triton";
        this.deck[13] = "Zeus";

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



}

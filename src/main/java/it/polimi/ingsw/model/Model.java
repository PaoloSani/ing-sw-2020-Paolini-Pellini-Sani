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
        this.constraint = new Constraint();
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
        this.table = new Space[5][5];
        for ( int i = 0; i < 5; i++ ){
            for ( int j = 0; j < 5; j++ ){
                this.table[i][j] = new Space( i, j );
            }
        }
    }

    public Space getSpace(int x, int y) throws IllegalSpaceException{
        if( x >= 0 && x <= 4    &&
            y >= 0 && y <= 4     ){
            return table[x][y];
        }
        else throw (new IllegalSpaceException());
    }

    private void setUpDeck() {
        this.deck = new String[14];
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

    public boolean isFreeToMove( Worker worker ){
        int currX,currY,currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();

        if( ( 0 <= currX && currX <=4 ) && ( 0 <= currY && currY <= 4 )){
            for( int i = currX - 1; i < currX + 2; i++ ){
                for( int j = currY - 1 ; j < currY + 2; j++ ){
                    if( !( i == currX && j == currY ) && ( i >= 0 && i <= 4 && j >= 0 && j <= 4 ) && ( table[i][j].getHeight() < 4 && !table[i][j].isDomed() ) ){
                        if( table[i][j].getWorker() == null && ( table[i][j].getHeight() - currH <= 1 ) ) return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isFreeToBuild( Worker worker ){
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


    //lascio al controller la gestione del caso in cui myWorker coincide con oppWorker e che la cella passata sia effetivamente nelle celle adiacenti
    //devo anche controllare di non scambiare un worker dello stesso player
    public void charonPower( Worker myWorker, Worker oppWorker ) throws IllegalSpaceException{
        int myX, myY, oppX, oppY, newX, newY;

        //salvo le coordinate per fare i calcoli
        myX = myWorker.getSpace().getX();
        myY = myWorker.getSpace().getY();
        oppX = oppWorker.getSpace().getX();
        oppY = oppWorker.getSpace().getY();

        //calcolo le nuove coordinate
        if ( myX == oppX  ){        //spostamento nella stessa riga
            newX = oppX;
            if ( myY > oppY ) newY = oppY + 2;
            else newY = oppY - 2;
        }
        else if ( myY == oppY ){    //spostamento nella stessa colonna
            newY = oppY;
            if ( myX > oppX ) newX = oppX + 2;
            else newX = oppX - 2;
        }
        else {                      //spostamento nella diagonale
            if ( myX > oppX ) newX = oppX + 2;
            else newX = oppX - 2;

            if ( myY > oppY ) newY = oppY + 2;
            else newY = oppY - 2;
        }

        //ora che ho le nuove coordinate, controllo eventuali anomalie
        if ( newX < 0 || newX > 4 || newY < 0 || newY > 4 ||        //la space deve appartenere alla tabella
             this.table[newX][newY].isDomed()             ||        //la space non è occupate da una cupola
             this.table[newX][newY].getWorker() != null    )        //la space non è occupata da un altro worker
             throw new IllegalSpaceException( "Error: Invalid Space!" );

        //se i controlli sono stati superati allora effettuo lo scambio
        oppWorker.getSpace().setWorker(null);           //la cella che conteneva l'oppWorker è ora liberata
        this.table[newX][newY].setWorker(oppWorker);    //la nuova cella contiene ora il worker
        oppWorker.setSpace(this.table[newX][newY]);     //la posizione del oppWorker è ora newX - newY

    }


    //Gli ho dovuto passare il model perchè è un metodo statico sostiturei il tutto con un observer in futuro
    public void minotaurPower( Worker myWorker, Worker oppWorker ) throws IllegalSpaceException{
        int myX, myY, oppX, oppY, newX, newY;

        //salvo le coordinate per fare i calcoli
        myX = myWorker.getSpace().getX();
        myY = myWorker.getSpace().getY();
        oppX = oppWorker.getSpace().getX();
        oppY = oppWorker.getSpace().getY();

        //Calcolo casella dove viene spinto il workers
        if ( myX == oppX  ){        //spostamento nella stessa riga
            newX = oppX;
            if ( myY > oppY ) newY = oppY - 1;
            else newY = oppY + 1;
        }
        else if ( myY == oppY ){    //spostamento nella stessa colonna
            newY = oppY;
            if ( myX > oppX ) newX = oppX - 1;
            else newX = oppX + 1;
        }
        else {                      //spostamento nella diagonale
            if ( myX > oppX ) newX = oppX - 1;
            else newX = oppX + 1;

            if ( myY > oppY ) newY = oppY - 1;
            else newY = oppY + 1;
        }

        //ora che ho le nuove coordinate, controllo eventuali anomalie
        if ( newX < 0 || newX > 4 || newY < 0 || newY > 4 ||        //la space deve appartenere alla tabella
                this.table[newX][newY].isDomed()          ||        //la space non è occupate da una cupola
                this.table[newX][newY].getWorker() != null    )        //la space non è occupata da un altro worker
            throw new IllegalSpaceException( "Error: Invalid Space!" );

        oppWorker.getSpace().setWorker(null);           //la cella che conteneva l'oppWorker è ora liberata
        this.table[newX][newY].setWorker(oppWorker);    //la nuova cella contiene ora il worker spostato precedentemente
        oppWorker.setSpace(this.table[newX][newY]);     //la posizione del oppWorker è ora newX - newY

    }
}

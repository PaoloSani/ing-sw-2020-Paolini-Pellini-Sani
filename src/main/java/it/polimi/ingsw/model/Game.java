package it.polimi.ingsw.model;

public class Game {
    private int level1;
    private int level2;
    private int level3;
    private int dome;
    private final static int constLevel1 = 22;
    private final static int constLevel2 = 18;
    private final static int constLevel3 = 14;
    private final static int constDome = 18;
    private Space[][] table;
    private Constraint constraint;
    private LiteGame liteGame;

    public Game() {
        this.level1 = constLevel1;
        this.level2 = constLevel2;
        this.level3 = constLevel3;
        this.dome = constDome;
        this.setUpTable();
        this.liteGame = new LiteGame();
        liteGame.setLevel1(this.level1);
        liteGame.setLevel2(this.level2);
        liteGame.setLevel3(this.level3);
        liteGame.setDome(this.dome);
        //converte la tabella di Spaces in una tabella a tre dimensioni di caratteri in LiteGame
        liteGame.convertTable(table); //per copiare la tabella e i pezzi disponibili nel liteGame
        this.constraint = new Constraint();
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public void setLevel1(int level1) {
        this.level1 = level1;
    }

    public void setLevel2(int level2) {
        this.level2 = level2;
    }

    public void setLevel3(int level3) {
        this.level3 = level3;
    }

    public void setDome(int dome) {
        this.dome = dome;
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    public LiteGame getLiteGame() {
        return liteGame;
    }

    public void setUpTable(){
        this.table = new Space[5][5];
        for ( int i = 0; i < 5; i++ ){
            for ( int j = 0; j < 5; j++ ){
                this.table[i][j] = new Space( i, j );
            }
        }
    }

    public Space getSpace(int x, int y) {
        if( x >= 0 && x <= 4    &&
            y >= 0 && y <= 4     ){
            return table[x][y];
        }
        else return null;
    }

    //Scrive i nickname e le divinità dei giocatori nella classe liteGame
    //Non è nel liteGame, per far sì che i setter non siano visibili alla virtualView
    public void setPlayers( Player player1, Player player2, Player player3 ){
        liteGame.setName1(player1.getNickname());
        liteGame.setGod1(player1.getGod());
        liteGame.setName2(player2.getNickname());
        liteGame.setGod2(player2.getGod());
        if ( player3 != null ) {
            liteGame.setName3(player3.getNickname());
            liteGame.setGod3(player3.getGod());
        }
    }


    public void refreshLiteGame(){
        liteGame.setLevel1(this.level1);
        liteGame.setLevel2(this.level2);
        liteGame.setLevel3(this.level3);
        liteGame.setDome(this.dome);
        //converte la tabella di Spaces in una tabella a tre dimensioni di caratteri in LiteGame
        liteGame.convertTable(table);

    }

    public void setWinner(boolean result){
        liteGame.setWinner(result);
    }


    public void setCurrWorker( Worker worker ){
        if ( worker == null ){          //il giocatore ha perso
            liteGame.setCurrWorker(-1, 0);
        }
        else {
            liteGame.setCurrWorker(worker.getSpace().getX(), worker.getSpace().getY());
        }
    }

    public boolean isFreeToMove( Worker worker ){
        int currX, currY, currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();

        if( ( 0 <= currX && currX <=4 ) && ( 0 <= currY && currY <= 4 )){
            for( int i = currX - 1; i < currX + 2; i++ ){
                for( int j = currY - 1 ; j < currY + 2; j++ ){
                    if( !( i == currX && j == currY ) && ( i >= 0 && i <= 4 && j >= 0 && j <= 4 ) && ( table[i][j].getHeight() < 4 && !table[i][j].isDomed() ) ){

                        //caso senza il blocco, deve esistere una posizione la cui differenza di altezza è al massimo +1
                        if( table[i][j].getWorker() == null && ( table[i][j].getHeight() - currH <= 1 ) && !constraint.athenaBlocks() ){
                            return true;
                        }

                        //caso con il blocco, deve esistere una posizione la cui differenza di altezza è al massimo 0
                        else if( table[i][j].getWorker() == null && ( table[i][j].getHeight() - currH <= 0 ) && constraint.athenaBlocks() ) return true;
                        else if ( table[i][j].getWorker() != null && table[i][j].getWorker().getPlayer() != worker.getPlayer() && worker.getPlayer().getGod().equals(God.APOLLO) ){
                            return true;
                        }
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

            for( int i = currX - 1; i < currX + 2; i++ ){
                for( int j = currY - 1; j < currY + 2; j++ ){
                    if( !( i == currX && j == currY ) && ( i >= 0 && i <= 4 && j >= 0 && j <= 4 ) ){
                        if( table[i][j].getWorker() == null && ( table[i][j].getHeight() < 4 ) && !table[i][j].isDomed() ) return true;
                    }
                }
            }
          return false;
       }


    //lascio al controller la gestione del caso in cui myWorker coincide con oppWorker e che la cella passata sia effetivamente nelle celle adiacenti
    //devo anche controllare di non scambiare un worker dello stesso player
    public boolean charonPower( Worker myWorker, Worker oppWorker ) {
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
                myWorker.getPlayer() == oppWorker.getPlayer()   ||
                this.table[newX][newY].getWorker() != null    )        //la space non è occupata da un altro worker
             return false;

        //se i controlli sono stati superati allora effettuo lo scambio
        oppWorker.getSpace().setWorker(null);           //la cella che conteneva l'oppWorker è ora liberata
        oppWorker.setSpace(this.table[newX][newY]);     //la posizione del oppWorker è ora newX - newY e
                                                        //la nuova cella contiene ora il worker
        return true;
    }


    //Gli ho dovuto passare il model perchè è un metodo statico sostiturei il tutto con un observer in futuro
    public boolean minotaurPower( Worker myWorker, Worker oppWorker ) {
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
                myWorker.getPlayer() == oppWorker.getPlayer()   ||
                this.table[newX][newY].getWorker() != null    )        //la space non è occupata da un altro worker
            return false;

        oppWorker.getSpace().setWorker(null);           //la cella che conteneva l'oppWorker è ora liberata
        oppWorker.setSpace(this.table[newX][newY]);     //la posizione del oppWorker è ora newX - newY e
                                                        // la nuova cella contiene ora il worker spostato precedentemente
        return true;

    }


    public void setCurrPlayer(Player currPlayer) {
        liteGame.setCurrPlayer(currPlayer.getNickname());
    }

    // la uso nelle move per eliminare duplicazione del codice
    public boolean invalidMoveSpace (Space nextSpace, Space currSpace){
        int currX, currY, currH;
        currX = currSpace.getX();
        currY = currSpace.getY();
        currH = currSpace.getHeight();
        return ( invalidSpace(nextSpace,currSpace)                                    ||
                nextSpace.getHeight() - currH > 1                                     ||     //sale più di un livello
                currX == nextSpace.getX() && currY == nextSpace.getY());
    }

    // la uso nelle build per eliminare duplicazione del codice
    public boolean invalidSpace (Space space, Space currSpace){
        int currX, currY;
        currX = currSpace.getX();
        currY = currSpace.getY();
        return (space.getX() > 4 || space.getX() < 0                           ||
                space.getY() > 4 || space.getY() < 0                           ||     //space non appartenente alla tabella
                ( currX - space.getX() ) > 1 || ( currX - space.getX() ) < -1  ||     //riga non valida
                ( currY - space.getY() ) > 1 || ( currY - space.getY() ) < -1);
    }

   public boolean buildSwitch (Space space, int level, boolean isAtlas){
        switch (level) {

            case 1:
                // level>0
                if (this.level1 > 0) {                       //controllo che il pezzo corrispondente sia disponibile
                    //decremento i pezzi del livello disponibili, level1 --
                    this.level1--;
                    space.setHeight(level);                                          //setto la nuova altezza dello space
                } else
                    return false;        // se il pezzo non è disponibile lancio l'eccezione per la costruzione in quella cella
                break;

            case 2:
                if (this.level2 > 0) {
                    this.level2--;
                    space.setHeight(level);
                } else return false;
                break;

            case 3:
                if (this.level3 > 0) {
                    this.level3--;
                    space.setHeight(level);
                } else return false;
                break;

            case 4:
                if (this.dome > 0) {
                    if (isAtlas) {
                        space.setHeight(space.getHeight());
                        space.setDome();
                    } else space.setHeight(level);
                    this.dome--;
                } else return false;
                break;

            default:
                return false;
        }
        return true;
    }

}

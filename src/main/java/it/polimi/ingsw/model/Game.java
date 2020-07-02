package it.polimi.ingsw.model;

/**
 * The class Game represents the core of the game, describing the status of the current match
 */

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

    /**
     * Game constructor, sets all the level and spaces back to the default characteristics
     */
    public Game() {
        this.level1 = constLevel1;
        this.level2 = constLevel2;
        this.level3 = constLevel3;
        this.dome = constDome;

        //creates the game table
        this.setUpTable();
        this.liteGame = new LiteGame();
        liteGame.setLevel1(this.level1);
        liteGame.setLevel2(this.level2);
        liteGame.setLevel3(this.level3);
        liteGame.setDome(this.dome);

        //creates the first light version of the table
        liteGame.convertTable(table);

        //sets the constraints of the game (Athena and Hypnus in this case)
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

    /**
     * This method initializes the game table for the first time during a game
     */
    public void setUpTable(){
        this.table = new Space[5][5];
        for ( int i = 0; i < 5; i++ ){
            for ( int j = 0; j < 5; j++ ){
                this.table[i][j] = new Space( i, j );
            }
        }
    }

    /**
     * Takes coordinates and returns the corresponding space in the game table
     * @param x x coordinate
     * @param y y coordinate
     * @return a space in the table
     */
    public Space getSpace(int x, int y) {
        if( x >= 0 && x <= 4    &&
            y >= 0 && y <= 4     ){
            return table[x][y];
        }
        else return null;
    }

    /**
     * Write the nicknames and gods of the players in the instance of the liteGame
     */
    public void setPlayers( Player player1, Player player2, Player player3 ){
        liteGame.setName1(player1.getNickname());
        liteGame.setGod1(player1.getGod());
        liteGame.setName2(player2.getNickname());
        liteGame.setGod2(player2.getGod());

        //if the match is a three players match
        if ( player3 != null ) {
            liteGame.setName3(player3.getNickname());
            liteGame.setGod3(player3.getGod());
        }
    }

    /**
     * This method is invoked every time you need to update the game status
     */
    public void refreshLiteGame(){
        liteGame.setLevel1(this.level1);
        liteGame.setLevel2(this.level2);
        liteGame.setLevel3(this.level3);
        liteGame.setDome(this.dome);

        //converts the table to a lighter table, to be processed by clients
        liteGame.convertTable(table);
    }

    /**
     * This method is called when a player wins the game
     */
    public void setWinner(){
        liteGame.setWinner(true);
    }

    /**
     * Sets the current Worker in the liteGame
     * @param worker current worker
     */
    public void setCurrWorker( Worker worker ){
        //the player lost the game
        if ( worker == null ){
            liteGame.setCurrWorker(-1, 0);
        }
        else {
            liteGame.setCurrWorker(worker.getSpace().getX(), worker.getSpace().getY());
        }
    }

    /** This method controls whether a player is free to move around
     * @param worker Worker you want to check on
     * @return The method returns true if the worker is free to move in adjacent cells
     */
    public boolean isFreeToMove( Worker worker ){
        int currX, currY, currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();

        if( ( 0 <= currX && currX <=4 ) && ( 0 <= currY && currY <= 4 )){
            for( int i = currX - 1; i < currX + 2; i++ ){
                for( int j = currY - 1 ; j < currY + 2; j++ ){
                    if( !( i == currX && j == currY ) && ( i >= 0 && i <= 4 && j >= 0 && j <= 4 ) && ( table[i][j].getHeight() < 4 && !table[i][j].isDomed() ) ){

                        //If the player is Apollo and there is a non empty space containing an opponent's worker, he can perform a move according to the move rules
                        if ( worker.getPlayer().getGod().equals(God.APOLLO) && table[i][j].getWorker() != null &&
                                table[i][j].getWorker().getPlayer() != worker.getPlayer() &&  table[i][j].getHeight() - currH <= 1 && !constraint.athenaBlocks()){
                            return true;
                        }
                        //default move rules
                        else if( table[i][j].getWorker() == null && ( table[i][j].getHeight() - currH <= 1 ) && !constraint.athenaBlocks() ){
                            return true;
                        }

                        //If Athena blocks the other players, the move can be performed only on space that are on the same level or lower
                        else if( table[i][j].getWorker() == null && ( table[i][j].getHeight() - currH <= 0 ) && constraint.athenaBlocks() ) return true;

                    }
                }
            }
        }
        return false;
    }

    /** This method controls whether Prometheus is free to move around
     * @param worker Worker you want to check on
     * @return The method returns true if the worker is free to move in adjacent cells
     */
    public boolean isPrometheusFreeToMove( Worker worker ){
        int currX, currY, currH;
        currX = worker.getSpace().getX();
        currY = worker.getSpace().getY();
        currH = worker.getSpace().getHeight();

        if( ( 0 <= currX && currX <=4 ) && ( 0 <= currY && currY <= 4 )){
            for( int i = currX - 1; i < currX + 2; i++ ){
                for( int j = currY - 1 ; j < currY + 2; j++ ){
                    if( !( i == currX && j == currY ) && ( i >= 0 && i <= 4 && j >= 0 && j <= 4 ) && ( table[i][j].getHeight() < 4 && !table[i][j].isDomed() ) ){
                        //He cannot move up after he first built
                        if( table[i][j].getWorker() == null && ( table[i][j].getHeight() - currH <= 0 )  ){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /** This method controls whether a player is free to build around
     * @param worker Worker you want to check on
     * @return The method returns true if the worker is free to build in adjacent cells
     */
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


    /**
     * This method performs the necessary game changes for the use of the special power of Charon
     * @param oppWorker Opponent player to which the special ability is invoked
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean charonPower( Worker myWorker, Worker oppWorker ) {
        int myX, myY, oppX, oppY, newX, newY;

        //at first I save the coordinates
        myX = myWorker.getSpace().getX();
        myY = myWorker.getSpace().getY();
        oppX = oppWorker.getSpace().getX();
        oppY = oppWorker.getSpace().getY();

        //calculates the new coordinates
        if ( myX == oppX  ){        //on the same row
            newX = oppX;
            if ( myY > oppY ) newY = oppY + 2;
            else newY = oppY - 2;
        }
        else if ( myY == oppY ){    //on the same column
            newY = oppY;
            if ( myX > oppX ) newX = oppX + 2;
            else newX = oppX - 2;
        }
        else {                      //on the diagonal
            if ( myX > oppX ) newX = oppX + 2;
            else newX = oppX - 2;

            if ( myY > oppY ) newY = oppY + 2;
            else newY = oppY - 2;
        }

        //after the calculation, check is the new space is valid
        if ( newX < 0 || newX > 4 || newY < 0 || newY > 4 ||        //the space is in the table
             this.table[newX][newY].isDomed()             ||        //the space is not domed
                myWorker.getPlayer() == oppWorker.getPlayer()   ||     //not an action on a worker of mine
                this.table[newX][newY].getWorker() != null    )        //the new space must be empty
             return false;

        //after checking I can now perform the switch
        oppWorker.getSpace().setWorker(null);           //the previous space is made empty
        oppWorker.setSpace(this.table[newX][newY]);     //the worker is placed in the new space

        return true;
    }

    /**
     * This method performs the necessary game changes for the use of the special power of Minotaur
     * @param oppWorker Opponent player to which the special ability is invoked
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean minotaurPower( Worker myWorker, Worker oppWorker ) {
        int myX, myY, oppX, oppY, newX, newY;

        //similar to charonSwitch
        myX = myWorker.getSpace().getX();
        myY = myWorker.getSpace().getY();
        oppX = oppWorker.getSpace().getX();
        oppY = oppWorker.getSpace().getY();

        if ( myX == oppX  ){
            newX = oppX;
            if ( myY > oppY ) newY = oppY - 1;
            else newY = oppY + 1;
        }
        else if ( myY == oppY ){
            newY = oppY;
            if ( myX > oppX ) newX = oppX - 1;
            else newX = oppX + 1;
        }
        else {
            if ( myX > oppX ) newX = oppX - 1;
            else newX = oppX + 1;

            if ( myY > oppY ) newY = oppY - 1;
            else newY = oppY + 1;
        }


        if ( newX < 0 || newX > 4 || newY < 0 || newY > 4 ||
                this.table[newX][newY].isDomed()          ||
                myWorker.getPlayer() == oppWorker.getPlayer()   ||
                this.table[newX][newY].getWorker() != null    )
            return false;

        oppWorker.getSpace().setWorker(null);
        oppWorker.setSpace(this.table[newX][newY]);

        return true;

    }

    /**
     * Sets the name of the current player in the liteGame
     * @param currPlayer the current player
     */
    public void setCurrPlayer(Player currPlayer) {
        liteGame.setCurrPlayer(currPlayer.getNickname());
    }

    /**
     * Checks if the current space is adjacent to the next space
     * @param nextSpace the space where the player wants to move
     * @param currSpace the space where the player is now
     * @return true if the next space is not a valid space where to move
     */
    public boolean invalidMoveSpace (Space nextSpace, Space currSpace){
        int currX, currY, currH;
        currX = currSpace.getX();
        currY = currSpace.getY();
        currH = currSpace.getHeight();
        return ( invalidSpace(nextSpace,currSpace)                                    ||
                nextSpace.getHeight() - currH > 1                                     ||     //the player cannot move up more than one level
                currX == nextSpace.getX() && currY == nextSpace.getY());
    }

    /**
     * Checks if the space is a valid space where to build
     * @param space the space where the player wants to build
     * @param currSpace the space where the player is now
     * @return true if the space is not valid
     */
    public boolean invalidSpace (Space space, Space currSpace){
        int currX, currY;
        currX = currSpace.getX();
        currY = currSpace.getY();
        return (space.getX() > 4 || space.getX() < 0                           ||
                space.getY() > 4 || space.getY() < 0                           ||
                ( currX - space.getX() ) > 1 || ( currX - space.getX() ) < -1  ||
                ( currY - space.getY() ) > 1 || ( currY - space.getY() ) < -1);
    }

    /**
     * Method used to perform the build
     * @param space Space where you want to build
     * @param level Level you want to build
     * @param isAtlas This parameter indicates whether the Atlas constraint is active
     * @return This method returns true if the move has been successful, false otherwise
     */
    public boolean buildSwitch (Space space, int level, boolean isAtlas){
        switch (level) {
            case 1:
                //checks if the current piece is available
                if (this.level1 > 0) {
                    //decrements the pieces --
                    this.level1--;
                    space.setHeight(level);
                }
                else
                    return false;
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
                    //if the player is Atlas and wants to build a dome on a lower space
                    if ( isAtlas && space.getHeight() != 3 ) {
                        space.setHeight(space.getHeight());
                        space.setDome();
                    }
                    else space.setHeight(level);
                    this.dome--;
                } else return false;
                break;

            default:
                return false;
        }
        return true;
    }

}

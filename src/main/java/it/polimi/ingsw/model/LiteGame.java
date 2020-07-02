package it.polimi.ingsw.model;

import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Class used as a message to send to the virtual view
 */
public class LiteGame extends Observable<LiteGame>  {

    private String name1;
    private String name2;
    private String name3;

    private God god1;
    private God god2;
    private God god3;

    //The name of the current player
    private String currPlayer = "";

    //the space where the current worker is now
    private int[] currWorker;

    private int level1;
    private int level2;
    private int level3;
    private int dome;
    private boolean isWinner;

    /**
     * Two-dimensional string array used to briefly describe the game table. A string is composed as follows:
     * (A,B,C if there is a worker of a player on that cell, V otherwise) + (0,1,2,3 space height) + (N or D if there is a dome)
     */
    private String[][] table;
    private List<Observer<LiteGame>> observers = new ArrayList<>();


    /**
     * Class constructor
     */
    public LiteGame() {
        isWinner = false;
        table = new String[5][5];
        //the current worker is set to 5,5, outside the game table
        currWorker = new int[]{5,5};
    }

    /**
     * @param observer observer to add
     */
    public void addObservers(Observer<LiteGame> observer){
        observers.add(observer);
    }


    /**
     * Notifies the virtual view
     * @param message message to send to the observer
     */
    @Override
    public void notify(LiteGame message){
        for(Observer<LiteGame> observer: observers){
            observer.update(message.cloneLG());
        }
    }

    /**
     * Method that converts the model's original play table into the synthetic string table to be sent to the client
     * @param gameTable Model's original play table
     */
    protected void convertTable( Space[][] gameTable){
        String player, dome;
        for( int i = 0; i < 5; i++ )
            for(int j = 0; j < 5; j++ ){

                //initial case
                if ( name1 == null && name2 == null && name3 == null ){
                    table[i][j] = "V" + gameTable[i][j].getHeight() + "N";
                }
                else {
                    if ( gameTable[i][j].getWorker() == null ) player = "V";
                    else if (gameTable[i][j].getWorker().getPlayer().getNickname().equals(name1)) player = "A";
                    else if (gameTable[i][j].getWorker().getPlayer().getNickname().equals(name2)) player = "B";
                    else player = "C";
                    if (gameTable[i][j].isDomed()) dome = "D";
                    else dome = "N";
                    table[i][j] = player + gameTable[i][j].getHeight() + dome;
                }
            }

    }

    /**
     *
     * @return a clone of the liteGame
     */
    public LiteGame cloneLG(){

        LiteGame newLG = new LiteGame();

        newLG.name1 = this.name1;
        newLG.name2 = this.name2;
        newLG.name3 = this.name3;
        newLG.god1 = this.god1;
        newLG.god2 = this.god2;
        newLG.god3 = this.god3;
        newLG.currPlayer = this.currPlayer;
        if (this.currWorker != null ) newLG.currWorker = this.currWorker.clone();
        else newLG.currWorker = null;
        newLG.level1 = this.level1;
        newLG.level2 = this.level2;
        newLG.level3 = this.level3;
        newLG.dome = this.dome;
        newLG.table = new String[5][5];
        newLG.setWinner(this.isWinner);

        //the observers list doesn't change
        newLG.observers = this.observers;

        for (int i = 0 ; i < 5 ; i++){
            for(int j = 0 ; j < 5 ; j++){
                newLG.table[i][j] = this.table[i][j];
            }
        }

        return newLG;
    }

    /**
     * Used to compare two liteGame
     * @param liteGame liteGame to compare
     * @return true if the two liteGames are equal
     */
    public boolean equalsLG(LiteGame liteGame){
        if(liteGame.getTable()==null) return false;
        boolean isEqual = true;
        for (int i = 0 ; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                isEqual = isEqual && liteGame.table[i][j].equals(this.table[i][j]);
            }
        }

        if ( liteGame.god1 == null ){
            return false;
        }

        else {
            if ( name3 == null ) {
                return (isEqual && liteGame.god1.equals(this.god1) && liteGame.god2.equals(this.god2) &&
                        liteGame.name1.equals(this.name1) &&
                        liteGame.name2.equals(this.name2) &&
                        //liteGame.currPlayer.equalsLG(this.currPlayer)                                   &&
                        this.currWorker != null &&
                        liteGame.currWorker[0] == this.currWorker[0] &&
                        liteGame.currWorker[1] == this.currWorker[1] &&
                        liteGame.level1 == this.level1 && liteGame.level2 == this.level2 &&
                        liteGame.level3 == this.level3 && liteGame.dome == this.dome &&
                        liteGame.isWinner == this.isWinner);
            }
            else return ( isEqual && liteGame.god1.equals(this.god1) && liteGame.god2.equals(this.god2)   &&
                    liteGame.god3.equals(this.god3) && liteGame.name1.equals( this.name1 )                &&
                    liteGame.name2.equals(this.name2) && liteGame.name3.equals( this.name3 )              &&
                    //liteGame.currPlayer.equalsLG(this.currPlayer)                                       &&
                    this.currWorker != null                                                               &&
                    liteGame.currWorker != null                                                           &&
                    liteGame.currWorker[0] == this.currWorker[0]                                          &&
                    liteGame.currWorker[1] == this.currWorker[1]                                          &&
                    liteGame.level1 == this.level1 && liteGame.level2 == this.level2                      &&
                    liteGame.level3 == this.level3 && liteGame.dome == this.dome                          &&
                    liteGame.isWinner == this.isWinner                                                     );
        }


    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getName3() {
        return name3;
    }


    protected void setName1(String name1) {
        this.name1 = name1;
    }

    protected void setName2(String name2) {
        this.name2 = name2;
    }

    protected void setName3(String name3) {
        this.name3 = name3;
    }


    public God getGod1() {
        return god1;
    }

    public God getGod2() {
        return god2;
    }

    public God getGod3() {
        return god3;
    }

    protected void setGod1(God god1) {
        this.god1 = god1;
    }

    protected void setGod2(God god2) {
        this.god2 = god2;
    }

    protected void setGod3(God god3) {
        this.god3 = god3;
    }


    public String getCurrPlayer() {
        return currPlayer;
    }

    public int[] getCurrWorker() {
        return currWorker;
    }


    public int getLevel1() {
        return level1;
    }

    public int getLevel2() {
        return level2;
    }

    public int getLevel3() {
        return level3;
    }

    public int getDome() {
        return dome;
    }


    public boolean isWinner() {
        return isWinner;
    }

    /**
     * sets the current Worker space in the liteGame
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setCurrWorker(int x, int y) {
        //x < 0 means that the worker must be set to null, because he lost the game
        if ( x < 0 ){
            this.currWorker = null;
        }
        else {
            if ( this.currWorker == null ){
                this.currWorker = new int[]{x,y};
            }
            else {
                this.currWorker[0] = x;
                this.currWorker[1] = y;
            }
        }
    }

    protected void setCurrPlayer(String currPlayer) {
        this.currPlayer = currPlayer;
    }

    protected void setLevel1(int level1) {
        this.level1 = level1;
    }

    protected void setLevel2(int level2) {
        this.level2 = level2;
    }

    protected void setLevel3(int level3) {
        this.level3 = level3;
    }

    protected void setDome(int dome) {
        this.dome = dome;
    }

    protected void setWinner(boolean winner) {
        isWinner = winner;
    }
    //per i test
    public String getStringValue(int x, int y) {
        return this.table[x][y];
    }
    // per i test

    public String[][] getTable() {
        return table;
    }

    /**
     * Method that creates a serializable copy of the table
     */
    public SerializableLiteGame makeSerializable() {
        SerializableLiteGame newSLG = new SerializableLiteGame();

        newSLG.setName1(this.name1);
        newSLG.setName2(this.name2);
        newSLG.setName3(this.name3);
        newSLG.setGod1(this.god1);
        newSLG.setGod2(this.god2);
        newSLG.setGod3(this.god3);
        newSLG.setCurrPlayer(this.currPlayer);
        if ( this.currWorker != null ) newSLG.setCurrWorker(this.currWorker[0], this.currWorker[1]);
        else newSLG.setCurrWorker(-1,0);
        newSLG.setLevel1(this.level1);
        newSLG.setLevel2(this.level2);
        newSLG.setLevel3(this.level3);
        newSLG.setDome(this.dome);
        newSLG.setWinner(this.isWinner);
        newSLG.setTable(this.table.clone());

        return newSLG;
    }

    public void setPlayer(String name) {
        currPlayer = name;
    }

    //used for the tests
    public void setName1Test(String name1) {
        this.name1 = name1;
    }
}

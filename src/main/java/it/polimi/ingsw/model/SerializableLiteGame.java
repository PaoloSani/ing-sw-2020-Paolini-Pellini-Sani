package it.polimi.ingsw.model;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.LiteGame;
import it.polimi.ingsw.model.Space;
import it.polimi.ingsw.util.Observer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableLiteGame implements Serializable {

    private static final long ID = 1L;
    private String name1;       // Challenger: sceglie le carte e gioca per ultimo
    private String name2;       // Start Player: giocatore che gioca per primo il turno e pesca per primo la carta
    private String name3;

    private God god1;
    private God god2;
    private God god3;

    private String currPlayer = "";

    //indica il worker scelto dal giocatore, se nullo allora la scelta ha dato esito negativo (per entrambe le pedine) e il giocatore ha perso
    private int[] currWorker;

    private int level1;
    private int level2;
    private int level3;
    private int dome;
    private boolean isWinner;

    // A, B, C a seconda del player
    //
    private String[][] table;

    public SerializableLiteGame() {
        isWinner = false;
        table = new String[5][5];
        currWorker = new int[]{5,5};    // 5,5 è una posizione fuori dalla tabella indicata come posizione default all'inizio del gioco
    }

    public boolean equalsSLG(SerializableLiteGame newOne){
        if( newOne.getTable() == null ) return false;
        boolean isEqual = true;
        for (int i = 0 ; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                isEqual = isEqual && newOne.table[i][j].equals(this.table[i][j]);
            }
        }

        if ( newOne.god1 == null ){
            return false;
        }
        else
            if ( god3 != null ) return (isEqual && newOne.god1.equals(this.god1) && newOne.god2.equals(this.god2)   &&
                newOne.god3.equals(this.god3) && newOne.name1.equals(this.name1)            &&
                newOne.name2.equals(this.name2) && newOne.name3.equals(this.name3)          &&
                newOne.currWorker[0] == this.currWorker[0]                                    &&
                newOne.currWorker[1] == this.currWorker[1]                                    &&
                newOne.level1 == this.level1 && newOne.level2 == this.level2                &&
                newOne.level3 == this.level3 && newOne.dome == this.dome                    &&
                newOne.isWinner == this.isWinner                                              );
            else return (isEqual && newOne.god1.equals(this.god1) && newOne.god2.equals(this.god2)   &&
                      newOne.name1.equals(this.name1)            &&
                    newOne.name2.equals(this.name2) &&
                    newOne.currWorker[0] == this.currWorker[0]                                    &&
                    newOne.currWorker[1] == this.currWorker[1]                                    &&
                    newOne.level1 == this.level1 && newOne.level2 == this.level2                &&
                    newOne.level3 == this.level3 && newOne.dome == this.dome                    &&
                    newOne.isWinner == this.isWinner                                              );
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

    protected void setTable(String[][] table ){
        this.table = table;
    }

    protected void setCurrWorker(int x, int y) {
        if ( x < 0 ){   //se il giocatore ha perso, setto una cella non valida nella tabella
            this.currWorker = null;
        }
        else {
            this.currWorker[0] = x;
            this.currWorker[1] = y;
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

    public int getHeight(int[] space) {
        return Integer.parseInt(table[space[0]][space[1]].substring(1,2));
    }

    public boolean isPerimetralSpace(int[] space){
        return space[0]== 0 || space[1] == 0 || space[0]== 4 || space[1] == 4;
    }

}

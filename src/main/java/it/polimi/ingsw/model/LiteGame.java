package it.polimi.ingsw.model;

import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;
import it.polimi.ingsw.virtualView.FrontEnd;

import java.util.ArrayList;
import java.util.List;

//Classe da passare come messaggio alla virtual view
public class LiteGame extends Observable<LiteGame> {

    private String name1;       // Challenger: sceglie le carte e gioca per ultimo
    private String name2;       // Start Player: giocatore che gioca per primo il turno e pesca per primo la carta
    private String name3;

    private God god1;
    private God god2;
    private God god3;

    private String currPlayer;

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

    //crea la tabella semplificata da mandare ai client
    protected void convertTable( Space[][] gameTable){
        String player, dome;
        for( int i = 0; i < 5; i++ )
            for(int j = 0; j < 5; j++ ){

                //caso iniziale in cui non ho i nickname dei player
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

        // cella è una String
        // cella = table[i][j]
        // cella[0] è il player
        // cella[1] è l'altezza
        // cella[2] è la cupola
     }

    private List<Observer<LiteGame>> observers = new ArrayList<>();


    public LiteGame() {
        isWinner = false;
        table = new String[5][5];
        currWorker = new int[]{5,5};    // 5,5 è una posizione fuori dalla tabella indicata come posizione default all'inizio del gioco
    }

    public void addObservers(Observer<LiteGame> observer){
        observers.add(observer);
    }

    public void removeObservers(Observer<LiteGame> observer){
        observers.remove(observer);
    }

    //notify alla virtual view
    @Override
    public void notify(LiteGame message){
        for(Observer<LiteGame> observer: observers){
            observer.update(message);
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

}

package it.polimi.ingsw.virtualView;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.util.Observable;
import it.polimi.ingsw.util.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 * GameMessage is the message sent by the remote view (the FrontEnd) to the controller (the BackEnd), it contains the information for
 * an action performed in a player's turn
 */
public class GameMessage extends Observable <GameMessage> {
    private final FrontEnd frontEnd;
    private List<Observer<GameMessage>> observers = new ArrayList<>();

    /**
     * Used to set a space or two at the start of the game, in which the client places or selects his workers.
     */
    private int[] space1 = new int[]{-1,0};
    private int[] space2 = new int[]{0,0};

    /**
     * If greater than zero it indicates a build
     */
    int level;

    private String name1;
    private String name2;
    private String name3;

    private God god1;
    private God god2;
    private God god3;

    /**
     * If true, it means that the action his a switch performed by Charon
     */
    private boolean charonSwitching;

    /**
     * Is the GameMessage constructor. It resets the charonSwitching flag and adds the BackEnd as observer.
     * @param frontEnd is the corresponding FrontEnd
     */
    public GameMessage(FrontEnd frontEnd) {
        this.charonSwitching = false;
        this.frontEnd = frontEnd;
        addObservers( frontEnd.getBackEnd() );

    }

    public void addObservers(Observer<GameMessage> observer) {
        observers.add(observer);
    }

    public int[] getSpace1() {
        return space1;
    }

    public int[] getSpace2() {
        return space2;
    }

    public void setSpace1(int[] space1) {
        this.space1 = space1;
    }

    public void setSpace2(int[] space2) {
        this.space2 = space2;
    }

    public void setCharonSwitching(boolean charonSwitching) {
        this.charonSwitching = charonSwitching;
    }

    public int getLevel() {
        return level;
    }

    public boolean isCharonSwitching() {
        return charonSwitching;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public God getGod1() {
        return god1;
    }

    public void setGod1(God god1) {
        this.god1 = god1;
    }

    public God getGod2() {
        return god2;
    }

    public void setGod2(God god2) {
        this.god2 = god2;
    }

    public God getGod3() {
        return god3;
    }

    public void setGod3(God god3) {
        this.god3 = god3;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public FrontEnd getFrontEnd() {
        return frontEnd;
    }

    /**
     * Used when finishing a build or a performing a move after Charon's switch.
     */
    protected void resetGameMessage(){
        charonSwitching = false;
        level = 0;
    }

    /**
     * Notifies the message to the BackEnd.
     * @param message message to send to the observer
     */
    public void notify(GameMessage message) {
        for (Observer<GameMessage> observer : observers) {
            observer.update(message.cloneGM());
        }
    }

    /**
     * Clones the current GameMessage
     * @return a copy of the GameMessage
     */
    public GameMessage cloneGM(){
        GameMessage newMessage = new GameMessage(frontEnd);
        newMessage.name1 = this.name1;
        newMessage.name2 = this.name2;
        newMessage.name3 = this.name3;
        newMessage.god1 = this.god1;
        newMessage.god2 = this.god2;
        newMessage.god3 = this.god3;
        newMessage.level = this.level;
        newMessage.charonSwitching = this.charonSwitching;
        if (this.space1 != null) newMessage.space1 = this.space1.clone();
        else newMessage.space1 = null;
        if (this.space2 != null) newMessage.space2 = this.space2.clone();
        else newMessage.space2 = null;
        newMessage.observers = this.observers;

        return newMessage;
    }
}

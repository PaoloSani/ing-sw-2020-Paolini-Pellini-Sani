package it.polimi.ingsw.client;

import java.io.Serializable;

/**
 * It is the message used at the beginning of the game, to fix it.
 */

public class SettingGameMessage implements Serializable {
    private static final long ID = 1L;

    /**
     * It is the client's nickname
     */
    private String nickname;

    /**
     * It is the number of player of the match
     */
    private int numberOfPlayer;

    /**
     * True if the client wants to create a new game and play with friends
     */
    private boolean creatingNewGame;

    /**
     * True if the client wants to play an existing match with friends
     */
    private boolean playingExistingMatch;

    /**
     * It is the game ID of the match
     */
    private int gameID;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isCreatingNewGame() {
        return creatingNewGame;
    }

    public void setCreatingNewGame(boolean creatingNewGame) {
        this.creatingNewGame = creatingNewGame;
    }

    public boolean isPlayingExistingMatch() {
        return playingExistingMatch;
    }

    public void setPlayingExistingMatch(boolean playingExistingMatch) {
        this.playingExistingMatch = playingExistingMatch;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}

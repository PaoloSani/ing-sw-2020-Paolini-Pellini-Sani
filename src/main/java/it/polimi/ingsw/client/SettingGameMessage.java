package it.polimi.ingsw.client;

import java.io.Serializable;

public class SettingGameMessage implements Serializable {
    private String nickname;
    private int numberOfPlayer;
    private boolean creatingNewGame;
    private boolean playingExistingMatch;
    private int gameID;

    public String getNickname() {
        return nickname;
    }

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

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}

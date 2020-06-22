package it.polimi.ingsw.util;

/**
 * an enum class for the different type of messages the server sends to the client.
 */
public enum Message {
    BEGIN("Beginning new match"),
    WAIT("Waiting for other players"),
    INVALID_ID("Insert valid gameID"),

    PING("PING"),
    PONG("PONG"),
    CLOSE("CLOSE");

    /**
     * The String read by the client.
     */
    private String message;

    Message(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}

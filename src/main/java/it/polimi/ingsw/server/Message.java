package it.polimi.ingsw.server;

public enum Message {
    BEGIN("Beginning new match"),
    WAIT("Waiting for other players"),
    INVALID_ID("Insert valid gameID"),

    NEXT_ACTION("NEXT_ACTION"),
    PING("PING"),
    PONG("PONG"),
    CLOSE("CLOSE");

    private String message;

    Message(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}

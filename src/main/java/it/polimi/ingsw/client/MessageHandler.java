package it.polimi.ingsw.client;

import it.polimi.ingsw.CLI.CommandLineGame;
import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.util.Observable;

public class MessageHandler extends Observable<MessageHandler>{
    private String message;
    private SerializableLiteGame liteGameFromServer;
    private CommandLineGame cliToNotify;
    //true se ho letto una stringa, false se ho letto un liteGame
    private boolean stringRead;

    public MessageHandler(CommandLineGame cli) {
        cliToNotify = cli;
    }


    public String getString() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notify(this);
    }

    public SerializableLiteGame getLiteGameFromServer() {
        return liteGameFromServer;
    }

    public void setLiteGameFromServer(SerializableLiteGame liteGameFromServer) {
        this.liteGameFromServer = liteGameFromServer;
        notify(this);
    }

    public boolean isStringRead() {
        return stringRead;
    }

    public void setStringRead(boolean stringRead) {
        this.stringRead = stringRead;
    }

    public void notify(MessageHandler message){
        cliToNotify.update(message);
    }
}

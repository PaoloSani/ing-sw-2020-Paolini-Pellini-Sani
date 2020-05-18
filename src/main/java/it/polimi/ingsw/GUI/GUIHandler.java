package it.polimi.ingsw.GUI;

import it.polimi.ingsw.CLI.ColourFont;
import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.util.Observer;

public class GUIHandler implements Observer<MessageHandler> {

    private final ClientConnection clientConnection;
    private MessageHandler messageHandler;
    private boolean updateString = false;
    private boolean updateLG = false;
    private String nickname;
    private String messageFromServer;


    public GUIHandler(){
        this.messageHandler = new MessageHandler(this);
        this.clientConnection = new ClientConnection("127.0.0.1",4702, this.messageHandler);
    }

    public boolean setClientConnection(){
        new Thread (clientConnection::run).start();
        messageFromServer = readString();
        if (messageFromServer.equals("Welcome, server ready!\n")) return true;
        else return false;
    }

    public boolean askNameAvailability(String nickname){
        clientConnection.send(nickname);
        messageFromServer = readString();
        if (messageFromServer.equals("Nickname accepted")){
            this.nickname = nickname;
            return true;
        }
        else return false;

    }

    public synchronized String readString(){
        while ( !updateString ){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String result = messageHandler.getString();
        updateString = false;
        messageHandler.setStringRead(false);
        return result;
    }

    public synchronized SerializableLiteGame readSerializableLG() {
        while ( !updateLG ){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updateLG = false;
        SerializableLiteGame result = messageHandler.getLiteGameFromServer();
        messageHandler.setLGRead(false);
        return result;
    }

    @Override
    public synchronized void update(MessageHandler message) {
        if ( message.isStringRead() ){
            updateString = true;
        }
        if ( message.isLGRead() ) updateLG = true;
        messageHandler = message;
        notifyAll();
    }
}

package it.polimi.ingsw.client;

import it.polimi.ingsw.CLI.CommandLineGame;
import it.polimi.ingsw.GUI.GUIHandler;
import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.util.Observable;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MessageHandler extends Observable<MessageHandler>{
    private String message;
    private SerializableLiteGame liteGameFromServer;
    private CommandLineGame cliToNotify;
    private GUIHandler guiToNotify;
    //true se ho letto una stringa, false se ho letto un liteGame
    private boolean stringRead;
    private boolean LGRead;

    public MessageHandler(CommandLineGame cli) {
        cliToNotify = cli;
    }

    public MessageHandler(GUIHandler guiHandler) {
        this.guiToNotify = guiHandler;
    }

    public GUIHandler getGuiToNotify() {
        return guiToNotify;
    }

    public String getString() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
        if ( !message.contains("Ending") ) {
            notify(this);
        }
        else if ( message.contains("You have been")){
            if ( cliToNotify != null ){
                System.out.println("  " + message);
            }
            else Platform.runLater(() -> {
                Label label = new Label();
                ImageView imageView = guiToNotify.getErrorImage();
                imageView.setVisible(true);
                imageView.setOpacity(1);
                label.setText(message);
            });
        }
        else {
            Platform.runLater(() -> {
                Label label = new Label();
                ImageView imageView = guiToNotify.getErrorImage();
                imageView.setVisible(true);
                imageView.setOpacity(1);
                label.setText(message);
            });
        }
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
        if (cliToNotify != null) cliToNotify.update(message);
        else if (guiToNotify != null) guiToNotify.update(message);
    }

    public boolean isLGRead() {
        return LGRead;
    }

    public void setLGRead(boolean LGRead) {
        this.LGRead = LGRead;
    }
}

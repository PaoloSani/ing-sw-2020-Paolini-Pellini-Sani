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

/**
 * It is the handler of all the message coming in and out on the internet.
 */
public class MessageHandler extends Observable<MessageHandler>{

    /**
     * It is the last String read by the handler
     */
    private String message;

    /**
     * It is the last SerializableLiteGame read by the handler
     */
    private SerializableLiteGame liteGameFromServer;

    /**
     * It is the CLI bound to this message handler.
     * It isn't null if guiToNotify is null
     */
    private CommandLineGame cliToNotify;

    /**
     * It is the GUI bound to this message handler.
     * It isn't null if cliToNotify is null
     */
    private GUIHandler guiToNotify;

    /**
     * It tells if a String is read by the handler.
     */
    private boolean stringRead;

    /**
     * It tells if a SerializableLiteGame is read by the handler.
     */
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

    /**
     * It parses the message coming from the server
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
        if ( !message.contains("Ending") && !message.contains("You have been") ) {
            notify(this);
        }
        else if ( message.contains("You have been")){
            if ( cliToNotify != null ){
                System.out.println("  " + message);
            }
            else Platform.runLater(() -> {
                Label label = new Label();
                ImageView imageView;
                //imageView = guiToNotify.getErrorImage();
                imageView = new ImageView();
                imageView.setImage(new Image("/Backgrounds/Odyssey-Circe-scaled.png"));
                imageView.setVisible(true);
                imageView.setOpacity(1);
                label.setText(message);
                double labelX = guiToNotify.getCurrPane().getWidth()/2.0;
                double labelY = guiToNotify.getCurrPane().getHeight()/2.0;
                label.setLayoutX(labelX);
                label.setLayoutY(labelY);
                label.getStylesheets().add("/GUIScenes/CSSFiles/ButtonTexts.css");
                guiToNotify.getCurrPane().getChildren().addAll(imageView,label);
        });
        }
        else {
            Platform.runLater(() -> {
                Label label = new Label();
                ImageView imageView;
                imageView = new ImageView();
                imageView.setImage(new Image("/Backgrounds/Odyssey-Circe-scaled.png"));
                imageView.setVisible(true);
                imageView.setOpacity(1);
                label.setText(message);
                label.getStylesheets().add("/GUIScenes/CSSFiles/Labels.css");
                double labelX = (guiToNotify.getCurrPane().getWidth() - 2*label.getWidth())/2.0;
                double labelY = guiToNotify.getCurrPane().getHeight()/2.0;
                label.setLayoutX(labelX);
                label.setLayoutY(labelY);
                guiToNotify.getCurrPane().getChildren().addAll(imageView,label);
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

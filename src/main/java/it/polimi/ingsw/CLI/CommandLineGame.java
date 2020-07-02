package it.polimi.ingsw.CLI;

import it.polimi.ingsw.GUI.Mode;
import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.client.SettingGameMessage;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.util.Observer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * It defines the command line interface for the user and all the logic behind it.
 */

public class CommandLineGame implements Observer<MessageHandler> {

    /**
     * It is the standard input scanner.
     */
    private final Scanner in = new Scanner(System.in);

    /**
     * It is player's nickname.
     */
    private String nickname;

    /**
     * It is player's god.
     */
    private God god;

    /**
     * It is the mode chosen by the player to start a match.
     */
    private Mode mode = Mode.DEFAULT;

    /**
     * It is the number of player of the match
     */
    private int numOfPlayers;

    /**
     * It is the game ID of the match
     */
    private int gameID;

    /**
     * It is the message sent to the server to fix the game.
     */
    private final SettingGameMessage settingGameMessage = new SettingGameMessage();

    /**
     * It tells if the client wants to come back and change his mode.
     */
    private boolean quit = true;

    /**
     * It is the client's connection
     */
    private ClientConnection clientConnection;

    /**
     * It is the message sent by the client to play the game.
     */
    private final ClientMessage clientMessage = new ClientMessage();

    /**
     * It tells if the game has ended
     */
    private boolean endOfTheGame = false;

    /**
     * It is the message sent from the server to communicate the state of the game.
     */
    private String messageFromFrontEnd;

    /**
     * It is the last action performed by the player during his turn.
     */
    private String lastAction = "none";

    /**
     * It is the last SerializableLiteGame read.
     */
    private SerializableLiteGame serializableLiteGame = new SerializableLiteGame();

    /**
     * It is used to understand if two SerializableLiteGame are equal during "placing workers".
     */
    private SerializableLiteGame newSLG = new SerializableLiteGame();

    /**
     * It handles every message coming in and out on the internet
     */
    private MessageHandler messageHandler;

    /**
     * It tells if a String has been read.
     */
    private boolean updateString = false;

    /**
     * It tells if a SerializableLiteGame has been read.
     */
    private boolean updateLG = false;

    /**
     * It is responsible to print the game table
     */
    private CLIPrinter cliPrinter;

    public CommandLineGame() {
        this.messageHandler = new MessageHandler(this);
        this.cliPrinter = new CLIPrinter(this);
        try {
            String filename = "/server-settings.txt";
            BufferedReader settings = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));

            String ip = settings.readLine();
            ip = ip.replaceAll("\\s+","");
            ip = ip.split(":")[1];
            String port = settings.readLine();
            port = port.replaceAll("\\s+","");
            port = port.split(":")[1];
            this.clientConnection = new ClientConnection(ip, Integer.parseInt(port), messageHandler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * It let the CLI process the game.
     */
    public void runCLI() {
        int moveCounter =0, buildCounter = 0;
        boolean repeat = false;
        int [] lastSpace = new int[]{5,5};
        int[] firstWorker = new int[]{5,5};
        String messageToPrint = "none";
        int charonSwitch = 0;

        new Thread ( () -> clientConnection.run()).start();
        welcomeMirror();
        //messaggio di inizio partita
        System.out.println(readString());
        //metodo per l'inizio della partita e la scelta delle carte
        challengerChoosesGods();
        chooseCard();
        placeWorkers();
        while(!endOfTheGame) {
            messageFromFrontEnd = readString();
            if(messageFromFrontEnd.equals("Next action") ){
                if ( !repeat ) {
                    if (lastAction.equals("none") || lastAction.equals("End")) {
                        lastAction = "Choose Worker";
                        messageToPrint = "  Select the worker you want to play with";
                    }

                    else if (lastAction.equals("Choose Worker")) {
                        if (god == God.CHARON) {
                            charonSwitch++;
                            messageToPrint = "  Please select a space(ROW-COL)";
                        }
                        else if (god == God.PROMETHEUS) {
                            System.out.println("  Do you want to use Prometheus power? (yes/no)");
                            if (in.nextLine().equalsIgnoreCase("YES")) {
                                lastAction = "Prometheus Build";
                                messageToPrint = "  Please select the space where you want to build (ROW-COL)";
                            }
                            else {
                                lastAction = "Move";
                                messageToPrint = "  Please select the space you want to occupy (ROW-COL)";
                                moveCounter++;
                            }
                        } else {
                            lastAction = "Move";
                            moveCounter++;
                            messageToPrint = "  Please select the space you want to occupy (ROW-COL)";
                        }

                    } else if (lastAction.equals("Charon Switch") || lastAction.equals("Prometheus Build")){
                        lastAction = "Move";
                        moveCounter++;
                        messageToPrint = "  Please select the space you want to occupy (ROW-COL)";
                    }
                    else if (lastAction.equals("Move")) {
                        if ((god == God.ARTEMIS && moveCounter == 1) || ( god == God.TRITON && serializableLiteGame.isPerimetralSpace(lastSpace) )) {
                            System.out.println("  Do you want to move again? (yes/no)");
                            if (in.nextLine().equalsIgnoreCase("YES")) {
                                lastAction = "Move";
                                messageToPrint = "  Please select the space you want to occupy (ROW-COL)";
                                moveCounter++;
                            } else {
                                lastAction = "Build";
                                buildCounter++;
                                messageToPrint = "  Please select the space where you want to build (ROW-COL)";
                            }
                        } else {
                            buildCounter++;
                            lastAction = "Build";
                            messageToPrint = "  Please select the space where you want to build (ROW-COL)";
                        }
                    } else if (lastAction.equals("Build")) {
                        if (  god == God.DEMETER && buildCounter == 1                     ||     //se Efesto o Demetra e ha già fatto una sola build
                              god == God.HEPHAESTUS && buildCounter == 1 && serializableLiteGame.getHeight(lastSpace) < 3 ||
                            ( god == God.POSEIDON && buildCounter > 0 && buildCounter < 4 &&              // se Poseidone e ha già fatto una o più costruzioni (max 3)
                             !Arrays.equals(firstWorker, serializableLiteGame.getCurrWorker()))           ){       // sta giocando con il suo secondo worker

                            System.out.println("  Do you want to build again? (yes/no)");
                            if (in.nextLine().equalsIgnoreCase("YES") ) {
                                lastAction = "Build";
                                messageToPrint = "  Please select the space where you want to build (ROW-COL)";
                                buildCounter++;
                            }
                            else{
                                moveCounter = 0;
                                buildCounter = 0;
                                messageToPrint = "  End of the turn";
                                lastAction = "End";
                            }
                        } else {
                            moveCounter = 0;
                            buildCounter = 0;
                            messageToPrint = "  End of the turn";
                            lastAction = "End";
                        }
                    }
                }
                else repeat = false;

                if( !lastAction.equals("End") ) {
                    System.out.println(messageToPrint);
                    lastSpace = getSpaceFromClient();
                    clientMessage.setSpace1(lastSpace);
                    if ( god == God.CHARON && charonSwitch == 1 ){
                        String space = serializableLiteGame.getStringValue(lastSpace[0], lastSpace[1]);
                        if ( !space.contains("V") ){
                            lastAction = "Charon Switch";
                        }
                        else {
                            lastAction = "Move";
                            moveCounter++;
                        }
                    }
                    if (lastAction.contains("Build")) {
                        if ( god == God.ATLAS ) {
                            System.out.println("  Do you want to build a dome? (yes/no)");
                            if(in.nextLine().equals("yes")) clientMessage.setLevelToBuild(4);
                            else clientMessage.setLevelToBuild(serializableLiteGame.getHeight(clientMessage.getSpace1())+1);
                        }
                        else clientMessage.setLevelToBuild(serializableLiteGame.getHeight(clientMessage.getSpace1())+1);
                        if ( buildCounter == 1 ) {
                            firstWorker = serializableLiteGame.getCurrWorker();
                        }
                    }
                }
                clientMessage.setAction(lastAction);
                clientConnection.send(clientMessage);
            }
            //Siamo in caso in cui o abbiamo vinto o abbiamo perso
            else if(!messageFromFrontEnd.contains("Wait")){
                endOfTheGame = true;
            }

            else System.out.println(messageFromFrontEnd);

            if ( !endOfTheGame) {
                serializableLiteGame = readSerializableLG();
                cliPrinter.buildGameTable(this.serializableLiteGame);
            }

            if ( messageFromFrontEnd.equals("Next action") && !lastAction.equals("End") ) {
                messageFromFrontEnd = readString();

                if ( messageFromFrontEnd.equals("Invalid action") ) {
                    repeat = true;
                    System.out.println("  " + messageFromFrontEnd);
                }
                else if ( messageFromFrontEnd.contains("You won")){
                    endOfTheGame = true;
                }
                else charonSwitch = 0;
            }
        }
        System.out.println(messageFromFrontEnd);
    }


    /**
     * Welcome method: initialize a new settingGameMessage to send to Server
     */

    public void welcomeMirror() {
        System.out.println(ColourFont.ANSI_BLUE_BACKGROUND);
        System.out.println(ColourFont.ANSI_BOLD + "  Welcome to Santorini\n  RETRO Version\n\n" + ColourFont.ANSI_RESET + ColourFont.ANSI_BLUE_BACKGROUND);

            String messageFromServer = "Beginning";
            //welcoming client

            System.out.println(readString());

            System.out.println("  What's your name?\n\n" + ColourFont.ANSI_RESET);
            while (!messageFromServer.equals("Nickname accepted")) {
                nickname = in.nextLine().toUpperCase();
                clientConnection.send(nickname);
                messageFromServer = readString();
                if (messageFromServer.equals("Invalid Nickname")) {
                    System.out.println("  Nickname not available. Choose another nickname\n" + ColourFont.ANSI_RESET);
                }
            }

            while (quit) {
                quit = false;
                while ((mode != Mode.NEW_GAME) && (mode != Mode.EXISTING_MATCH) && (mode != Mode.RANDOM_MATCH)) {
                    String tempMode;
                    System.out.println(ColourFont.ANSI_BLUE_BACKGROUND + "\nPlease " + nickname + ", type A, B or C to choose different options:\n");
                    System.out.println(" - A) CREATE A NEW MATCH\n      Be the challenger of the isle!\n");
                    System.out.println(" - B) PLAY WITH YOUR FRIENDS\n      Play an already existing game!\n");
                    System.out.println(" - C) PLAY WITH STRANGERS\n      Challenge yourself with randomly chosen players!\n");
                    tempMode = in.nextLine().toUpperCase();
                    if (!(tempMode.equals("A")) && !(tempMode.equals("B")) && !(tempMode.equals("C")))
                        System.out.println("Dare you challenge the Olympus?? Retry\n ");
                    else mode = Mode.fromText(tempMode);
                }
                switch (mode) {
                    case NEW_GAME:
                        settingGameMessage.setCreatingNewGame(true);
                        settingGameMessage.setPlayingExistingMatch(false);
                        settingGameMessage.setGameID(0);
                        while (numOfPlayers != 2 && numOfPlayers != 3 && !quit) {
                            System.out.println("\n  Choose the number of players (2 or 3)");
                            System.out.println("  Type quit to return back!\n");
                            String actionA = in.nextLine();
                            actionA = actionA.toUpperCase();
                            if (actionA.equals("QUIT")) {
                                quit = true;
                                mode = Mode.DEFAULT;
                            } else if (!actionA.equals("2") && !actionA.equals("3"))
                                System.out.println("  Dare you challenge the Olympus?? Retry\n ");
                            else numOfPlayers = Integer.parseInt(actionA);
                        }
                        if ( !quit ) {
                            settingGameMessage.setNumberOfPlayer(numOfPlayers);
                            clientConnection.send(settingGameMessage);
                            System.out.println("  Waiting for other players");
                            System.out.println("  The gameID is " + readString() + "\n");
                        }
                        break;
                    case EXISTING_MATCH:
                        settingGameMessage.setPlayingExistingMatch(true);
                        settingGameMessage.setCreatingNewGame(false);
                        System.out.println("  Type the game ID");
                        System.out.println("  Type quit to return back!\n");
                        boolean validGameId = false;
                        while (!validGameId && !quit){
                            String actionB = in.nextLine();
                            actionB = actionB.toUpperCase();
                            if (actionB.equals("QUIT")) {
                                quit = true;
                                mode = Mode.DEFAULT;
                            } else {
                                gameID = Integer.parseInt(actionB);
                                settingGameMessage.setGameID(gameID);
                            }
                            if ( !quit ) {
                                clientConnection.send(settingGameMessage);
                                messageFromServer = readString();
                                System.out.println("  Server says: " + messageFromServer + "\n");
                                if (messageFromServer.equals("Insert valid gameID"))
                                    System.out.println("  Insert a valid gameID");
                                else validGameId = true;
                            }
                        }
                        break;
                    case RANDOM_MATCH:
                        settingGameMessage.setCreatingNewGame(false);
                        settingGameMessage.setPlayingExistingMatch(false);
                        settingGameMessage.setGameID(0);
                        while (numOfPlayers != 2 && numOfPlayers != 3 && !quit) {
                            System.out.println("  Choose the number of players (2 or 3)");
                            System.out.println("  Type quit to return back!");
                            String actionC = in.nextLine();
                            actionC = actionC.toUpperCase();
                            if (actionC.equals("QUIT")) {
                                quit = true;
                                mode = Mode.DEFAULT;
                            } else if (!actionC.equals("2") && !actionC.equals("3"))
                                System.out.println("  Dare you challenge the Olympus?? Retry\n ");
                            else numOfPlayers = Integer.parseInt(actionC);
                        }
                        if ( !quit ) {
                            settingGameMessage.setNumberOfPlayer(numOfPlayers);
                            settingGameMessage.setNickname(nickname);
                            clientConnection.send(settingGameMessage);
                            messageFromServer = readString();
                            System.out.println("  Server says: " + messageFromServer + "\n");
                            if ( messageFromServer.contains("You are")){
                                mode = Mode.NEW_GAME;
                            }
                        }
                        break;
                }
            }



    }

    /**
     * The Challenger chooses the cards which he wants to play with
     */

    void challengerChoosesGods(){
        if(mode == Mode.NEW_GAME) {
            List<God> chosenGods = new ArrayList<>();
            while (chosenGods.size() < numOfPlayers) {
                System.out.println("  Challenger of the Olympus, choose the Gods who will lead you to the glory\n");
                for (God g : God.values()) {
                    if (!chosenGods.contains(g)) System.out.println("  - " + g.toString() + ": " + g.getPower());
                }
                System.out.println();
                System.out.println("  Please,choose a God\n");
                String singleChosenGod = in.nextLine().toUpperCase();
                try {
                    chosenGods.add(God.valueOf(singleChosenGod));
                } catch (IllegalArgumentException e) {
                    System.out.println(ColourFont.ANSI_BOLD + "  Please, type a valid God!\n" + ColourFont.ANSI_RESET + ColourFont.ANSI_BLUE_BACKGROUND);
                }
            }
            String[] challengerMessage;
            if (numOfPlayers == 2) {
                challengerMessage =
                        new String[]{chosenGods.get(0).toString(), chosenGods.get(1).toString()};
            } else {
                challengerMessage =
                        new String[]{chosenGods.get(0).toString(), chosenGods.get(1).toString(), chosenGods.get(2).toString()};
            }
            System.out.println("  Now wait for other players to choose their cards");
            clientConnection.send(challengerMessage);

        }
        else System.out.println("  Please, wait the Challenger to choose the Pantheon");
    }

    /**
     * It let the player choose his card to play with.
     */
    private void chooseCard() {

        messageFromFrontEnd = readString();
        if ( mode == Mode.NEW_GAME ){
            god = God.valueOf(messageFromFrontEnd);
            System.out.println("  Your god is " + messageFromFrontEnd + ColourFont.ANSI_RESET);
        }
        else {
            String choice = "none";

            while ( !messageFromFrontEnd.contains(choice) ){
                System.out.println("  Choose your god! Available gods: " + messageFromFrontEnd);
                choice = in.nextLine().toUpperCase();
                try {
                    god = God.valueOf(choice);
                }
                catch ( IllegalArgumentException e ){
                    System.out.println("Please type a valid God!");
                    choice = "none";
                }

            }
            clientMessage.setName(nickname);
            clientMessage.setGod(god);
            clientConnection.send(clientMessage);
            System.out.println(readString()+ColourFont.ANSI_RESET+"\n\n");
        }
        serializableLiteGame = readSerializableLG();
        cliPrinter.buildGameTable(this.serializableLiteGame);
    }

    /**
     * It let the player put his worker on the game table.
     */
    private void placeWorkers() {
        messageFromFrontEnd = "none";
        while ( !messageFromFrontEnd.equals("Placing workers") ){
            messageFromFrontEnd = readString();
            System.out.println("  "+messageFromFrontEnd);
           if ( messageFromFrontEnd.contains("Wait") ){
               serializableLiteGame = readSerializableLG();
               cliPrinter.buildGameTable(this.serializableLiteGame);
           }
        }
        boolean validPlacing = false;
        while (!validPlacing) {
            clientMessage.setSpace1(getSpaceFromClient());
            clientMessage.setSpace2(getSpaceFromClient());
            clientConnection.send(clientMessage);
            newSLG = readSerializableLG();
            if(!newSLG.equalsSLG(serializableLiteGame)){
                validPlacing = true;
                serializableLiteGame = newSLG;
            }
            cliPrinter.buildGameTable(this.serializableLiteGame);
            if(!validPlacing){
                System.out.println("  Please retype two correct spaces!");
                messageFromFrontEnd = readString();
            }
        }
    }

    /**
     * It parses the player's space from standard input.
     * @return the parsed space.
     */
    private int[] getSpaceFromClient(){
        int[] newSpace = new int[]{5,5};
        while ( newSpace[0] < 0 || newSpace[0] > 4 || newSpace[1] < 0 || newSpace[1] > 4 ) {
            System.out.println("  Insert the space coordinates (ROW-COL): \n ");
            for(boolean validMessage = false;!validMessage; ) {
                String space = in.nextLine();
                space.replace(" ","-");
                space.replace(",","-");
                String[] coord = space.split("-");

                if (coord.length == 2){
                    newSpace = new int[]{Integer.parseInt(coord[0])-1, Integer.parseInt(coord[1])-1};
                    validMessage = true;
                }
                else{
                    System.out.println("  Invalid space!");
                }
            }
        }
        return newSpace;
    }

    /**
     * @return the nickname of the client
     */
    public String getNickname() {
        return nickname;
    }


    /**
     *
     * @param serializableLiteGame
     */
    public void setLiteGame(SerializableLiteGame serializableLiteGame) {
        this.serializableLiteGame = serializableLiteGame;
    }

    public void setNumOfPlayer(int i) {
        this.numOfPlayers = i;
    }

    /**
     * It reads a String message sent by the server.
     * If there are no messages, it waits until a message comes.
     * @return the read message.
     */

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

    /**
     * It reads a SerializableLiteGame message sent by the server.
     * If there are no messages, it waits until a message comes.
     * @return the read message.
     */
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

    /**
     * It sets updateString or updateLG and notifies all sleeping threads.
     * @param message: message received from the observable instance
     */
    @Override
    public synchronized void update(MessageHandler message){
        if ( message.isStringRead() ){
            updateString = true;
        }
        if ( message.isLGRead() ) updateLG = true;
        messageHandler = message;
        notifyAll();
    }


    /////////////////////////////////////////////////////////////////////
    //////////////////////// TESTS' METHOD //////////////////////////////
    /////////////////////////////////////////////////////////////////////


    public CLIPrinter getCliPrinter() {
        return cliPrinter;
    }

    public SerializableLiteGame getSerializableLiteGame() {
        return serializableLiteGame;
    }
}

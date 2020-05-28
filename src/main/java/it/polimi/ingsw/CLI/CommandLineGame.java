package it.polimi.ingsw.CLI;

import it.polimi.ingsw.GUI.Mode;
import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.MessageHandler;
import it.polimi.ingsw.client.SettingGameMessage;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;
import it.polimi.ingsw.server.Message;
import it.polimi.ingsw.util.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;



public class CommandLineGame implements Observer<MessageHandler> {

    private final Scanner in = new Scanner(System.in);
    private String nickname;
    private God god;
    private Mode mode = Mode.DEFAULT;
    private int numOfPlayers;
    private int gameID;
    private SettingGameMessage settingGameMessage = new SettingGameMessage();
    private boolean quit = true;
    private ClientConnection clientConnection;
    private String[] challengerMessage;
    private ClientMessage clientMessage = new ClientMessage();
    private boolean endOfTheGame = false;
    private String messageFromFrontEnd;
    private String lastAction = "none";
    private SerializableLiteGame serializableLiteGame = new SerializableLiteGame();
    private SerializableLiteGame newSLG = new SerializableLiteGame();
    private MessageHandler messageHandler;
    private boolean updateString = false;
    private boolean updateLG = false;
    private boolean enableInput;

    public CommandLineGame() {
        this.messageHandler = new MessageHandler(this);
        this.clientConnection = new ClientConnection("127.0.0.1", 4702, messageHandler);
        enableInput = false;
    }

    public void runCLI() {
        int moveCounter =0, buildCounter = 0;
        boolean repeat = false;
        int [] lastSpace = new int[]{5,5};
        int[] firstWorker = new int[]{5,5};
        String messageToPrint = "none";             //TODO: stampo questo invece che lastAction
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
                        if ( ((god == God.HEPHAESTUS || god == God.DEMETER) && buildCounter == 1)  ||     //se Efesto o Demetra e ha già fatto una sola build
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

            serializableLiteGame = readSerializableLG();
            buildGameTable();

            if ( messageFromFrontEnd.equals("Next action") && !lastAction.equals("End") ) {
                messageFromFrontEnd = readString();

                if ( messageFromFrontEnd.equals("Invalid action") ) {
                    repeat = true;
                    System.out.println("  " + messageFromFrontEnd);
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
     * The Challenger chooses the cards which he wants to play with*
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
            }
            god = God.valueOf(choice);
            //in realtà settare il nickname è superfluo perché il frontend già conosce i nickname dei client
            clientMessage.setName(nickname);
            clientMessage.setGod(god);
            clientConnection.send(clientMessage);
            System.out.println(readString()+ColourFont.ANSI_RESET+"\n\n");
        }
        serializableLiteGame = readSerializableLG();
        buildGameTable();
    }

    private void placeWorkers() {
        messageFromFrontEnd = "none";
        while ( !messageFromFrontEnd.equals("Placing workers") ){
            messageFromFrontEnd = readString();
            System.out.println("  "+messageFromFrontEnd);
           if ( messageFromFrontEnd.contains("Wait") ){
               serializableLiteGame = readSerializableLG();
               buildGameTable();
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
            buildGameTable();
            if(!validPlacing) System.out.println("  Please retype two correct spaces!");
        }
    }

    private int[] getSpaceFromClient(){
        int[] newSpace = new int[]{5,5};
        //TODO: migliorare controlli sulle celle disponibili e messaggio di errore al client
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
     * It prints on mirror the gametable from the litegame
     */

    void printKeysTable(){
        System.out.println(ColourFont.ANSI_BOLD+"  KEYS  "+ColourFont.ANSI_RESET+ColourFont.ANSI_BLACK_BACKGROUND + "\n");
        System.out.println("  - GROUND LEVEL: " + ColourFont.ANSI_GREEN_BACKGROUND + "    " + ColourFont.ANSI_RESET + ColourFont.ANSI_BLACK_BACKGROUND);
        System.out.println("  - FIRST LEVEL:  " + ColourFont.ANSI_LEVEL1 + "    " + ColourFont.ANSI_RESET + ColourFont.ANSI_BLACK_BACKGROUND);
        System.out.println("  - SECOND LEVEL: " + ColourFont.ANSI_LEVEL2 + "    " + ColourFont.ANSI_RESET + ColourFont.ANSI_BLACK_BACKGROUND);
        System.out.println("  - THIRD LEVEL:  " + ColourFont.ANSI_LEVEL3 + "    " + ColourFont.ANSI_RESET + ColourFont.ANSI_BLACK_BACKGROUND);
        System.out.println("  - DOME:         " + ColourFont.ANSI_DOME + "    " + ColourFont.ANSI_RESET + ColourFont.ANSI_BLACK_BACKGROUND + ColourFont.ANSI_RESET+"\n");

    }

    public synchronized void  buildGameTable(){
        String[][] gameTable = serializableLiteGame.getTable();
        System.out.println("                                                             ");
        System.out.println("                                  1        2        3        4        5                " +  ColourFont.ANSI_BOLD+"  KEYS  "+ColourFont.ANSI_RESET);
        System.out.println("                              + = = = ++ = = = ++ = = = ++ = = = ++ = = = +            " + "  - GROUND LEVEL: " + ColourFont.ANSI_GREEN_BACKGROUND + "    " + ColourFont.ANSI_RESET );
        for (int i = 0; i < 5; i++){
            buildTableRow(gameTable[i],i+1);
        }
    }

    void buildTableRow(String[] serializableLiteGameRow, int row){
        String[] space1 = buildGameSpace(serializableLiteGameRow[0],row-1,0);
        String[] space2 = buildGameSpace(serializableLiteGameRow[1],row-1,1);
        String[] space3 = buildGameSpace(serializableLiteGameRow[2],row-1,2);
        String[] space4 = buildGameSpace(serializableLiteGameRow[3],row-1,3);
        String[] space5 = buildGameSpace(serializableLiteGameRow[4],row-1,4);
        String[] newRow;
        if (row == 1){
            newRow = new String[]{
                    "                              "+space1[0]+space2[0]+space3[0]+space4[0]+space5[0]+"            "+"  - FIRST LEVEL:  " + ColourFont.ANSI_LEVEL1 + "    " + ColourFont.ANSI_RESET + " x"+serializableLiteGame.getLevel1(),
                    "                           "+row+"  "+space1[1]+space2[1]+space3[1]+space4[1]+space5[1]+"            "+"  - SECOND LEVEL: " + ColourFont.ANSI_LEVEL2 + "    " + ColourFont.ANSI_RESET + " x"+serializableLiteGame.getLevel2(),
                    "                              "+space1[2]+space2[2]+space3[2]+space4[2]+space5[2]+"            "+"  - THIRD LEVEL:  " + ColourFont.ANSI_LEVEL3 + "    " + ColourFont.ANSI_RESET + " x"+serializableLiteGame.getLevel3(),
                    "                              "+space1[3]+space2[3]+space3[3]+space4[3]+space5[3]+"            "+"  - DOME:         " + ColourFont.ANSI_DOME + "    " + ColourFont.ANSI_RESET + " x"+serializableLiteGame.getDome()
            };
        }

        else if (row == 2) {
            String yourPlayer = " (YOU)";
            String name1 = "none";
            String name2 = "none";
            String name3 = "none";

            if (serializableLiteGame.getName1() != null ) {
                name1 = serializableLiteGame.getName1();
                if ( name1.equals(nickname)) name1 = name1.concat(yourPlayer);
            }
            if (serializableLiteGame.getName2() != null ) {
                name2 = serializableLiteGame.getName2();
                if ( name2.equals(nickname)) name2 = name2.concat(yourPlayer);
            }
            if (serializableLiteGame.getName3() != null ) {
                name3 = serializableLiteGame.getName3();
                if ( name3.equals(nickname)) name3 = name3.concat(yourPlayer);
            }

            if (this.serializableLiteGame.getName3() != null) {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - PLAYER A: " + ColourFont.getGodColour(serializableLiteGame.getGod1()) + name1 + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2] + "            " + "  - PLAYER B: " + ColourFont.getGodColour(serializableLiteGame.getGod2()) + name2 + ColourFont.ANSI_RESET,
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3] + "            " + "  - PLAYER C: " + ColourFont.getGodColour(serializableLiteGame.getGod3()) + name3 + ColourFont.ANSI_RESET
                };
            }
            else {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - PLAYER A: " + ColourFont.getGodColour(serializableLiteGame.getGod1()) + name1 + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2] + "            " + "  - PLAYER B: " + ColourFont.getGodColour(serializableLiteGame.getGod2()) + name2 + ColourFont.ANSI_RESET,
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3],
                };
            }
        }

        else if (row == 3){
            if (this.serializableLiteGame.getName3() != null) {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - CURRENT WORKER: " + ColourFont.ANSI_WORKER + "    " + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3] + "            " + "  - GOD A: " + ColourFont.getGodColour(serializableLiteGame.getGod1()) + serializableLiteGame.getGod1() + ColourFont.ANSI_RESET,
                };
            }
            else {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - CURRENT WORKER: " + ColourFont.ANSI_WORKER + "    " + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3] + "            " + "  - GOD A: " + ColourFont.getGodColour(serializableLiteGame.getGod1()) + serializableLiteGame.getGod1() + ColourFont.ANSI_RESET,
                };
            }
        }

        else if (row == 4) {
            if (this.serializableLiteGame.getName3() != null) {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0] + "            " + "  - GOD B: " + ColourFont.getGodColour(serializableLiteGame.getGod2()) + serializableLiteGame.getGod2() + ColourFont.ANSI_RESET,
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1] + "            " + "  - GOD C: "  + ColourFont.getGodColour(serializableLiteGame.getGod3()) + serializableLiteGame.getGod3() + ColourFont.ANSI_RESET,
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3],
                };
            }
            else {
                newRow = new String[]{
                        "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0] + "            " + "  - GOD B: " + ColourFont.getGodColour(serializableLiteGame.getGod2()) + serializableLiteGame.getGod2() + ColourFont.ANSI_RESET,
                        "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1],
                        "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                        "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3],
                };
            }
        }
        else {
            newRow = new String[]{
                    "                              " + space1[0] + space2[0] + space3[0] + space4[0] + space5[0],
                    "                           " + row + "  " + space1[1] + space2[1] + space3[1] + space4[1] + space5[1],
                    "                              " + space1[2] + space2[2] + space3[2] + space4[2] + space5[2],
                    "                              " + space1[3] + space2[3] + space3[3] + space4[3] + space5[3],
            };
        }
        for (String s : newRow){
            System.out.println(s);
        }
    }

    String[] buildGameSpace(String spaceFromLiteGame, int row, int col){
        ColourFont background = ColourFont.ANSI_MENU_BACKGROUND;
        ColourFont foreground = ColourFont.ANSI_WHITE_TEXT;
        ColourFont chosen = ColourFont.ANSI_GREEN_BACKGROUND;
        String dome = "";
        String reset = ColourFont.ANSI_RESET;
        String player;
        String[] gameSpace;
        char[] spaceAnalyzer = spaceFromLiteGame.toCharArray();

        if (spaceAnalyzer[1] == '0') {
            background = ColourFont.ANSI_GREEN_BACKGROUND;
            chosen = background;
        }
        else if (spaceAnalyzer[1] == '1') {
            background = ColourFont.ANSI_LEVEL1;
            chosen = background;
        }
        else if (spaceAnalyzer[1] == '2') {
            background = ColourFont.ANSI_LEVEL2;
            chosen = background;
        }
        else if (spaceAnalyzer[1] == '3') {
            background = ColourFont.ANSI_LEVEL3;
            chosen = background;
            foreground = ColourFont.ANSI_BLACK_TEXT;
        }
        if (spaceAnalyzer[2] == 'D') {
            dome = ColourFont.ANSI_DOME.toString();
        }
        switch(spaceAnalyzer[0]){
            case 'A':
                player = "(A)";
                break;

            case 'B':
                player = "(B)";
                break;

            case 'C':
                player = "(C)";
                break;

            default:
                player = "   ";
                break;
            }
        player = ColourFont.ANSI_BLACK_TEXT+player+ColourFont.ANSI_BLACK_TEXT;
        if (serializableLiteGame.getCurrWorker()[0] == row && serializableLiteGame.getCurrWorker()[1] == col){
            chosen = ColourFont.ANSI_WORKER;
        }

        if(chosen == ColourFont.ANSI_WORKER) {
            gameSpace = new String[]{
                    "|" + chosen + foreground.toString() + " " + dome + "     " + chosen + " " + reset + "|",
                    "|" + chosen + foreground.toString() + " " + dome + " " + background + player + chosen.toString() + dome + " " + chosen + " " + reset + "|",
                    "|" + chosen + foreground.toString() + " " + dome + "     " + chosen + " " + reset + "|",
                    "+ = = = +"
            };
        }
        else{
            gameSpace = new String[]{
                    "|" + background + foreground.toString() + " " + dome + "     " + background + " " + reset + "|",
                    "|" + background + foreground.toString() + " " + dome + " " + background + dome + player + dome + " " + background + " " + reset + "|",
                    "|" + background + foreground.toString() + " " + dome + "     " + background+ " " + reset + "|",
                    "+ = = = +"
            };
        }
        return gameSpace;
    }

    //todo: da finire
    String parseInput() {
        String message = in.nextLine();
        message = message.toUpperCase();
        String[] parsedMessage = message.split(" ");
        for (String s : parsedMessage){
            message = s + " ";
        }
        return null;
    }

    public void setLiteGame(SerializableLiteGame serializableLiteGame) {
        this.serializableLiteGame = serializableLiteGame;
    }

    public SerializableLiteGame getSerializableLiteGame(){
        return this.serializableLiteGame;
    }

    public void setNumOfPlayer(int i) {
        this.numOfPlayers = i;
    }

   /* public synchronized String readStringFromInput(){
        fromClient = null;
        enableInput = true;
        while( fromClient )
    }
    */

    public void readInputThread() {
        new Thread ( () ->
        {
            String command = "start";
            while (!command.equals("CLOSE")) {
                if (in.hasNext()) {
                    command = in.nextLine().toUpperCase();
                    if ( enableInput ){
                        String fromClient = command;
                    }

                    //processo la stringa
                    //se non è il suo turno non riceve NEXT ACTION dal front end, quindi non fa niente se l'utente scrive un comando valido

                }
            }
            clientConnection.send(Message.CLOSE);
            clientConnection.setActive(false);
        }
        ).start();
    }

    //Legge stringhe dal MessageHandler se la cli è stata notificata per una stringa nuova dal messageHandler
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
    public synchronized void update(MessageHandler message){
        if ( message.isStringRead() ){
            updateString = true;
        }
        if ( message.isLGRead() ) updateLG = true;
        messageHandler = message;
        notifyAll();
    }
}

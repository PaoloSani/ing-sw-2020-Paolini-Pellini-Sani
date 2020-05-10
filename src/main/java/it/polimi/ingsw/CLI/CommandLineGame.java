package it.polimi.ingsw.CLI;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.SettingGameMessage;

import java.awt.image.BandCombineOp;
import java.io.IOException;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.SerializableLiteGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineGame {

    private final Scanner in = new Scanner(System.in);
    private String nickname;
    private God god;
    private String mode = "start";
    private int numOfPlayers;
    private int gameID;
    private SettingGameMessage settingGameMessage = new SettingGameMessage();
    private boolean quit = true;
    private ClientConnection clientConnection;
    private String[] challengerMessage;
    private SerializableLiteGame serializableLiteGame = new SerializableLiteGame();
    private ClientMessage clientMessage = new ClientMessage();
    private boolean endOfTheGame = false;
    private String messageFromFrontEnd;
    private String lastAction = "none";
    private SerializableLiteGame newMessage = new SerializableLiteGame();

    public void runCLI() {
        int moveCounter =0, buildCounter =0;
        int [] lastSpace = new int[]{5,5};
        welcomeMirror();
        //messaggio di inizio partita
        System.out.println(clientConnection.readString());
        //metodo per l'inizio della partita e la scelta delle carte
        challengerChoosesGods();
        chooseCard();
        placeWorkers();
        while(!endOfTheGame) {
            messageFromFrontEnd = clientConnection.readString();
            if(messageFromFrontEnd.equals("Next action")){
                if (lastAction.equals("none") || lastAction.equals("End")) lastAction = "Choose Worker";

                else if (lastAction.equals("Choose Worker")){
                    if(god == God.CHARON) {
                        System.out.println("  Do you want to use Charon power? (type yes/no)");
                        if (in.nextLine().toUpperCase().equals("YES")) {
                            lastAction = "Charon Switch";
                        }
                    }
                    else if(god == God.PROMETHEUS) {
                        System.out.println("  Do you want to use Prometheus power? (type yes/no)");
                        if (in.nextLine().toUpperCase().equals("YES")) {
                            lastAction = "Prometheus Build";
                        }
                    }
                    else {
                        lastAction = "Move";
                        moveCounter ++;

                    }

                }
                else if( lastAction.equals("Charon Switch") || lastAction.equals("Prometheus Build") ) lastAction = "Move";
                else if( lastAction.equals("Move")){
                    if( god == God.ARTEMIS && moveCounter ==1 || god == God.TRITON && isPerimetralSpace(lastSpace)){
                        System.out.println("  Do you want to move again? (yes/no)");
                        if (in.nextLine().toUpperCase().equals("YES")) {
                            lastAction = "Move";
                            moveCounter ++;
                        }
                    }
                    else {
                        buildCounter ++;
                        lastAction = "Build";

                    }
                }

                else if (lastAction.equals("Build")){
                    if( (god == God.HEPHAESTUS || god == God.DEMETER) && buildCounter ==1){
                        System.out.println("  Do you want to build again? (yes/no)");
                        if (in.nextLine().toUpperCase().equals("YES")) {
                            lastAction = "Build";
                            buildCounter ++;
                        }
                    }
                    else if ( god == God.POSEIDON ){
                        System.out.println("  Do you want to build again? (yes/no)");
                        if (in.nextLine().toUpperCase().equals("YES") && buildCounter > 0 &&
                            buildCounter <4 && getHeight(serializableLiteGame.getCurrWorker()) == 0 ){
                            lastAction = "Build";
                            buildCounter ++;
                        }
                    }
                    else {
                        moveCounter = 0;
                        buildCounter = 0;
                        lastAction = "End";
                    }
                }

                if(!lastAction.equals("End")) {
                    System.out.println("  Make your " + lastAction + "!" );
                    lastSpace = getSpaceFromClient();
                    clientMessage.setSpace1(lastSpace);
                    if (lastAction.contains("Build")) {
                        System.out.println("  Which level do you want to build? (1-4)");
                        clientMessage.setLevelToBuild(Integer.parseInt(in.nextLine()));
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

            if(!messageFromFrontEnd.equals("Invalid action")) {
                serializableLiteGame = clientConnection.readLiteGame();
                buildGameTable();
            }
        }
        System.out.println(messageFromFrontEnd);
    }




    /**
     * Welcome method: initialize a new settingGameMessage to send to Server
     */

    void welcomeMirror() {
        System.out.println(ColourFont.ANSI_BLUE_BACKGROUND);
        System.out.println(ColourFont.ANSI_BOLD + "  Welcome to Santorini\n  RETRO Version\n\n" + ColourFont.ANSI_RESET + ColourFont.ANSI_BLUE_BACKGROUND);
        clientConnection = new ClientConnection("127.0.0.1", 4700);
        try {
            clientConnection.connect();
            String messageFromServer = "Beginning";
            //welcoming client
            System.out.println(clientConnection.readString());


            System.out.println("  What is your name?\n" + ColourFont.ANSI_RESET);
            while (!messageFromServer.equals("Nickname accepted")) {
                nickname = in.nextLine().toUpperCase();
                settingGameMessage.setNickname(nickname);
                clientConnection.send(settingGameMessage);
                messageFromServer = clientConnection.readString();
                if (messageFromServer.equals("Invalid Nickname")) {
                    System.out.println("  Nickname not available. Choose another nickname\n" + ColourFont.ANSI_RESET);
                }
            }

            while (quit) {
                quit = false;
                while (!mode.equals("A") && !mode.equals("B") && !mode.equals("C")) {
                    System.out.println(ColourFont.ANSI_BLUE_BACKGROUND + "\nPlease " + nickname + ", type A, B or C to choose different options:\n");
                    System.out.println(" - A) CREATE A NEW MATCH\n      Be the challenger of the isle!\n");
                    System.out.println(" - B) PLAY WITH YOUR FRIENDS\n      Play an already existing game!\n");
                    System.out.println(" - C) PLAY WITH STRANGERS\n      Challenge yourself with randomly chosen players!\n");
                    mode = in.nextLine().toUpperCase();
                    if (!mode.equals("A") && !mode.equals("B") && !mode.equals("C"))
                        System.out.println("Dare you challenge the Olympus?? Retry\n ");
                }
                switch (mode) {
                    case "A":
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
                                mode = "start";
                            } else if (!actionA.equals("2") && !actionA.equals("3"))
                                System.out.println("  Dare you challenge the Olympus?? Retry\n ");
                            else numOfPlayers = Integer.parseInt(actionA);
                        }
                        if ( !quit ) {
                            settingGameMessage.setNumberOfPlayer(numOfPlayers);
                            clientConnection.send(settingGameMessage);
                            System.out.println("  The gameID is " + clientConnection.readString() + "\n");
                        }
                        break;
                    case "B":
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
                                mode = "start";
                            } else {
                                gameID = Integer.parseInt(actionB);
                                settingGameMessage.setGameID(gameID);
                            }
                            if ( !quit ) {
                                clientConnection.send(settingGameMessage);
                                messageFromServer = clientConnection.readString();
                                System.out.println("  Server says: " + messageFromServer + "\n");
                                if (messageFromServer.equals("Insert valid gameID"))
                                    System.out.println("  Insert a valid gameID");
                                else validGameId = true;
                            }
                        }
                        break;
                    case "C":
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
                                mode = "start";
                            } else if (!actionC.equals("2") && !actionC.equals("3"))
                                System.out.println("  Dare you challenge the Olympus?? Retry\n ");
                            else numOfPlayers = Integer.parseInt(actionC);
                        }
                        if ( !quit ) {
                            settingGameMessage.setNumberOfPlayer(numOfPlayers);
                            clientConnection.send(settingGameMessage);
                            System.out.println("  Server says: " + clientConnection.readString() + "\n");
                        }
                        break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The Challenger chooses the cards which he wants to play with*
     */

    void challengerChoosesGods(){
        if(mode.equals("A")) {
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

        messageFromFrontEnd = clientConnection.readString();
        if ( mode.equals("A") ){
            god = God.valueOf(messageFromFrontEnd);
            System.out.println("  Your god is " + messageFromFrontEnd);
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
            System.out.println(clientConnection.readString()+ColourFont.ANSI_RESET+"\n\n");
        }
        serializableLiteGame = clientConnection.readLiteGame();
        buildGameTable();
    }

    private void placeWorkers() {
        messageFromFrontEnd = "none";
        while ( !messageFromFrontEnd.equals("Placing workers") ){
            messageFromFrontEnd = clientConnection.readString();
            System.out.println("  "+messageFromFrontEnd);
           if ( messageFromFrontEnd.contains("Wait") ){
               serializableLiteGame = clientConnection.readLiteGame();
               buildGameTable();
           }
        }
        boolean validPlacing = false;
        while (!validPlacing) {
            clientMessage.setSpace1(getSpaceFromClient());
            clientMessage.setSpace2(getSpaceFromClient());
            clientConnection.send(clientMessage);
            newMessage = clientConnection.readLiteGame();
            if(!newMessage.equalsSLG(serializableLiteGame)){
                validPlacing = true;
                serializableLiteGame = newMessage;
            }
            buildGameTable();
            if(!validPlacing) System.out.println("  Please retype two correct spaces!");
        }
    }

    private void chooseWorker() {
        clientMessage.setSpace1(getSpaceFromClient());
        lastAction = "Choose Worker";
        clientMessage.setAction(lastAction);
        clientConnection.send(clientMessage);
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
            }
        }
        return newSpace;
    }

    private boolean isPerimetralSpace(int[] space){
        return space[0]== 0 || space[1] == 0 || space[0]== 4 || space[1] == 4;
    }

    private int getHeight(int[] space) {
        return Integer.parseInt(serializableLiteGame.getTable()[space[0]][space[1]].substring(1,1));
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

    void buildGameTable(){
        String[][] gameTable = serializableLiteGame.getTable();

        System.out.println("        1        2        3        4        5");
        System.out.println("    + = = = ++ = = = ++ = = = ++ = = = ++ = = = +");
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

        String[] newRow = new String[]{
                "    "+space1[0]+space2[0]+space3[0]+space4[0]+space5[0],
                " "+row+"  "+space1[1]+space2[1]+space3[1]+space4[1]+space5[1],
                "    "+space1[2]+space2[2]+space3[2]+space4[2]+space5[2],
                "    "+space1[3]+space2[3]+space3[3]+space4[3]+space5[3]
        };
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

    SettingGameMessage getSettingGameMessage() {
        return settingGameMessage;
    }

    String parseInput(){
        String message = in.nextLine();
        message.toUpperCase();
        String[] parsedMessage = message.split(" ");
        for (String s : parsedMessage){
            message = s + " ";
        }
        return null;
    }

    public void setLiteGame(SerializableLiteGame serializableLiteGame){
        this.serializableLiteGame = serializableLiteGame;
    }

    public SerializableLiteGame getSerializableLiteGame(){
        return this.serializableLiteGame;
    }
}

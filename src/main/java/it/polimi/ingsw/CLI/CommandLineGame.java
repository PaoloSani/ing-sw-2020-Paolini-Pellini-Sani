package it.polimi.ingsw.CLI;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.SettingGameMessage;
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


    public void runCLI(){
        welcomeMirror();
        //messaggio di inizio partita
        System.out.println(clientConnection.readString());
        //metodo per l'inizio della partita e la scelta delle carte
        challengerChoosesGods();
        chooseCard();
        serializableLiteGame = clientConnection.readLiteGame();
        placeWorkers();
        initializeGameTable();
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
                            System.out.println("The gameID is " + clientConnection.readString() + "\n");
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
                                System.out.println("Server says: " + messageFromServer + "\n");
                                if (messageFromServer.equals("Insert valid gameID"))
                                    System.out.println("Insert a valid gameID");
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
                            System.out.println("Server says: " + clientConnection.readString() + "\n");
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
                System.out.println("");
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
            System.out.println("Now wait for other players to choose their cards");
            clientConnection.send(challengerMessage);

        }
        else System.out.println("  Please, wait the Challenger to choose the Pantheon");
    }

    private void chooseCard() {

        String messageFromFrontEnd = clientConnection.readString();
        if ( mode.equals("A") ){
            god = God.valueOf(messageFromFrontEnd);
            System.out.println("Your god is " + messageFromFrontEnd);
        }
        else {
            String choice = "none";

            while ( !messageFromFrontEnd.contains(choice) ){
                System.out.println("Choose your god! Available gods: " + messageFromFrontEnd);
                choice = in.nextLine().toUpperCase();
            }
            god = God.valueOf(choice);
            //in realtà settare il nickname è superfluo perché il frontend già conosce i nickname dei client
            clientMessage.setName(nickname);
            clientMessage.setGod(god);
            clientConnection.send(clientMessage);
            System.out.println(clientConnection.readString());
        }
    }

    private void placeWorkers() {
        String messageFromFrontEnd = "none";
        while ( !messageFromFrontEnd.equals("Placing workers") ){
            messageFromFrontEnd = clientConnection.readString();
            System.out.println(messageFromFrontEnd);
           if ( messageFromFrontEnd.contains("Wait") ){
               serializableLiteGame = clientConnection.readLiteGame();
           }
        }
        clientMessage.setSpace1(getSpaceFromClient());
        clientMessage.setSpace2(getSpaceFromClient());
        clientConnection.send(clientMessage);
        serializableLiteGame = clientConnection.readLiteGame();
    }

    private int[] getSpaceFromClient(){
        int[] newSpace = new int[]{5,5};
        //TODO: migliorare controlli sulle celle disponibili e messaggio di errore al client
        while ( newSpace[0] < 0 || newSpace[0] > 4 || newSpace[1] < 0 || newSpace[1] > 4 ) {
            System.out.println("Insert the space coordinates (NUM NUM): \n ");
            String space = in.nextLine();
            String[] coord = space.split(" ");
            newSpace = new int[]{Integer.parseInt(coord[0]), Integer.parseInt(coord[1])};
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
        System.out.println("  - DOME:         " + ColourFont.ANSI_DOME + "    " + ColourFont.ANSI_RESET + ColourFont.ANSI_BLACK_BACKGROUND + "\n");

    }

    void buildGameTable(){
        String[][] gameTable = serializableLiteGame.getTable();

        System.out.println("        1        2        3        4        5");
        System.out.println("    + = = = ++ = = = ++ = = = ++ = = = ++ = = = +");
        for (int i = 0; i < 5; i++){
            buildTableRow(gameTable[i],i+1);
        }
    }

    void buildTableRow(String[] serializableLiteGameRow, int x){
        String[] space1 = buildGameSpace(serializableLiteGameRow[0]);
        String[] space2 = buildGameSpace(serializableLiteGameRow[1]);
        String[] space3 = buildGameSpace(serializableLiteGameRow[2]);
        String[] space4 = buildGameSpace(serializableLiteGameRow[3]);
        String[] space5 = buildGameSpace(serializableLiteGameRow[4]);

        String[] row = new String[]{
                "    "+space1[0]+space2[0]+space3[0]+space4[0]+space5[0],
                " "+x+"  "+space1[1]+space2[1]+space3[1]+space4[1]+space5[1],
                "    "+space1[2]+space2[2]+space3[2]+space4[2]+space5[2],
                "    "+space1[3]+space2[3]+space3[3]+space4[3]+space5[3]
        };
        for (String s : row){
            System.out.println(s);
        }
    }

    String[] buildGameSpace(String spaceFromLiteGame){
        ColourFont background = ColourFont.ANSI_MENU_BACKGROUND;
        ColourFont foreground = ColourFont.ANSI_WHITE_TEXT;
        String dome = "";
        String reset = ColourFont.ANSI_RESET;
        String player;
        char[] spaceAnalyzer = spaceFromLiteGame.toCharArray();

        if (spaceAnalyzer[1] == '0') background = ColourFont.ANSI_GREEN_BACKGROUND;
        else if (spaceAnalyzer[1] == '1') background = ColourFont.ANSI_LEVEL1;
        else if (spaceAnalyzer[1] == '2') background = ColourFont.ANSI_LEVEL2;
        else if (spaceAnalyzer[1] == '3') {
            background = ColourFont.ANSI_LEVEL3;
            foreground = ColourFont.ANSI_BLACK_TEXT;
        }
        if (spaceAnalyzer[2] == 'D') dome = ColourFont.ANSI_DOME.toString();
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

        String[] gameSpace = new String[]{
                                          "|"+background.toString()+foreground.toString()+" "+dome+"     "+background.toString()+" "+reset+"|",
                                          "|"+background.toString()+foreground.toString()+" "+dome +" "+player+" "+background.toString()+" "+reset+"|",
                                          "|"+background.toString()+foreground.toString()+" "+dome+"     "+background.toString()+" "+reset+"|",
                                          "+ = = = +"
        };
        return gameSpace;
    }


    void initializeGameTable(){
        printKeysTable();
        System.out.println(                                      "         1       2       3       4       5");
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " +-------+-------+-------+-------+-------+ " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("  1 " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " +-------+-------+-------+-------+-------+ " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("  2 " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " +-------+-------+-------+-------+-------+ " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("  3 " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " +-------+-------+-------+-------+-------+ " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("  4 " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " +-------+-------+-------+-------+-------+ " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("  5 " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " |       |       |       |       |       | " + ColourFont.ANSI_RESET);
        System.out.println("    " + ColourFont.ANSI_GREEN_BACKGROUND + " +-------+-------+-------+-------+-------+ " + ColourFont.ANSI_RESET + "\n\n");

    }

    SettingGameMessage getSettingGameMessage(){
        return  settingGameMessage;
    }

    void setLiteGame(SerializableLiteGame serializableLiteGame){
        this.serializableLiteGame = serializableLiteGame;
    }

}

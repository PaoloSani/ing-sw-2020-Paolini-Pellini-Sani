package it.polimi.ingsw.CLI;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.SettingGameMessage;
import java.io.IOException;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.LiteGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineGame {

    private final Scanner in = new Scanner(System.in);
    private String nickname;
    private String mode = "start";
    private int numOfPlayers;
    private int gameID;
    private SettingGameMessage settingGameMessage = new SettingGameMessage();
    private boolean quit = true;
    private ClientConnection clientConnection;
    private String[] challengerMessage;
    private LiteGame liteGame;
    private ClientMessage clientMessage;


    public void runCLI(){
        welcomeMirror();
        System.out.println(clientConnection.readString());
        challengerChoosesGods();

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
                nickname = in.nextLine();
                nickname = nickname.toUpperCase();
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
                            actionA.toUpperCase();
                            if (actionA.equals("QUIT")) {
                                quit = true;
                                mode = "start";
                            } else if (!actionA.equals("2") && !actionA.equals("3"))
                                System.out.println("  Dare you challenge the Olympus?? Retry\n ");
                            else numOfPlayers = Integer.parseInt(actionA);
                        }
                        clientConnection.send(settingGameMessage);
                        System.out.println("The gameID is " + clientConnection.readString() + "\n");
                        break;
                    case "B":
                        settingGameMessage.setPlayingExistingMatch(true);
                        settingGameMessage.setCreatingNewGame(false);
                        System.out.println("  Type the game ID");
                        System.out.println("  Type quit to return back!\n");
                        boolean validGameId = false;
                        while (!validGameId){
                            String actionB = in.nextLine();
                            actionB = actionB.toUpperCase();
                            if (actionB.equals("QUIT")) {
                                quit = true;
                                mode = "start";
                            } else {
                                gameID = Integer.parseInt(actionB);
                                settingGameMessage.setGameID(gameID);
                            }
                            clientConnection.send(settingGameMessage);
                            messageFromServer = clientConnection.readString();
                            System.out.println("Server says: " + messageFromServer + "\n");
                            if (messageFromServer.equals("Insert valid gameID"))
                                System.out.println("Insert a valid gameID");
                            else validGameId = true;
                        }
                        break;
                    case "C":
                        settingGameMessage.setCreatingNewGame(false);
                        settingGameMessage.setPlayingExistingMatch(false);
                        settingGameMessage.setGameID(0);
                        while (numOfPlayers != 2 && numOfPlayers != 3 && !quit) {
                            System.out.println("  Choose the number of players (2 or 3)\n");
                            String actionC = in.nextLine();
                            actionC = actionC.toUpperCase();
                            if (actionC.equals("QUIT")) {
                                quit = true;
                                mode = "start";
                            } else if (!actionC.equals("2") && !actionC.equals("3"))
                                System.out.println("  Dare you challenge the Olympus?? Retry\n ");
                            else numOfPlayers = Integer.parseInt(actionC);
                        }
                        settingGameMessage.setNumberOfPlayer(numOfPlayers);
                        clientConnection.send(settingGameMessage);
                        System.out.println("Server says: " + clientConnection.readString() + "\n");

                        break;
                }
            }
            settingGameMessage.setNumberOfPlayer(numOfPlayers);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * The Challenger chooses the card which he wants to play with*
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
                    System.out.println(ColourFont.ANSI_BOLD + "  Please, type a valid God!\n" + ColourFont.ANSI_RESET + ColourFont.ANSI_CYAN_BACKGROUND);
                }
            }
            if (numOfPlayers == 2) {
                challengerMessage =
                        new String[]{chosenGods.get(0).toString(), chosenGods.get(1).toString()};
            } else {
                challengerMessage =
                        new String[]{chosenGods.get(0).toString(), chosenGods.get(1).toString(), chosenGods.get(2).toString()};
            }
        }
        else System.out.println("  Please, wait the Challenger to choose the Pantheon");
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

}

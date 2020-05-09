package it.polimi.ingsw.CLI;

import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.SettingGameMessage;
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
    private String[] challengerMessage;
    private LiteGame liteGame;
    private ClientMessage clientMessage;


    public void startCLI(){
        welcomeMirror();
        waitGame();
        challengerChoosesGods();
    }

    /**
     * Welcome method: initialize a new settingGameMessage to send to Server
     */

    public void welcomeMirror(){
        System.out.println(ColourFont.ANSI_CYAN_BACKGROUND);
        System.out.println(ColourFont.ANSI_BOLD + "  Welcome to Santorini\n  RETRO Version\n\n");
        System.out.println("  What is your name?\n\n" + ColourFont.ANSI_RESET);
        nickname = in.nextLine();
        settingGameMessage.setNickname(nickname);
        while(quit) {
            quit = false;
            while (!mode.equals("A") && !mode.equals("B") && !mode.equals("C")) {
                System.out.println(ColourFont.ANSI_CYAN_BACKGROUND + "\nPlease " + nickname + ", type A, B or C to choose different options:\n");
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
                        }
                    else if (!actionA.equals("2") && !actionA.equals("3"))
                            System.out.println("  Dare you challenge the Olympus?? Retry\n ");
                        else numOfPlayers = Integer.parseInt(actionA);
                    }
                    break;
                case "B":
                    settingGameMessage.setPlayingExistingMatch(true);
                    settingGameMessage.setCreatingNewGame(false);
                    System.out.println("  Type the game ID");
                    System.out.println("  Type quit to return back!\n");
                    String actionB = in.nextLine();
                    actionB = actionB.toUpperCase();
                    if (actionB.equals("QUIT")) {
                        quit = true;
                        mode = "start";
                    }
                    else {
                        gameID = Integer.parseInt(actionB);
                        settingGameMessage.setGameID(gameID);
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
                        }
                        else if (!actionC.equals("2") && !actionC.equals("3"))
                            System.out.println("  Dare you challenge the Olympus?? Retry\n ");
                        else numOfPlayers = Integer.parseInt(actionC);
                    }
                    break;
            }
        }
        settingGameMessage.setNumberOfPlayer(numOfPlayers);
    }

    public void waitGame(){

    }

    /**
     * The Challenger chooses the card which he wants to play with*
     */

    public void challengerChoosesGods(){
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

    public void printGameTable(){
        System.out.println(ColourFont.ANSI_BOLD+"  KEYS:\n"+ColourFont.ANSI_RESET+ColourFont.ANSI_CYAN_BACKGROUND);
        System.out.println("  - ground level: " + ColourFont.ANSI_GREEN_BACKGROUND + "  \n" + ColourFont.ANSI_RESET + ColourFont.ANSI_CYAN_BACKGROUND);
        System.out.println("  - first level: " + ColourFont.ANSI_GREEN_BACKGROUND + "  \n" + ColourFont.ANSI_RESET + ColourFont.ANSI_CYAN_BACKGROUND);
    }

    SettingGameMessage getSettingGameMessage(){
        return  settingGameMessage;
    }

}

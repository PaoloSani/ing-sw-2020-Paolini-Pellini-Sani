package it.polimi.ingsw.client;

import java.util.Scanner;

public class CommandLineGame {

    private final Scanner in = new Scanner(System.in);
    private String nickname;
    private String mode = "start";
    private int numOfPlayers;
    private int gameID;
    private SettingGameMessage settingGameMessage = new SettingGameMessage();
    private boolean quit = true;

    public void startCLI(){
        welcomeMirror();
    }

    public void welcomeMirror(){
        System.out.println(ColourFont.ANSI_CYAN_BACKGROUND);
        System.out.println(ColourFont.ANSI_BOLD + "  Welcome to Santorini\n  RETRO Version\n\n");
        System.out.println("  What is your name?\n" + ColourFont.ANSI_RESET);
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


}

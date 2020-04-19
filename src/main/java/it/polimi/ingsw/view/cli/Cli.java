package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.UserInterface;

import java.util.Scanner;

public class Cli implements UserInterface
{
    @Override
    public void showLogo()
    {
        CliUtils.printBlueOnWhite("      ______    ______   __    __  ________  ______   _______   ______  __    __  ______     ");
        CliUtils.printBlueOnWhite("     /      \\  /      \\ /  \\  /  |/        |/      \\ /       \\ /      |/  \\  /  |/      |    ");
        CliUtils.printBlueOnWhite("    /$$$$$$  |/$$$$$$  |$$  \\ $$ |$$$$$$$$//$$$$$$  |$$$$$$$  |$$$$$$/ $$  \\ $$ |$$$$$$/     ");
        CliUtils.printBlueOnWhite("    $$ \\__$$/ $$ |__$$ |$$$  \\$$ |   $$ |  $$ |  $$ |$$ |__$$ |  $$ |  $$$  \\$$ |  $$ |      ");
        CliUtils.printBlueOnWhite("    $$      \\ $$    $$ |$$$$  $$ |   $$ |  $$ |  $$ |$$    $$<   $$ |  $$$$  $$ |  $$ |      ");
        CliUtils.printBlueOnWhite("     $$$$$$  |$$$$$$$$ |$$ $$ $$ |   $$ |  $$ |  $$ |$$$$$$$  |  $$ |  $$ $$ $$ |  $$ |      ");
        CliUtils.printBlueOnWhite("    /  \\__$$ |$$ |  $$ |$$ |$$$$ |   $$ |  $$ \\__$$ |$$ |  $$ | _$$ |_ $$ |$$$$ | _$$ |_     ");
        CliUtils.printBlueOnWhite("    $$    $$/ $$ |  $$ |$$ | $$$ |   $$ |  $$    $$/ $$ |  $$ |/ $$   |$$ | $$$ |/ $$   |    ");
        CliUtils.printBlueOnWhite("     $$$$$$/  $$/   $$/ $$/   $$/    $$/    $$$$$$/  $$/   $$/ $$$$$$/ $$/   $$/ $$$$$$/     ");
        CliUtils.printBlueOnWhite("                                                                                             ");
        System.out.println(AnsiColors.ANSI_RESET);
    }

    @Override
    public void askNumPlayers(){
        CliUtils.printBlueOnWhiteSameLine("Choose the number of players:");
    }

    //wrong input is handled by ConnectionState
    @Override
    public int readNumPlayers(){
        int numPlayers = readInt();
        return numPlayers;
    }

    private int readInt(){
        Scanner stdin = new Scanner(System.in);
        int num = stdin.nextInt();
        System.out.println();
        return num;
    }

    @Override
    public void askUsername() {
        CliUtils.printBlueOnWhiteSameLine("Choose a username:");
    }

    @Override
    public String readUsername(){
        String name = readString();
        return name;
    }

    //utility function used to get a string from user input
    public String readString(){
        Scanner stdin = new Scanner(System.in);
        String word = stdin.nextLine();
        System.out.println();
        return word;

    }

    public void showCustomError(String s)
    {
        CliUtils.printRedOnWhite(s);
        System.out.println();
    }

    public void showUsernameError()
    {
        showCustomError("Error! Username not available.");
    }
}

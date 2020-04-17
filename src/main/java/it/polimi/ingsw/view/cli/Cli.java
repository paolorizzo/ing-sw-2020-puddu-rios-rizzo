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

    //TODO handle wrong input
    @Override
    public int getNumPlayers()
    {
        CliUtils.printBlueOnWhiteSameLine("Choose the number of players:");
        Scanner stdin = new Scanner(System.in);
        int num = stdin.nextInt();
        System.out.println();
        return num;
    }

    //TODO handle wrong input
    @Override
    public String getUsername() {
        CliUtils.printBlueOnWhiteSameLine("Choose an username:");
        Scanner stdin = new Scanner(System.in);
        String name = stdin.nextLine();
        System.out.println();
        return name;
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

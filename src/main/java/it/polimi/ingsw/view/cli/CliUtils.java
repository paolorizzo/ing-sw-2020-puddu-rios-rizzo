package it.polimi.ingsw.view.cli;

public class CliUtils
{
    static void printBlueOnWhite(String s)
    {
        System.out.print(AnsiColors.ANSI_BLUE + AnsiColors.ANSI_BG_BLACK + s);
        System.out.println(AnsiColors.ANSI_RESET);
    }

    static void printRedOnWhite(String s)
    {
        System.out.print(AnsiColors.ANSI_RED + AnsiColors.ANSI_BG_BLACK + s);
        System.out.println(AnsiColors.ANSI_RESET);
    }

    static void printBlueOnWhiteSameLine(String s)
    {
        System.out.print(AnsiColors.ANSI_BLUE + AnsiColors.ANSI_BG_BLACK + s);
        System.out.print(AnsiColors.ANSI_RESET);
        System.out.print(" ");
    }
}

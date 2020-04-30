package it.polimi.ingsw.view.cli;

public class SpaceCLI
{
    public static void buildLevel(RectangleCLI figure, int level) {
        switch (level) {
            case 0:
                figure.setPalette(AnsiColors.ANSI_BG_GREEN);
                break;
            case 1:
                figure.setPalette(AnsiColors.ANSI_BG_BLACK);
                break;
            case 2:
                figure.setMask("./src/main/resources/level2.txt");
                figure.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BG_WHITE);
                break;
            case 3:
                figure.setMask("./src/main/resources/level3.txt");
                figure.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BRIGHT_BG_BLACK, AnsiColors.ANSI_BG_WHITE);
                break;
            case 4:
                figure.setMask("./src/main/resources/level3.txt");
                figure.setPalette(AnsiColors.ANSI_BG_BLUE, AnsiColors.ANSI_BRIGHT_BG_BLACK, AnsiColors.ANSI_BG_WHITE);
                break;
            default:
                figure.setPalette(AnsiColors.ANSI_BG_RED);
                break;
        }
    }
}

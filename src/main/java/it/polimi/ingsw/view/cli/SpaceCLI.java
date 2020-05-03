package it.polimi.ingsw.view.cli;

public class SpaceCLI
{
    private int x;
    private int y;
    private int level;

    public SpaceCLI()
    {
        this.x = 0;
        this.y = 0;
        this.level = 0;
    }

    public SpaceCLI(int x, int y, int level)
    {
        this.x = x;
        this.y = y;
        this.level = level;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getLevel()
    {
        return this.level;
    }

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

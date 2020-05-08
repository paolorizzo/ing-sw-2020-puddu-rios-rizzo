package it.polimi.ingsw.view.cli;

public class GraphicsElementsCLI
{
    public static void drawLevel(RectangleCLI figure, int level)
    {
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

    public static void drawGod(RectangleCLI pic, String title)
    {
        switch(title)
        {
            case "PROMETHEUS":
                pic.setMask("./src/main/resources/prometheus.txt");
                pic.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BRIGHT_BG_RED, AnsiColors.ANSI_BG_RED, AnsiColors.ANSI_BRIGHT_BG_YELLOW, AnsiColors.ANSI_BG_YELLOW, AnsiColors.ANSI_RESET, AnsiColors.ANSI_BRIGHT_BG_BLACK);
                break;
            case "ATLAS":
                pic.setMask("./src/main/resources/atlas.txt");
                pic.setPalette(AnsiColors.ANSI_BRIGHT_BG_CYAN, AnsiColors.ANSI_BG_WHITE, AnsiColors.ANSI_RESET, AnsiColors.ANSI_BRIGHT_BG_GREEN, AnsiColors.ANSI_BG_GREEN, AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BG_BLUE);
                break;
            case "ATHENA":
                pic.setMask("./src/main/resources/athena.txt");
                pic.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BRIGHT_BG_RED, AnsiColors.ANSI_BG_RED, AnsiColors.ANSI_BRIGHT_BG_YELLOW, AnsiColors.ANSI_BG_YELLOW, AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BRIGHT_BG_BLACK, AnsiColors.ANSI_RESET);
                break;
            case "DEMETER":
                pic.setMask("./src/main/resources/demeter.txt");
                pic.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_RESET, AnsiColors.ANSI_BRIGHT_BG_RED, AnsiColors.ANSI_BG_RED, AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BG_BLUE, AnsiColors.ANSI_BRIGHT_BG_BLACK, AnsiColors.ANSI_BG_YELLOW);
                break;
            case "MINOTAUR":
                pic.setMask("./src/main/resources/minotaur.txt");
                pic.setPalette(AnsiColors.ANSI_BG_BLACK, AnsiColors.ANSI_BRIGHT_BG_BLACK, AnsiColors.ANSI_BG_RED, AnsiColors.ANSI_RESET);
                break;
            case "ZEUS":
                pic.setMask("./src/main/resources/zeus.txt");
                pic.setPalette(AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BG_BLUE, AnsiColors.ANSI_BRIGHT_BG_YELLOW, AnsiColors.ANSI_BG_YELLOW, AnsiColors.ANSI_RESET, AnsiColors.ANSI_BG_WHITE);
                break;
            case "HEPHAESTUS":
                pic.setMask("./src/main/resources/efesto.txt");
                pic.setPalette(AnsiColors.ANSI_BG_RED, AnsiColors.ANSI_BRIGHT_BG_RED, AnsiColors.ANSI_RESET, AnsiColors.ANSI_BG_YELLOW, AnsiColors.ANSI_BG_WHITE);
                break;
            case "APOLLO":
                pic.setMask("./src/main/resources/pan.txt");
                pic.setPalette(AnsiColors.ANSI_BRIGHT_BG_CYAN, AnsiColors.ANSI_BG_CYAN, AnsiColors.ANSI_BG_PURPLE, AnsiColors.ANSI_BRIGHT_BG_YELLOW, AnsiColors.ANSI_BG_YELLOW, AnsiColors.ANSI_BG_GREEN, AnsiColors.ANSI_BRIGHT_BG_PURPLE, AnsiColors.ANSI_BG_BLUE);
                break;
            default:
                pic.setPalette(AnsiColors.ANSI_BG_BLUE);
                break;
        }
    }

    public static void drawArrow(RectangleCLI arrow, int userSelection, boolean right)
    {
        if(right)
        {
            arrow.setMask("./src/main/resources/arrow.txt");
            arrow.setPalette(AnsiColors.ANSI_RESET, userSelection == 2? AnsiColors.ANSI_BG_RED : AnsiColors.ANSI_BG_WHITE);
        }
        else
        {
            arrow.setMask("./src/main/resources/arrow2.txt");
            arrow.setPalette(AnsiColors.ANSI_RESET, userSelection == 0? AnsiColors.ANSI_BG_RED : AnsiColors.ANSI_BG_WHITE);
        }
    }

    public static void drawCard(RectangleCLI card)
    {
        card.setMask("./src/main/resources/card.txt");
        card.setPalette(AnsiColors.ANSI_BG_YELLOW, AnsiColors.ANSI_BRIGHT_BG_YELLOW);
    }

    public static void drawNumber(RectangleCLI figure, int num, int selection)
    {
        if(num==2)
        {
            figure.setMask("./src/main/resources/two.txt");
            figure.setPalette(selection == 2? AnsiColors.ANSI_BG_BLUE : AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BRIGHT_BG_BLACK);
        }
        else if(num == 3)
        {
            figure.setMask("./src/main/resources/three.txt");
            figure.setPalette(selection == 3? AnsiColors.ANSI_BG_BLUE : AnsiColors.ANSI_BRIGHT_BG_BLUE, AnsiColors.ANSI_BRIGHT_BG_BLACK);
        }
    }

    public static void drawWorker(RectangleCLI worker, char player)
    {
        worker.setMask("./src/main/resources/worker.txt");

        switch(player)
        {
            case '0':
                worker.setPalette(AnsiColors.ANSI_BRIGHT_BG_CYAN, AnsiColors.ANSI_BG_CYAN);
                break;
            case '1':
                worker.setPalette(AnsiColors.ANSI_BRIGHT_BG_PURPLE, AnsiColors.ANSI_BG_PURPLE);
                break;
            case '2':
                worker.setPalette(AnsiColors.ANSI_BG_RED, AnsiColors.ANSI_BRIGHT_BG_RED);
                break;
            default:
                break;
        }
    }
}
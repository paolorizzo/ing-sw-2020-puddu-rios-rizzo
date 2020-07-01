package it.polimi.ingsw.view.cli;

public class CanvasCLI extends RectangleCLI
{
    private String textColor;

    public CanvasCLI(int OriginX, int OriginY, int sideX, int sideY)
    {
        super(OriginX, OriginY, sideX, sideY);
        textColor = AnsiColors.ANSI_BLUE;
    }

    public CanvasCLI()
    {
        super(0,0,36,36);
        setPalette(AnsiColors.ANSI_BG_BLACK);
        textColor = AnsiColors.ANSI_BLUE;
    }

    private void printUnit(PrintCLI printer, int x, int y)
    {
        printer.setBg(getTopLayerColor(x,y));
        printer.setTextColor(textColor);
        printer.print(getTopLayerText(x,y));
    }

    public void printFigure()
    {
        PrintCLI printer = new PrintCLI();
        printer.setDefaultContent("   ");

        for(int y = 0; y<sideY; y++)
        {
            for(int x = 0; x < sideX; x++)
            {
                printUnit(printer,x,y);
            }
            printer.resetAndBreak();
        }
    }

    public void setTextColor(String textColor)
    {

        this.textColor = textColor;
    }
}

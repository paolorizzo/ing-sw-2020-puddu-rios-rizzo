package it.polimi.ingsw.view.cli;

/**
 * Extends the RectangleCLI class with custom methods that allow the user to print the overlapped figures on the terminal.
 */
public class CanvasCLI extends RectangleCLI
{
    private String textColor;

    /**
     * Constructs a CanvasCLI object.
     * @param OriginX the starting x position of the canvas in the frame of the terminal window.
     * @param OriginY the starting y position of the canvas in the frame of the terminal window.
     * @param sideX the horizontal dimension of the canvas.
     * @param sideY the vertical dimension of the canvas.
     */
    public CanvasCLI(int OriginX, int OriginY, int sideX, int sideY)
    {
        super(OriginX, OriginY, sideX, sideY);
        textColor = AnsiColors.ANSI_BLUE;
    }

    /**
     * Constructs a CanvasCLI object with default parameters.
     */
    public CanvasCLI()
    {
        super(0,0,36,36);
        setPalette(AnsiColors.ANSI_BG_BLACK);
        textColor = AnsiColors.ANSI_BLUE;
    }

    /**
     * Prints a single unit of the picture stored.
     * @param printer the printCLI object for a formatted printing.
     * @param x the x coordinate of the unit in the picture.
     * @param y the y coordinate of the unit in the picture.
     */
    private void printUnit(PrintCLI printer, int x, int y)
    {
        printer.setBg(getTopLayerColor(x,y));
        printer.setTextColor(textColor);
        printer.print(getTopLayerText(x,y));
    }

    /**
     * Prints to screen the figure stored in the canvas.
     */
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

    /**
     * Sets the color for the text to be printed on the canvas.
     * @param textColor the ANSI color String.
     */
    public void setTextColor(String textColor)
    {

        this.textColor = textColor;
    }
}

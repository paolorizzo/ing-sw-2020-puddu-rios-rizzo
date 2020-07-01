package it.polimi.ingsw.view.cli;

/**
 * Contains utilities to specify a style in printing text on the CLI.
 */
public class PrintCLI
{
    private String bg;
    private String textColor;
    private String defaultContent;

    /**
     * Constructs a PrintCLI object with default values.
     */
    public PrintCLI()
    {
        this.bg = "";
        this.textColor = "";
        this.defaultContent = "";
    }

    /**
     * Constructs a PrintCLI object with a specific background color.
     * @param bg the ANSI color string.
     */
    public PrintCLI(String bg)
    {
        this.bg = bg;
        this.textColor = "";
        this.defaultContent = "";
    }

    /**
     * Constructs a PrintCLI object with specific background and text color.
     * @param bg the ANSI color string for the background.
     * @param textColor the ANSI color string for the text.
     */
    public PrintCLI(String bg, String textColor)
    {
        this.bg = bg;
        this.textColor = textColor;
        this.defaultContent = "";
    }

    /**
     * Constructs a PrintCLI object with specific background, text color and content.
     * @param bg the ANSI color string for the background.
     * @param textColor the ANSI color string for the text.
     * @param defaultContent the default content to print if a text is not specified in calls.
     */
    public PrintCLI(String bg, String textColor, String defaultContent)
    {
        this.bg = bg;
        this.textColor = textColor;
        this.defaultContent = defaultContent;
    }

    /**
     * Sets the background color for the text.
     * @param bg the ANSI color string.
     */
    public void setBg(String bg)
    {

        this.bg = bg;
    }

    /**
     * Sets the text color.
     * @param textColor the ANSI color string.
     */
    public void setTextColor(String textColor)
    {

        this.textColor = textColor;
    }

    /**
     * Sets the default content to print if a text is not specified in calls.
     * @param defaultContent the default content String.
     */
    public void setDefaultContent(String defaultContent)
    {

        this.defaultContent = defaultContent;
    }

    /**
     * Resets the color and ends the line.
     */
    public void resetAndBreak()
    {

        System.out.println(AnsiColors.ANSI_RESET);
    }

    /**
     * Prints a String with the style specified by the parameters, ending the line at the end.
     * @param s the String to print.
     */
    public void println(String s)
    {

        System.out.println(textColor + bg + s);
    }

    /**
     * Prints the default content with the style specified by the parameters.
     */
    public void printDefault()
    {

        print(defaultContent);
    }

    /**
     * Prints the default content with the style specified by the parameters and then ends the line.
     */
    public void printlnDefault()
    {

        println(defaultContent);
    }

    /**
     * Prints a String with the style specified by the parameters.
     * @param s the String to print.
     */
    public void print(String s)
    {

        System.out.print(textColor + bg + s);
    }

    /**
     * Ends the line.
     */
    public void lineBreak()
    {

        System.out.println();
    }
}

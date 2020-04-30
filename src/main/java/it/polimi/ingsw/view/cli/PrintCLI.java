package it.polimi.ingsw.view.cli;

public class PrintCLI
{
    private String bg;
    private String textColor;
    private String defaultContent;

    //constructors
    public PrintCLI()
    {
        this.bg = "";
        this.textColor = "";
        this.defaultContent = "";
    }

    public PrintCLI(String bg)
    {
        this.bg = bg;
        this.textColor = "";
        this.defaultContent = "";
    }

    public PrintCLI(String bg, String textColor)
    {
        this.bg = bg;
        this.textColor = textColor;
        this.defaultContent = "";
    }

    public PrintCLI(String bg, String textColor, String defaultContent)
    {
        this.bg = bg;
        this.textColor = textColor;
        this.defaultContent = defaultContent;
    }


    //modifiers
    public void setBg(String bg)
    {

        this.bg = bg;
    }

    public void setTextColor(String textColor)
    {

        this.textColor = textColor;
    }

    public void setDefaultContent(String defaultContent)
    {

        this.defaultContent = defaultContent;
    }


    //functions
    public void resetAndBreak()
    {

        System.out.println(AnsiColors.ANSI_RESET);
    }

    public void println(String s)
    {

        System.out.println(textColor + bg + s);
    }

    public void printDefault()
    {

        print(defaultContent);
    }

    public void printlnDefault()
    {

        println(defaultContent);
    }

    public void print(String s)
    {

        System.out.print(textColor + bg + s);
    }

    public void lineBreak()
    {

        System.out.println();
    }
}

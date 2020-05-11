package it.polimi.ingsw.view.cli;

/**
 * A CLI-oriented representation of a choice between a set of prior-defined possibilities.
 */
public class SelectionCLI
{
    protected int x;
    protected int xmax;
    /**
     * If true, the selection will restart from the first position when reaching the limit. 
     */
    protected boolean xcircular; // 0-1-2-3-0-1-2-3

    public SelectionCLI()
    {
        this.x = 0;
        this.xmax = 0;
        this.xcircular = false;
    }

    public SelectionCLI(int x, int xmax, boolean xcircular)
    {
        this.x = x;
        this.xmax = xmax;
        this.xcircular = xcircular;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setXmax(int xmax)
    {
        this.xmax = xmax;
    }

    public void setXcircular(boolean xcircular)
    {
        this.xcircular = xcircular;
    }

    public int getX()
    {
        return this.x;
    }

    public void addX()
    {
        handleWASD("d");
    }

    public void subX()
    {

        handleWASD("a");
    }

    public void handleWASD(String input)
    {
        switch(input)
        {
            case "a":
                if(x == 0 && xcircular)
                {
                    x = xmax;
                }
                else if(x != 0)
                {
                    x--;
                }

                break;
            case "d":
                if(x == xmax && xcircular)
                {
                    x = 0;
                }
                else if(x != xmax)
                {
                    x++;
                }
                break;
            default:
                break;
        }
    }

}

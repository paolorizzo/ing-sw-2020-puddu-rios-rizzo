package it.polimi.ingsw.view.cli;

public class BidimensionalSelectionCLI extends SelectionCLI
{
    private int y;
    private int ymax;
    private boolean ycircular;

    public BidimensionalSelectionCLI()
    {
        super();
        this.y = 0;
        this.ymax = 0;
        this.ycircular = false;
    }

    public BidimensionalSelectionCLI(int x, int xmax, boolean xcircular, int y, int ymax, boolean ycircular)
    {
        super(x, xmax, xcircular);
        this.y = y;
        this.ymax = ymax;
        this.ycircular = ycircular;
    }

    public void setY(int y)
    {

        this.y = y;
    }

    public void setYmax(int ymax)
    {

        this.ymax = ymax;
    }

    public void setYcircular(boolean ycircular)
    {

        this.ycircular = ycircular;
    }

    public int getY()
    {

        return y;
    }

    @Override
    public void handleWASD(String input)
    {
        switch(input.toLowerCase())
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
            case "s":
                if(y == ymax && ycircular)
                {
                    y = 0;
                }
                else if(y != ymax)
                {
                    y++;
                }
                break;
            case "w":
                if(y == 0 && ycircular)
                {
                    y = ymax;
                }
                else if(y != 0)
                {
                    y--;
                }
                break;
            default:
                break;
        }
    }
}

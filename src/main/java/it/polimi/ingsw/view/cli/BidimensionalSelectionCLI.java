package it.polimi.ingsw.view.cli;

/**
 * Extends the SelectionCLI class for modeling bi-dimensional selections.
 */
public class BidimensionalSelectionCLI extends SelectionCLI
{
    private int y;
    private int ymax;
    private boolean ycircular;

    /**
     * Constructs a bi-dimensional selection object.
     */
    public BidimensionalSelectionCLI()
    {
        super();
        this.y = 0;
        this.ymax = 0;
        this.ycircular = false;
    }

    /**
     * Constructs a bi-dimensional selection object.
     * @param x starting index on the first dimension.
     * @param xmax range for the first dimension.
     * @param xcircular flag indicating if, once reached the limit of the first dimension's range, the index should restart from the beginning.
     * @param y starting index on the second dimension.
     * @param ymax range for the second dimension.
     * @param ycircular flag indicating if, once reached the limit of the second dimension's range, the index should restart from the beginning.
     */
    public BidimensionalSelectionCLI(int x, int xmax, boolean xcircular, int y, int ymax, boolean ycircular)
    {
        super(x, xmax, xcircular);
        this.y = y;
        this.ymax = ymax;
        this.ycircular = ycircular;
    }

    /**
     * Sets the index for the second dimension.
     * @param y the index number.
     */
    public void setY(int y)
    {
        if(y <= this.ymax)
            this.y = y;
    }

    /**
     * Sets the range for the second dimension.
     * @param ymax the number indicating the max index reachable in the second dimension.
     */
    public void setYmax(int ymax)
    {
        if(ymax >= 0)
            this.ymax = ymax;
    }

    /**
     * Sets the flag indicating if, once reached the limit of the second dimension's range, the index should restart from the beginning.
     * @param ycircular the boolean flag.
     */
    public void setYcircular(boolean ycircular)
    {

        this.ycircular = ycircular;
    }

    /**
     * Gets the second dimension's current index.
     * @return the index number.
     */
    public int getY()
    {

        return y;
    }

    /**
     * Increments the second dimension's index by 1, in the limits of the range.
     */
    public void addY()
    {

        handleWASD("w");
    }

    /**
     * Decrements the second dimension's index by 1, in the limits of the range.
     */
    public void subY()
    {

        handleWASD("s");
    }

    /**
     * Handles the user WASD input and updates the current selection state.
     * @param input the user input as String.
     */
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

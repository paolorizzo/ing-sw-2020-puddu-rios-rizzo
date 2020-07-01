package it.polimi.ingsw.view.cli;

/**
 * A CLI-oriented representation of a choice between a set of prior-defined possibilities.
 */
public class SelectionCLI
{
    protected int x;
    protected int xmax;
    protected boolean xcircular;

    /**
     * Constructs a selection object.
     */
    public SelectionCLI()
    {
        this.x = 0;
        this.xmax = 0;
        this.xcircular = false;
    }

    /**
     * Constructs a selection object.
     * @param x starting index.
     * @param xmax range for the selection.
     * @param xcircular flag indicating if, once reached the limit of the range, the index should restart from the beginning.
     */
    public SelectionCLI(int x, int xmax, boolean xcircular)
    {
        this.x = x;
        this.xmax = xmax;
        this.xcircular = xcircular;
    }

    /**
     * Sets the index for the selection.
     * @param x the index number.
     */
    public void setX(int x)
    {
        if(x <= this.xmax)
            this.x = x;
    }

    /**
     * Sets the range for the selection.
     * @param xmax the number indicating the max index reachable.
     */
    public void setXmax(int xmax)
    {

        this.xmax = xmax;
    }

    /**
     * Sets the flag indicating if, once reached the limit of the range, the index should restart from the beginning.
     * @param xcircular the boolean flag.
     */
    public void setXcircular(boolean xcircular)
    {

        this.xcircular = xcircular;
    }

    /**
     * Gets the current selection index.
     * @return the index number.
     */
    public int getX()
    {

        return this.x;
    }

    /**
     * Increments the index by 1, in the limits of the range.
     */
    public void addX()
    {

        handleWASD("d");
    }

    /**
     * Decrements the index by 1, in the limits of the range.
     */
    public void subX()
    {

        handleWASD("a");
    }

    /**
     * Handles the user WASD input and updates the current selection state.
     * @param input the user input as String.
     */
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

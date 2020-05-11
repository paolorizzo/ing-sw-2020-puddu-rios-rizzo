package it.polimi.ingsw.view.cli;

/**
 * A CLI-oriented version of the Space class.
 */
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

    //TODO check if acceptable level
    /**
     * Sets texture and colors to a RectangleCLI instance representing a building.
     * @param figure the figure to be decorated.
     * @param level the top level of the building.
     */
    public static void buildLevel(RectangleCLI figure, int level)
    {
        GraphicsElementsCLI.drawLevel(figure, level);
    }
}

package it.polimi.ingsw.view.cli;

/**
 * A CLI-oriented version of the Space class.
 */
public class SpaceCLI
{
    private int x;
    private int y;
    private int level;

    /**
     * Constructs a SpaceCLI object.
     */
    public SpaceCLI()
    {
        this.x = 0;
        this.y = 0;
        this.level = 0;
    }

    /**
     * Constructs a SpaceCLI object.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param level the building level.
     */
    public SpaceCLI(int x, int y, int level)
    {
        this.x = x;
        this.y = y;
        this.level = level;
    }

    /**
     * Gets the x coordinate of the space.
     * @return the x coordinate number.
     */
    public int getX()
    {

        return this.x;
    }

    /**
     * Gets the y coordinate of the space.
     * @return the y coordinate number.
     */
    public int getY()
    {

        return this.y;
    }

    /**
     * Retrieves the current building level in the space.
     * @return the level number.
     */
    public int getLevel()
    {

        return this.level;
    }

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

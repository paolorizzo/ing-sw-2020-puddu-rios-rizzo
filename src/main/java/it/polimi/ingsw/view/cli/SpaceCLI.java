package it.polimi.ingsw.view.cli;

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

    public static void buildLevel(RectangleCLI figure, int level)
    {
        GraphicsElementsCLI.drawLevel(figure, level);
    }
}

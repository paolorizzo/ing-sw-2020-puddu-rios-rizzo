package it.polimi.ingsw.view.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The basic printable entity for the CLI graphics.
 */
public class RectangleCLI
{
    protected final int originX;
    protected final int originY;
    protected final int sideX;
    protected final int sideY;

    protected char[][] mask;
    protected String[] palette;
    protected Iterator<String> chunks;
    protected List<RectangleCLI> figures;

    //constructors
    public RectangleCLI(int originX, int originY, int sideX, int sideY)
    {
        this.originX = originX;
        this.originY = originY;
        this.sideX = sideX;
        this.sideY = sideY;
        this.mask = setDefaultMask();
        this.palette = setDefaultPalette();
        this.figures = new ArrayList<>();
        this.chunks = null;
    }

    public RectangleCLI()
    {
        this.originX = 0;
        this.originY = 0;
        this.sideX = 0;
        this.sideY = 0;
        this.mask = setDefaultMask();
        this.palette = setDefaultPalette();
        this.figures = new ArrayList<>();
        this.chunks = null;
    }

    /**
     * Factory method useful to design figures in a relative position to an existing one.
     * @param x the desired horizontal delta whit respect to the object the method is called on.
     * @param y the desired vertical delta whit respect to the object the method is called on.
     * @param sideX the horizontal side of the desired figure.
     * @param sideY the vertical side of the desired figure.
     * @return the new RectangleCLI object.
     */
    public RectangleCLI createInRelativeFrame(int x, int y, int sideX, int sideY)
    {

        return new RectangleCLI(x+originX, y+originY, sideX, sideY);
    }


    //modifiers
    /**
     * Adds a new figure as overlapping, meaning that it will be printed on the top of this.
     * Multiple overlapping figures are allowed, but every new figure is added as top layer.
     * @param figure the overlapping RectangleCLI object.
     */
    void addOverlappingFigure(RectangleCLI figure)
    {

        figures.add(0,figure);
    }

    /**
     * Adds text to be printed on this.
     * @param s the text to be printed.
     */
    void addText(String s)
    {

        this.chunks = Arrays.asList(getChunks(s)).iterator();
    }

    //TODO test if they're actually color strings.
    /**
     * Sets a set of colors to be mapped on the texture.
     * @param colors a set of color strings.
     */
    void setPalette(String... colors)
    {
        List<String> pal = new ArrayList();
        Collections.addAll(pal, colors);
        this.palette = pal.toArray(new String[0]);
    }

    /**
     * Sets a texture mask to be mapped on the local palette.
     * @param URI the location of the text file storing the mask.
     */
    void setMask(String URI)
    {

        this.mask = obtainMask(URI);
    }


    //observers
    /**
     * Checks if a pair of coordinates belongs to this rectangle's area, considered in the canvas frame.
     * @param x the x coordinate, considered in the canvas frame.
     * @param y the y coordinate, considered in the canvas frame.
     * @return true if the considered position is in the rectangle, else otherwise.
     */
    boolean isItIn(int x, int y)
    {
        return x >= originX && y >= originY && x < (originX+sideX) && y < (originY+sideY);
    }

    /**
     * Checks if this rectangle has some text on it.
     * @return true if text has been set on this.
     */
    boolean hasText()
    {

        return chunks != null;
    }

    /**
     * Checks if this has overlapping figures in a certain point.
     * @param x the x coordinate, considered in the canvas frame.
     * @param y the y coordinate, considered in the canvas frame.
     * @return true if this has overlapping figures in the defined position.
     */
    boolean hasOverlapHere(int x, int y)
    {
        for(RectangleCLI r:figures)
        {
            if(r.isItIn(x,y))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Recursively compute the top layer's text in a certain position.
     * @param x the x coordinate, considered in the canvas frame.
     * @param y the y coordinate, considered in the canvas frame.
     * @return the text if present, a blank string otherwise.
     */
    String getTopLayerText(int x, int y)
    {
        if(hasOverlapHere(x, y))
        {
            for(RectangleCLI r:figures)
            {
                if(r.isItIn(x,y))
                {
                    return r.getTopLayerText(x,y);
                }
            }
        }

        if(hasText() && chunks.hasNext())
            return chunks.next();

        return "   ";
    }

    /**
     * Recursively compute the top layer's background color in a certain position.
     * @param x the x coordinate, considered in the canvas frame.
     * @param y the y coordinate, considered in the canvas frame.
     * @return the color string.
     */
    String getTopLayerColor(int x, int y)
    {
        if(hasOverlapHere(x, y))
        {
            for(RectangleCLI r:figures)
            {
                if(r.isItIn(x,y))
                {
                    return r.getTopLayerColor(x,y);
                }
            }
        }
        return getColor(x,y);
    }

    /**
     * Gets the background color of a determinate point of this.
     * @param x the x coordinate, considered in the canvas frame.
     * @param y the y coordinate, considered in the canvas frame.
     * @return the color string.
     */
    String getColor(int x, int y)
    {
        if(mask != null && palette != null)
        {
            return getBG(mask, palette, XInRelativeFrame(x), YInRelativeFrame(y));
        }
        else
        {
            return null;
        }
    }

    //TODO handle requests out of the area
    /**
     * Actually computes the background color in a certain position, mapping the palette on the texture mask.
     * @param mask a matrix of chars representing the color in every point of the figure.
     * @param palette a set of color strings.
     * @param x the x coordinate, considered in the local frame.
     * @param y the y coordinate, considered in the local frame.
     * @return the color string.
     */
    protected static String getBG(char[][] mask, String[] palette, int x, int y)
    {
        return palette[Character.getNumericValue(mask[y][x])];
    }


    //utils
    //TODO rewrite in a less horrible way
    /**
     * Splits a single string in an array of multiple strings of length 3.
     * This is needed to correctly display the text on the picture, as the basic printable unit is composed of  chars.
     * @param text the original string to be split up.
     * @return the array of strings.
     */
    protected static String[] getChunks(String text)
    {
        String[] chunks;
        if(text.length() > 3)
        {
            //chunks = new String[text.length()%3==0? text.length()/3 : ((text.length()/3)+1)];
            chunks = new String[(text.length()/3)+1];

            int start = 0;
            for(int i=0; i<chunks.length; i++)
            {
                chunks[i] = text.substring(start, start + Math.min(3, text.length()-start));
                start += 3;
            }

            for(int i=0; i<chunks.length; i++)
            {
                if(chunks[i] != null)
                {
                    if(chunks[i].length() == 2)
                    {
                        chunks[i] = chunks[i] + " ";
                    }
                    else if(chunks[i].length() == 1)
                    {
                        chunks[i] = chunks[i] + "  ";
                    }
                    else if(chunks[i].length() ==0)
                    {
                        chunks[i] = "   ";
                    }
                }
                else
                {
                    chunks[i] = "   ";
                }
            }
        }
        else
        {
            chunks = new String[1];
            chunks[0] = text;
        }
        return chunks;
    }

    //TODO create palette class
    /**
     * Initialize the palette field with a white background color.
     * @return the newly created palette.
     */
    protected String[] setDefaultPalette()
    {

        return new String[]{AnsiColors.ANSI_BG_WHITE};
    }

    /**
     * Initialize the mask field with an empty texture composed only by one background color.
     * @return the newly created mask.
     */
    protected char[][] setDefaultMask()
    {
        char[][] defMask = new char[sideY][sideX];

        for(int y = 0; y<sideY; y++)
        {
            for(int x=0; x<sideX; x++)
            {
                defMask[y][x] = '0';
                //System.out.print("0");
            }
            //System.out.println();
        }
        return defMask;
    }

    //TODO check the validity of URI
    //TODO create mask class
    /**
     * Extract the mask from the text file storing it.
     * @param URI the location of the file.
     * @return the char matrix representing the texture.
     */
    protected static char[][] obtainMask(String URI)
    {
        List<char[]> l = new ArrayList();
        try
        {
            File file = new File(URI);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                l.add(data.toCharArray());
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found!");
        }

        char[][] matr2 = new char[12][12];
        matr2 = l.toArray(matr2);

        return matr2;
    }

    /**
     * Converts x coordinate from the canvas frame to the local frame.
     * @param x the x coordinate, considered in the canvas frame.
     * @return the converted coordinate.
     */
    protected int XInRelativeFrame(int x)
    {

        return x - originX;
    }

    /**
     * Converts y coordinate from the canvas frame to the local frame.
     * @param y the y coordinate, considered in the canvas frame.
     * @return the converted coordinate.
     */
    protected int YInRelativeFrame(int y)
    {

        return y - originY;
    }
}
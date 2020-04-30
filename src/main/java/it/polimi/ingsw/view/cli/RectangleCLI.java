package it.polimi.ingsw.view.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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

    public RectangleCLI createInRelativeFrame(int x, int y, int sideX, int sideY)
    {

        return new RectangleCLI(x+originX, y+originY, sideX, sideY);
    }


    //modifiers
    public void addOverlappingFigure(RectangleCLI figure)
    {

        figures.add(0,figure);
    }

    public void addText(String s)
    {
        this.chunks = Arrays.asList(getChunks(s)).iterator();
        //System.out.println(s);
    }

    public void setPalette(String... colors)
    {
        List<String> pal = new ArrayList();
        Collections.addAll(pal, colors);
        this.palette = pal.toArray(new String[0]);
    }

    public void setMask(String URI)
    {

        this.mask = obtainMask(URI);
    }


    //observers
    public boolean isItIn(int x, int y)
    {
        return x >= originX && y >= originY && x < (originX+sideX) && y < (originY+sideY);
    }

    public boolean hasText()
    {

        return chunks != null;
    }

    public boolean hasOverlapHere(int x, int y)
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

    public String getTopLayerText(int x, int y)
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

    public String getTopLayerColor(int x, int y)
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

    public String getColor(int x, int y)
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

    protected static String getBG(char[][] mask, String[] palette, int x, int y)
    {
        return palette[Character.getNumericValue(mask[y][x])];
    }


    //utils
    protected static String[] getChunks(String text)
    {
        String[] chunks;
        if(text.length() > 3)
        {
            chunks = new String[text.length()%3==0? text.length()/3 : (text.length()/3+1)];

            int start = 0;
            for(int i=0; i<chunks.length && start+3<text.length(); i++)
            {
                chunks[i] = text.substring(start, start+3);
                start += 3;
            }

            if(text.length()%3 == 2)
            {
                chunks[chunks.length -1] = text.substring(text.length()-2) + " ";
            }
            else if(text.length()%3 == 1)
            {
                chunks[chunks.length -1] = text.substring(text.length()-1) + "  ";
            }
        }
        else
        {
            chunks = new String[1];
            chunks[0] = text;
        }
        return chunks;
    }

    protected String[] setDefaultPalette()
    {

        return new String[]{AnsiColors.ANSI_BG_WHITE};
    }

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

    protected static char[][] obtainMask(String URI)
    {
        File file = new File(URI); //"./src/main/resources/two.txt"
        List<char[]> l = new ArrayList();
        try
        {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                l.add(data.toCharArray());
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("file non trovato");
        }

        char[][] matr2 = new char[12][12];
        matr2 = l.toArray(matr2);

        return matr2;
    }

    protected int XInRelativeFrame(int x)
    {

        return x - originX;
    }

    protected int YInRelativeFrame(int y)
    {

        return y - originY;
    }
}
package it.polimi.ingsw.model;

public class Card
{
    private String name;
    private int num;
    private String description;

    public Card(String name, int num, String desc)
    {
        this.name = name;
        this.num = num;
        this.description = desc;
    }

    public String getName()
    {
        return name;
    }

    public int getNum()
    {
        return num;
    }

    public String getDescription()
    {
        return description;
    }
}

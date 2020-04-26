package it.polimi.ingsw.model;

import it.polimi.ingsw.model.power.PowerStrategy;

import java.io.Serializable;

public class Card implements Serializable
{
    private String name;
    private int num;
    private String description;
    transient private PowerStrategy powerStrategy;

    public Card(int num, String name, String desc, PowerStrategy ps)
    {
        this.num = num;
        this.name = name;
        this.description = desc;
        this.powerStrategy = ps;
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

    public PowerStrategy getPowerStrategy() {
        return powerStrategy;
    }

}

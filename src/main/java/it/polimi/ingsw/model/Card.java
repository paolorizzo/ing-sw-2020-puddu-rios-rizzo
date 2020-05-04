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

    /**
     * checks customary equals conditions and
     * checks that name, num and descriptions are equal
     * @param o the object against which to compare this
     * @return whether the object is equal to this
     */
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(! (o instanceof Card))
            return false;
        Card that = (Card) o;
        return  this.name.equals(that.name) &&
                this.num == that.num &&
                this.description.equals(that.description);
    }

    @Override
    protected Card clone(){
        return new Card(num, name, description, powerStrategy);
    }

}

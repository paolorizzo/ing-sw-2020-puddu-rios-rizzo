package it.polimi.ingsw.model;

import it.polimi.ingsw.model.power.PowerStrategy;

import java.io.Serializable;

public class Card implements Serializable
{
    private String name;
    private int num;
    private String description;
    transient private PowerStrategy powerStrategy;

    /**
     * constructs a Card object with all its fields
     * @param num the number of the card
     * @param name the name of the god on the card
     * @param desc the description of the power of the god
     * @param ps the PowerStrategy that modifies the ActionTree
     *           based on the power of the god
     */
    public Card(int num, String name, String desc, PowerStrategy ps)
    {
        this.num = num;
        this.name = name;
        this.description = desc;
        this.powerStrategy = ps;
    }

    /**
     * returns the name of the god on the card
     * @return the name of the god on the card
     */
    public String getName()
    {
        return name;
    }

    /**
     * returns the number of the card
     * @return the number of the card
     */
    public int getNum()
    {
        return num;
    }

    /**
     * returns the description of the power of the god
     * @return the description of the power of the god
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * returns the power strategy of the card, which modifies
     * the action tree and de facto implements the power of the god
     * @return the power strategy of the card
     */
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

    /**
     * returns a copy of a Card object
     * @return a copy of a Card object
     */
    @Override
    protected Card clone(){
        return new Card(num, name, description, powerStrategy);
    }

}

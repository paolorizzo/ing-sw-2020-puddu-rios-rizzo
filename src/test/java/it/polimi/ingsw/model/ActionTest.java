package it.polimi.ingsw.model;

import org.junit.Test;

public class ActionTest
{
    @Test(expected = IllegalArgumentException.class)
    public void validCoordinates()
    {
        Action act = new Action("id", 3, 6);
    }
}

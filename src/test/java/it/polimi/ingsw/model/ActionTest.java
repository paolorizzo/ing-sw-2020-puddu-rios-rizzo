package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActionTest
{
    @Test(expected = IllegalArgumentException.class)
    public void checkValidCoordinates()
    {
        Action act = new Action("id", 3, 6);
    }

    @Test
    public void checkWorker()
    {
        Action act = new Action("id", 1, 1);

        assertEquals(act.getWorkerID(), "id");
    }

    @Test
    public void checkGetCoordinates()
    {
        Action act = new Action("id", 1, 2);

        assertEquals(act.getTargetX(), 1);
        assertEquals(act.getTargetY(), 2);
    }

    @Test
    public void checkToString()
    {
        Action act = new Action("id", 1, 2);

        assertEquals(act.toString(), "Action{workerID='id', targetX=1, targetY=2}");
    }

    @Test
    public void checkEquals()
    {
        Action act1 = new Action("id", 1, 2);
        Action act2 = new Action("id", 1, 2);

        assert(act1.equals(act2));
    }
}

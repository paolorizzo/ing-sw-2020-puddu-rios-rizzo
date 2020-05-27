package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests the class MoveAction
 */
public class MoveActionTest extends ActionTest{

    /**
     * checks that the toString method works as expected
     */
    @Test
    public void checkToString()
    {
        MoveAction m1 = new MoveAction("id", 1, 2, Direction.SAME, 1, 1);

        assertEquals(m1.toString(), "MoveAction{direction=SAME, startX=1, startY=1, workerID='id', targetX=1, targetY=2}");
    }

    /**
     * checks that it is possible to convert a MoveAction to a map, and then
     * back to an equal MoveAction
     */
    @Test
    public void checkMapConversion(){
        MoveAction original = new MoveAction("id", 1, 1, Direction.SAME, 0, 0);
        checkConversion(original);
    }
}

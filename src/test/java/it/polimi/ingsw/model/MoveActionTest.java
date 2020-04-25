package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoveActionTest {

    @Test
    public void checkToString()
    {
        MoveAction m1 = new MoveAction("id", 1, 2, Direction.SAME, 1, 1);

        assertEquals(m1.toString(), "MoveAction{direction=SAME, startX=1, startY=1, workerID='id', targetX=1, targetY=2}");
    }
}

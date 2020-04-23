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
    @Test
    public void checkMatches()
    {
        Action act1 = new Action("id", 1, 2);
        Action act2 = new Action("id", 2, 4);

        assert(act1.matches(act2.getWorkerID()));

        MoveAction m1 = new MoveAction("id", 1, 2, Direction.UP, 2, 2);

        assert(act1.matches(m1.getWorkerID()));

        assert(act1.matches(m1.getWorkerID(), m1.getTargetX(), m1.getTargetY()));

        BuildAction b1 = new BuildAction("id", 1, 2, Piece.DOME);

        assert(!act1.matches(b1.getWorkerID(), b1.getPiece()));
        assert(!act1.matches(b1.getWorkerID(), b1.getTargetX(), b1.getTargetY(), b1.getPiece()));

        BuildAction b2 = new BuildAction("id", 1, 2, Piece.DOME);

        assert(b1.matches(b2.getWorkerID(), b2.getTargetX(), b2.getTargetY(), b2.getPiece()));
    }
}

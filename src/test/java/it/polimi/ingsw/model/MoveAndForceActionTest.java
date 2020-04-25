package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoveAndForceActionTest {

    @Test
    public void checkToString()
    {
        MoveAndForceAction m1 = new MoveAndForceAction("id", 1, 2, Direction.SAME, 2, 2, "forcedId", 2, 2, 1, 2);

        assertEquals(m1.toString(), "MoveAndForceAction{forcedWorkerId='forcedId', forcedStartX=2, forcedStartY=2, forcedTargetX=1, forcedTargetY=2, direction=SAME, startX=2, startY=2, workerID='id', targetX=1, targetY=2}");
    }
    @Test
    public void checkEquals()
    {
        MoveAndForceAction m1 = new MoveAndForceAction("id", 1, 2, Direction.SAME, 2, 2, "forcedId", 2, 2, 1, 2);

        assert(m1.equals(new MoveAndForceAction("id", 1, 2, Direction.SAME, 2, 2, "forcedId", 2, 2, 1, 2)));

        assert(!m1.equals(new MoveAndForceAction("notEqualsId", 1, 2, Direction.SAME, 2, 2, "forcedId", 2, 2, 1, 2)));
        assert(!m1.equals(new MoveAndForceAction("id", 2, 2, Direction.SAME, 2, 2, "forcedId", 2, 2, 1, 2)));
        assert(!m1.equals(new MoveAndForceAction("id", 1, 3, Direction.UP, 1, 2, "forcedId", 2, 2, 1, 2)));
        assert(!m1.equals(new MoveAndForceAction("id", 1, 2, Direction.SAME, 2, 3, "notEqualsForcedId", 2, 2, 1, 2)));
        assert(!m1.equals(new MoveAndForceAction("id", 1, 2, Direction.SAME, 2, 2, "forcedId", 4, 2, 1, 2)));
        assert(!m1.equals(new MoveAndForceAction("id", 1, 2, Direction.SAME, 2, 2, "forcedId", 2, 1, 1, 2)));
        assert(!m1.equals(new MoveAndForceAction("id", 1, 2, Direction.SAME, 2, 2, "forcedId", 2, 2, 0, 2)));
        assert(!m1.equals(new MoveAndForceAction("id", 1, 2, Direction.SAME, 2, 2, "forcedId", 2, 2, 1, 5)));
    }
}

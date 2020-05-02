package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SetupActionTest {
    @Test
    public void checkMatches()
    {
        SetupAction act1 = new SetupAction("id", 1, 2);

        assert(act1.matches(1, 2));
        assert(!act1.matches(1, 0));
        assert(!act1.matches(2, 2));

    }
    @Test
    public void checkToString()
    {
        SetupAction m1 = new SetupAction("id", 1, 2);

        assertEquals(m1.toString(), "SetupAction{workerID='id', targetX=1, targetY=2}");
    }
}

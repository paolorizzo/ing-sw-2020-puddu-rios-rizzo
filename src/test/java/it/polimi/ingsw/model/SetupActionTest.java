package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests SetupAction
 */
public class SetupActionTest extends ActionTest{

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

    /**
     * checks that it is possible to convert a SetupAction to a map, and then
     * back to an equal SetupAction
     */
    @Test
    public void checkMapConversion(){
        SetupAction original = new SetupAction("id", 1, 1);
        checkConversion(original);
    }
}

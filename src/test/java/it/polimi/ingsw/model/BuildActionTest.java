package it.polimi.ingsw.model;

import it.polimi.ingsw.view.gui.Building;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BuildActionTest extends ActionTest{
    @Test
    public void checkMatches()
    {
        BuildAction b1 = new BuildAction("id", 1, 2, Piece.LEVEL1);
        assert (b1.matches("id", 1, 2, Piece.LEVEL1));
        assert (!b1.matches("notEqualsId", 1, 2, Piece.LEVEL1));
        assert (!b1.matches("id", 3, 2, Piece.LEVEL1));
        assert (!b1.matches("id", 1, 0, Piece.LEVEL1));
        assert (!b1.matches("id", 1, 2, Piece.LEVEL3));
        assert (b1.matches("id", Piece.LEVEL1));
        assert (!b1.matches("notEqualsId", Piece.LEVEL1));
        assert (!b1.matches("id", Piece.LEVEL2));
    }
    @Test
    public void checkToString()
    {
        BuildAction b1 = new BuildAction("id", 1, 2, Piece.LEVEL1);

        assertEquals(b1.toString(), "BuildAction{piece=LEVEL1, workerID='id', targetX=1, targetY=2}");
    }

    /**
     * checks that a BuildAction object can be correctly converted to a map and then back
     * to an equal object
     */
    @Test
    public void testMapConversion(){
        BuildAction original = new BuildAction("id", 1, 1, Piece.LEVEL1);
        checkConversion(original);
    }
}

package it.polimi.ingsw.model;

import it.polimi.ingsw.view.gui.Building;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuildActionTest {
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
}

package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.Map;

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

        assert(!act1.matches("differentid"));

        assert(!act1.matches("differentid", 1, 2));
        assert(!act1.matches("id", 4, 2));
        assert(!act1.matches("id", 1, 3));
    }

    /**
     * tests that it is possible to convert an action to a map, and back to an equal action again
     */
    @Test
    public void checkMapConversion(){
        Action original = new Action("id", 1, 1);
        checkConversion(original);
    }

    /**
     * tests that it is possible to convert a BuildAction to a map, and back to a BuildAction,
     * even though we're using the fromMethod in Action and java disallows overriding
     * between static methods
     */
    @Test
    public void checkConversionDynamicTyping(){
        Action original = new BuildAction("id", 1, 1, Piece.LEVEL1);
        checkConversion(original);
    }

    /**
     * utility method to incapsulate the actual conversion and check
     * @param original the original action to be converted to map and back
     */
    public void checkConversion(Action original){
        System.out.println(original.toString());
        Map map = original.toMap();
        System.out.println(map.toString());
        Action processed = Action.fromMap(map);
        System.out.println(processed.toString());
        assertEquals(original, processed);
    }
}

package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.AlreadyFullException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TurnTest {

    //tests that the empty constructor returns a turn of size 0
    @Test
    public void testEmptyConstructor(){
        Player player = new Player("Federico", Color.BLUE, 1);
        Turn turn= new Turn(player);
        assert(player.getId() == turn.getPlayerId());
        assert(turn.isEmpty());
    }

    //tests that the constructor that takes an Action argument returns a turn that contains that action
    //and only that action
    @Test
    public void testActionConstructor(){
        Player player = new Player("Federico", Color.BLUE, 1);
        Action action=new Action("workerID", 2, 2);
        Turn turn = new Turn(player, action);
        assertEquals(turn.size(), 1);
        assert(player.getId() == turn.getPlayerId());
        assert(turn.contains(action));
    }

    //tests that it is not allowed to insert more than 3 actions into a turn
    @Test(expected = AlreadyFullException.class)
    public void testMaximumCapacity(){
        Player player = new Player("Federico", Color.BLUE, 1);
        Action first = new Action("1", 1, 1);
        Action second = new Action("2", 2, 2);
        Action third = new Action("3", 3, 3);
        Action fourth = new Action("4", 4, 4);
        Turn turn = new Turn(player, first);
        turn.add(second);
        turn.add(third);
        turn.add(fourth);
    }

    //tests that it is not allowed to add a null action
    @Test(expected = NullPointerException.class)
    public void testNullActionNotAllowed(){
        Player player = new Player("Federico", Color.BLUE, 1);
        Turn turn = new Turn(player);
        turn.add(null);
    }

    //tests that size() method actually returns the correct value
    @Test
    public void testSizeMethod(){
        Player player = new Player("Federico", Color.BLUE, 1);
        Action first = new Action("1", 1, 1);
        Action second = new Action("2", 2, 2);
        Action third = new Action("3", 3, 3);
        Turn turn = new Turn(player);
        assertEquals(turn.size(), 0);
        turn.add(first);
        assertEquals(turn.size(), 1);
        turn.add(second);
        assertEquals(turn.size(), 2);
        turn.add(third);
        assertEquals(turn.size(), 3);
    }

    //tests that contains() method returns true for an action only if it has been added
    @Test
    public void testContainsMethod(){
        Player player = new Player("Federico", Color.BLUE, 1);
        Action first = new Action("1", 1, 1);
        Action second = new Action("2", 2, 2);
        Action third = new Action("3", 3, 3);
        Turn turn = new Turn(player);
        assert(!turn.contains(first));
        assert(!turn.contains(second));
        assert(!turn.contains(third));
        turn.add(first);
        assert(turn.contains(first));
        assert(!turn.contains(second));
        assert(!turn.contains(third));
    }

    //tests that isEmpty() returns true if and only if no action has been added
    @Test
    public void testIsEmptyMethod(){
        Player player = new Player("Federico", Color.BLUE, 1);
        Action first = new Action("1", 1, 1);
        Action second = new Action("2", 2, 2);
        Action third = new Action("3", 3, 3);
        Turn turn = new Turn(player);
        assert(turn.isEmpty());
        turn.add(first);
        assert(!turn.isEmpty());
        turn.add(second);
        assert(!turn.isEmpty());
        turn.add(third);
        assert(!turn.isEmpty());
    }

    /**
     * tests that it is possible to build a turn, convert it to a map
     * and then back to an equal turn
     */
    @Test
    public void testMapConversion(){
        Turn original = new Turn(0);
        original.add(new MoveAction("P0-F", 1, 1, Direction.SAME, 0, 0));
        original.add(new MoveAction("P0-F", 2, 2, Direction.UP, 1, 1));
        original.add(new BuildAction("P0-F", 3, 3, Piece.LEVEL1));
        System.out.println(original.toString());
        Turn processed = Turn.fromMap(original.toMap());
        System.out.println(processed.toString());
        assertEquals(original.toString(), processed.toString());
    }

    /**
     * tests that the equals method works as intended
     */
    @Test
    public void testEquals(){
        Turn t1 = new Turn(0);
        t1.add(new MoveAction("P0-F", 1, 1, Direction.SAME, 0, 0));
        t1.add(new MoveAction("P0-F", 2, 2, Direction.UP, 1, 1));
        Turn t2 = new Turn(0);
        t2.add(new MoveAction("P0-F", 1, 1, Direction.SAME, 0, 0));
        t2.add(new MoveAction("P0-F", 2, 2, Direction.UP, 1, 1));
        assertEquals(t1, t2);
        BuildAction b = new BuildAction("P0-F", 3, 3, Piece.LEVEL1);
        assertNotEquals(t1, b);
        t1.add(b);
        assertNotEquals(t1, t2);
    }
}

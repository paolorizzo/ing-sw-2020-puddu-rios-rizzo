package it.polimi.ingsw.model;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ActionTreeTest {
    @Test
    public void ConstructorWithoutParamsTest(){
        ActionTree t = new ActionTree();
        assert(t.getChildren().size() == 0);
        assert(t.isRoot());
        assert(t.getAction() == null);
        assert(!t.isWin());
        assert(!t.isLose());
        assert(!t.isEndOfTurn());
        assert(t.isAppendedLayer());
    }
    @Test
    public void ConstructorWithParamsTest(){
        SetupAction a = new SetupAction("P1-M", 0, 0);
        boolean win = true;
        boolean lose = false;
        boolean endOfTurn = false;
        boolean appendLayer = true;
        ActionTree t = new ActionTree(a, win, lose, endOfTurn, appendLayer);
        assert(t.getChildren().size() == 0);
        assert(!t.isRoot());
        assert(t.getAction() == a);
        assert(t.isWin() == win);
        assert(t.isLose() == lose);
        assert(t.isEndOfTurn() == endOfTurn);
        assert(t.isAppendedLayer() == appendLayer);
    }
    @Test
    public void isPathPresentTest(){
        ActionTree t = new ActionTree();

        MoveAction m1 = new MoveAction("P1-M", 0, 1, Direction.SAME, 0, 0);
        BuildAction b1 = new BuildAction("P1-M", 0, 0, Piece.LEVEL1);
        ActionTree t1 = new ActionTree(m1, false, false, false, false);
        ActionTree t2 = new ActionTree(b1, false, false, true, false);

        BuildAction b2 = new BuildAction("P1-F", 3, 1, Piece.LEVEL2);
        ActionTree t3 = new ActionTree(b2, false, false, true, false);


        assert(!t.isPathPresent(Arrays.<Action>asList(m1, b1)));

        t1.addChild(t2);
        t.addChild(t1);

        assert(t.isPathPresent(Arrays.<Action>asList(m1, b1)));
        assert(!t.isPathPresent(Arrays.<Action>asList(b2)));
    }
    @Test
    public void equalsTest(){
        ActionTree t = new ActionTree();
        assert(t.equals(new ActionTree()));

        MoveAction m1 = new MoveAction("P1-M", 0, 1, Direction.SAME, 0, 0);
        BuildAction b1 = new BuildAction("P1-M", 0, 0, Piece.LEVEL1);

        ActionTree t1 = new ActionTree(m1, false, false, false, true);

        assert(!t.equals(t1));

        assert(!t.equals(new ActionTree(m1, false, false, false, true)));

        assert(!t.equals(new ActionTree(b1, false, false, false, true)));
        assert(!t.equals(new ActionTree(m1, true, false, false, true)));
        assert(!t.equals(new ActionTree(m1, false, true, false, true)));
        assert(!t.equals(new ActionTree(m1, false, false, true, true)));
        assert(!t.equals(new ActionTree(m1, false, false, false, false)));

        assert(!t.equals(m1));

        ActionTree t2 = new ActionTree(m1, false, false, false, true);
        ActionTree t3 = new ActionTree(b1, false, false, true, false);

        t1.addChild(t3);
        t2.addChild(t3);

        assert(t1.equals(t2));

        t2.addChild(t);

        assert(!t1.equals(t2));

    }
}

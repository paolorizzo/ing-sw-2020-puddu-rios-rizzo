package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ControllerTest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PlayerTest
{
    @Test(expected = IllegalArgumentException.class)
    public void tryToCreatePlayerWithWrongIdFirstConstructor()
    {
       Player p = new Player("name", Color.BLUE, 3);
    }
    @Test(expected = IllegalArgumentException.class)
    public void tryToCreatePlayerWithWrongIdSecondConstructor()
    {
        Player p = new Player(-1, "name");
    }
    @Test
    public void checkWorkers()
    {
        Player p = new Player("name", Color.BLUE, 1);
        assertNotNull(p.getWorker(Sex.MALE));
        assertNotNull(p.getWorker(Sex.FEMALE));
    }

    @Test
    public void checkNickname()
    {
        Player p = new Player("name", Color.BLUE, 1);
        assertEquals(p.getNickname(), "name");
    }

    @Test
    public void checkGetWorkerBySex()
    {
        Player p = new Player("name", Color.BLUE, 1);
        assertEquals(p.getWorker(Sex.MALE).getSex(), Sex.MALE );
        assertEquals(p.getWorker(Sex.FEMALE).getSex(), Sex.FEMALE );
    }

    @Test
    public void checkPlayerNum()
    {
        Player p = new Player("name", Color.BLUE, 1);
        assertEquals(p.getPlayerNum(), 1);
    }

    @Test
    public void checkColor()
    {
        Player p = new Player("name", Color.BLUE, 1);
        assertEquals(p.getColor(), Color.BLUE);
    }
    @Test
    public void checkEquals()
    {
        Player p = new Player("name", Color.BLUE, 1);

        assert(p.equals(new Player("name", Color.BLUE, 1)));
        assert(!p.equals(new Player("notEquals", Color.BLUE, 1)));
        assert(!p.equals(new Player("name", Color.ORANGE, 1)));
        assert(!p.equals(new Player("name", Color.BLUE, 2)));
        assert(!p.equals(new Object()));
    }
    @Test
    public void checkGenerateSetupActionsWorker(){
        Player p = new Player(0,"Mark");
        Board board = new Board();
        board.createPlayerWorkers(p);

        assert(p.generateSetupActionsWorker(board, Sex.FEMALE).size() == 25);
        board.executeAction(p.generateSetupActionsWorker(board, Sex.FEMALE).get(0));

        assert(p.generateSetupActionsWorker(board, Sex.FEMALE) == null);

        assert(p.generateSetupActionsWorker(board, Sex.MALE).size() == 24);
        board.executeAction(p.generateSetupActionsWorker(board, Sex.MALE).get(0));

        assert(p.generateSetupActionsWorker(board, Sex.MALE)== null);
    }

    /**
     * tests that the fullEquals method works as intended
     */
    @Test
    public void testFullEquals(){
        Controller c1 = new Controller();
        ControllerTest ct1 = new ControllerTest();
        ct1.playSomeTurns(c1);
        Player p1 = c1.getModel().getPlayer(0);
        Controller c2 = new Controller();
        ControllerTest ct2 = new ControllerTest();
        ct2.playSomeTurns(c2);
        Player p2 = c2.getModel().getPlayer(0);
        Player p3 = c2.getModel().getPlayer(1);
        assert(p1.fullEquals(p2));
        assert(! p1.fullEquals(p3));
    }
}

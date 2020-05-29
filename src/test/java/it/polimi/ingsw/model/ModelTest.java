package it.polimi.ingsw.model;

import it.polimi.ingsw.MvcIntegrationTest;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ControllerTest;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.middleware.Connection;
import it.polimi.ingsw.view.middleware.Server;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.net.Socket;

public class ModelTest extends MvcIntegrationTest {
    @Test
    public void ConstructorTest(){
        Model model = new Model();
        assert (model.getNumPlayers() == -1);
        assert (!model.numPlayersIsSet());
        assert (model.getPlayers() != null);
        assert (model.getPlayers().size() == 0);
        assert (model.getFeed() != null);
    }
    @Test
    public void HandlePlayerTest(){
        Model model = new Model();
        model.setNumPlayers(3);
        assert(model.numPlayersIsSet());
        assert(model.getNumPlayers() == 3);
        assert(!model.nicknamePresent("Paolo"));
        assert(!model.playerPresent(0));
        model.addPlayer(new Player(0, "Paolo"));
        assert(model.playerPresent(0));
        assert(model.getPlayers().size() == 1);
        assert(model.nicknamePresent("Paolo"));
    }
    @Test
    public void HandleObserverTest() throws IOException {
        Model model = new Model();
        View view = new VirtualView(new Connection(new Socket(), new Server(4103)));
        assert (!model.feed.hasObservers());
        model.addObserver(view);
        assert (model.feed.hasObservers());
        model.removeObserver(view);
        assert (!model.feed.hasObservers());
    }

    /**
     * tests that it is possible to save the state of the game without exceptions
     * and that the files are actually stored
     */
    @Test
    public void testSave(){
        Controller c = new Controller();
        ControllerTest ct = new ControllerTest();
        Model m = c.getModel();
        ct.playSomeTurns(c);
        m.save();
        assert m.isSaved();
    }

    /**
     * test that fullEquals works as intended
     */
    @Test
    public void testFullEquals(){
        Controller c1 = new Controller();
        ControllerTest ct1 = new ControllerTest();
        ct1.playSomeTurns(c1);
        Model m1 = c1.getModel();
        Controller c2 = new Controller();
        ControllerTest ct2 = new ControllerTest();
        Model m2 = c2.getModel();

        assert(! m1.fullEquals(m2));
        ct2.playSomeTurns(c2);
        assert(m1.fullEquals(m2));
    }

    /**
     * tests that it is possible to restore the model up to the connection phase
     */
    @Test
    public void testConnectionPhaseRestoration(){
        Controller c1 = new Controller();
        ControllerTest ct1 = new ControllerTest();
        ct1.playSomeTurns(c1);
        Model m1 = c1.getModel();
        m1.save();
        Model m2 = new Model();
        PersistenceGame pg = PersistenceGame.load(m1.saveName());
        m2.restoreConnectionPhase(pg);
        assertEquals(m1.game.getNumPlayers(), m2.game.getNumPlayers());
        assertEquals(m1.players.keySet(), m2.players.keySet());
        for(int key:m1.players.keySet()){
            assertEquals(m1.players.get(key), m2.players.get(key));
        }
        assertEquals(m1.game.getCurrentPlayerId(), m2.game.getCurrentPlayerId());
    }

    /**
     * tests that it is possible to restore the model up to the choice of gods
     */
    @Test
    public void testSetupPhaseRestoration(){
        Controller c1 = new Controller();
        ControllerTest ct1 = new ControllerTest();
        ct1.playSomeTurns(c1);
        Model m1 = c1.getModel();
        m1.save();
        Model m2 = new Model();
        PersistenceGame pg = PersistenceGame.load(m1.saveName());
        m2.restoreConnectionPhase(pg);
        m2.restoreSetupPhase(pg);
        assertEquals(m1.players.keySet(), m2.players.keySet());
        for(int key:m1.players.keySet()){
            assert(m1.players.get(key).equals(m2.players.get(key)));
            assertEquals(m1.players.get(key).getCard(), m2.players.get(key).getCard());
        }
        assertEquals(m1.game.getDeck(), m2.game.getDeck());
    }


    @Test
    public void testFullRestore(){
        Controller c1 = new Controller();
        ControllerTest ct1 = new ControllerTest();
        ct1.playSomeTurns(c1);
        Model m1 = c1.getModel();
        m1.save();
        Model m2 = new Model();
        m2.load();
        assert (m1.fullEquals(m2));
    }

}

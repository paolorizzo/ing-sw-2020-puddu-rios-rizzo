package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ControllerTest;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.middleware.Connection;
import it.polimi.ingsw.view.middleware.Server;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class ModelTest {
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


}

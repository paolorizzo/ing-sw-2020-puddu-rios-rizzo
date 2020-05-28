package it.polimi.ingsw.model;
import org.junit.Test;
import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ControllerTest;

import static org.junit.Assert.assertEquals;

/**
 * implements tests for class TurnArchive
 */
public class TurnArchiveTest {

    /**
     * simulates a game to fill the turn archive, saves it, loads it, and tests that the loaded archive
     * and the original one correspond by testing their string representation
     */
    @Test
    public void testSaveLoad(){
        Controller c = new Controller();
        ControllerTest ct = new ControllerTest();
        ct.playSomeTurns(c);
        TurnArchive original = c.getModel().game.turnArchive;
        original.save();
        TurnArchive loaded = TurnArchive.load();
        assertEquals(original.toString(), loaded.toString());
    }

    /**
     * tests that it is possible to convert a TurnArchive to a map,
     * and then back to an equal TurnArchive object
     */
    @Test
    public void testMapConversion(){
        Controller c = new Controller();
        ControllerTest ct = new ControllerTest();
        ct.playSomeTurns(c);
        TurnArchive original = c.getModel().game.turnArchive;
        TurnArchive processed = TurnArchive.fromMap(original.toMap());
        assertEquals(original.toString(), processed.toString());
    }
}
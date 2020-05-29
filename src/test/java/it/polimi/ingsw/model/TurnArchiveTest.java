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
        original.save("");
        TurnArchive loaded = TurnArchive.load("");
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

    /**
     * tests that the path to the json persistence file is correctly generated
     */
    @Test
    public void testSavePath(){
        String names = "abcd";
        assertEquals("src/main/resources/persistence/turns" + names + ".json", TurnArchive.savePath(names));
    }

    /**
     * tests that the equals method works as expected
     */
    @Test
    public void testEquals(){
        Controller c1 = new Controller();
        ControllerTest ct1 = new ControllerTest();
        ct1.playSomeTurns(c1);
        TurnArchive ta1 = c1.getModel().game.turnArchive;
        Controller c2 = new Controller();
        ControllerTest ct2 = new ControllerTest();
        ct2.playSomeTurns(c2);
        TurnArchive ta2 = c2.getModel().game.turnArchive;
        assertEquals(ta1, ta2);
    }
}

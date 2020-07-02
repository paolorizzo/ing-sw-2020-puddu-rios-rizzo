package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ControllerTest;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class PersistenceGameTest {


    /**
     * tests that save returns no errors if called on valid parameters
     */
    @Test
    public void testJsonWritten(){
        Controller c = new Controller();
        ControllerTest ct = new ControllerTest();
        ct.fullSetupPhase(c, 3);
        PersistenceGame pg = new PersistenceGame(c.getModel().game);
        System.out.println(c.getModel().game.getChosenCards());
        Gson gson = new Gson();
        System.out.println(gson.toJson(pg));
        assert pg.save("");
    }

    /**
     * tests that the path to the game json file is valid
     * this tests only needs to run without exceptions
     */
    @Test
    public void testFilePath(){
        String[] components = new String[2];
        components[0] = ".";
        components[1] = "/persistence";
        String path = "";
        for (String component:components){
            path = path + component;
            File curDir = new File(path);
            System.out.println(path);
            System.out.println(curDir.isDirectory());
            File[] contents = curDir.listFiles();
            for (File f:contents){
                System.out.println(f);
            }
            System.out.println();
        }
        assert true;
    }

    /**
     * tests that it is possible to correctly generate a persistance game, save it, and load another one
     * that is equal to the first
     */
    @Test
    public void testSaveLoad(){
        Controller c = new Controller();
        ControllerTest ct = new ControllerTest();
        ct.fullSetupPhase(c, 3);
        PersistenceGame pg = new PersistenceGame(c.getModel().game);
        pg.save("");
        PersistenceGame new_pg = PersistenceGame.load("");
        assertEquals(pg.toString(), new_pg.toString());
    }

    /**
     * tests that the path to the json persistence file is correctly generated
     */
    @Test
    public void testSavePath(){
        String names = "_abcd";
        assertEquals("./persistence/game" + names + ".json", PersistenceGame.savePath(names));
    }

    /**
     * test equals game
     */
    @Test
    public void testEquals(){
        Controller c = new Controller();
        ControllerTest ct = new ControllerTest();
        ct.fullSetupPhase(c, 3);
        PersistenceGame pg = new PersistenceGame(c.getModel().game);

        Controller c2 = new Controller();
        ControllerTest ct2 = new ControllerTest();
        ct2.fullSetupPhase(c2, 3);
        PersistenceGame pg2 = new PersistenceGame(c2.getModel().game);

        assert(pg.equals(pg2));
    }
}

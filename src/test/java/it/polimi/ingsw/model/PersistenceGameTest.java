package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ControllerTest;
import org.junit.Test;

import java.io.File;

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
        assert pg.save();
    }

    /**
     * tests that the path to the game json file is valid
     * this tests only needs to run without exceptions
     */
    @Test
    public void testFilePath(){
        String[] components = new String[4];
        components[0] = "src";
        components[1] = "/main";
        components[2] = "/resources";
        components[3] = "/persistence";
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
}

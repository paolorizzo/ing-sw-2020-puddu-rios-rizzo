package it.polimi.ingsw.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PlayerTest
{
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
}

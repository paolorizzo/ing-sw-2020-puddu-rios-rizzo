package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorkerTest
{
    @Test
    public void checkToString()
    {
        Player player = new Player("name", Color.BLUE, 1);
        Worker worker = new Worker(Sex.MALE, player);
        assertEquals(worker.toString(), "P1-M");
    }

    @Test
    public void checkSex()
    {
        Player player = new Player("name", Color.BLUE, 1);
        Worker worker = new Worker(Sex.MALE, player);
        assertEquals(worker.getSex(), Sex.MALE);
    }

    @Test
    public void checkPlayer()
    {
        Player player = new Player("name", Color.BLUE, 1);
        Worker worker = new Worker(Sex.MALE, player);
        assertEquals(worker.getPlayer(), 1);
    }

    @Test
    public void checkSpace()
    {
        Player player = new Player("name", Color.BLUE, 1);
        Worker worker = new Worker(Sex.MALE, player);
        Space space = new Space(1,1);

        worker.setSpace(space);
        assertEquals(worker.getSpace(), space);
    }
}

package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

    /**
     * tests that the "equals" method works as intended
     */
    @Test
    public void checkEquals(){
        Player player = new Player("name", Color.BLUE, 1);
        Worker worker1 = new Worker(Sex.MALE, player);
        Worker worker2 = new Worker(Sex.FEMALE, player);
        Worker worker3 = new Worker(Sex.FEMALE, player);
        assertEquals(worker2, worker3);
        assertNotEquals(worker1, worker2);
        assertNotEquals(worker1, worker3);

    }
}

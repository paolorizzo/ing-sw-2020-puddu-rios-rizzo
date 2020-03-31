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
}

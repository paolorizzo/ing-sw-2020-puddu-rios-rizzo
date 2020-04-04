package it.polimi.ingsw.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest
{
    @Test
    public void checkGetName()
    {
        Message m = new Message("prova");

        assertEquals(m.getMethodName(), "prova");
    }

    @Test
    public void checkAddArg()
    {
        Message m = new Message("prova");
        Object o = new Object();

        m.addArg(o);
        assert(m.hasArgs());
        assertEquals(m.numberOfArgs(), 1);
        assertEquals(m.getArg(0), o);
    }
}

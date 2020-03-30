package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpaceTest {

    @Test
    public void constructorOk(){
        int x = 2;
        int y = 3;
        Space space = new Space(2, 3);
        assertEquals(space.getPosX(), x);
        assertEquals(space.getPosY(), y);
        assertEquals(space.getAdjacentSpaces().size(), 0);
        assertEquals(space.getLevel(), 0);
    }



}

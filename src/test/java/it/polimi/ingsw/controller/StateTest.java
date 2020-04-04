package it.polimi.ingsw.controller;

import org.junit.Test;

import static org.junit.Assert.*;

public class StateTest {

    //tests that no states were added, removed or modified and that their order was not changed
    @Test
    public void testEnumInstances(){
        State[] expectedStates= new State[9];
        expectedStates[0]=State.PLAYER_SETUP;
        expectedStates[1]=State.GET_ACTION_TREE;
        expectedStates[2]=State.DISPLAY_SELECT_VIEW;
        expectedStates[3]=State.VALIDATE_SELECT;
        expectedStates[4]=State.ELABORATE_SELECT;
        expectedStates[5]=State.DISPLAY_ACTION_VIEW;
        expectedStates[6]=State.VALIDATE_ACTION;
        expectedStates[7]=State.ELABORATE_ACTION;
        expectedStates[8]=State.UPDATE_TURN_ARCHIVE;
        State[] actualStates=State.values();
        assertEquals(expectedStates.length, actualStates.length);
        for (int i=0;i<actualStates.length;i++)
            assertEquals(actualStates[i], expectedStates[i]);
    }

    //tests that for all states except ELABORATE_ACTION the next state function returns the state immediately
    //after the caller. For ELABORATE_ACTION tests that its next state is either DISPLAY_ACTION_VIEW or
    //UPDATE_TURN_ARCHIVE. This is because this state may cycle back or progress depending on user input.
    //refer to study/turn_FSM.pdf for more.
    //it does NOT test whether the next state for ELABORATE_ACTION is logically correct, it only tests
    //that it is acceptable
    @Test
    public void testNextStateAcceptable(){
        State[] states=State.values();
        for(State state:states){
            if(state.equals(State.UPDATE_TURN_ARCHIVE))
                assert(state.next().equals(State.PLAYER_SETUP));
            else if(state.equals(State.ELABORATE_ACTION))
                assert(state.next().equals(State.DISPLAY_ACTION_VIEW) || state.next().equals(State.UPDATE_TURN_ARCHIVE));
            else
                assert(state.next().ordinal() == state.ordinal()+1);
        }
    }


}

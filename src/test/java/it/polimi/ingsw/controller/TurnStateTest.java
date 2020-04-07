package it.polimi.ingsw.controller;

import org.junit.Test;

import static org.junit.Assert.*;

public class TurnStateTest {

    //tests that no states were added, removed or modified and that their order was not changed
    @Test
    public void testEnumInstances(){
        TurnState[] expectedStates= new TurnState[9];
        expectedStates[0]=TurnState.PLAYER_SETUP;
        expectedStates[1]=TurnState.GET_ACTION_TREE;
        expectedStates[2]=TurnState.DISPLAY_SELECT_VIEW;
        expectedStates[3]=TurnState.VALIDATE_SELECT;
        expectedStates[4]=TurnState.ELABORATE_SELECT;
        expectedStates[5]=TurnState.DISPLAY_ACTION_VIEW;
        expectedStates[6]=TurnState.VALIDATE_ACTION;
        expectedStates[7]=TurnState.ELABORATE_ACTION;
        expectedStates[8]=TurnState.UPDATE_TURN_ARCHIVE;
        TurnState[] actualStates=TurnState.values();
        assertEquals(expectedStates.length, actualStates.length);
        for (int i=0;i<actualStates.length;i++)
            assertEquals(actualStates[i], expectedStates[i]);
    }

    //tests that the next state function returns an acceptable state
    //For state A a state B is acceptable if and only if there exists a transition
    //in the FSM from A to B
    //it does NOT test whether the next state is logically correct, just that it is acceptable
    @Test
    public void testNextStateAcceptable(){
        TurnState[] states=TurnState.values();
        for(TurnState state:states){
            if(state.equals(TurnState.UPDATE_TURN_ARCHIVE))
                assert(state.next().equals(TurnState.PLAYER_SETUP));
            else if(state.equals(TurnState.ELABORATE_ACTION))
                assert(state.next().equals(TurnState.DISPLAY_ACTION_VIEW) || state.next().equals(TurnState.UPDATE_TURN_ARCHIVE));
            else if(state.equals(TurnState.VALIDATE_SELECT))
                assert(state.next().equals(TurnState.ELABORATE_SELECT) || state.next().equals(TurnState.DISPLAY_SELECT_VIEW));
            else if(state.equals(TurnState.VALIDATE_ACTION))
                assert(state.next().equals(TurnState.ELABORATE_ACTION) || state.next().equals(TurnState.DISPLAY_ACTION_VIEW));
            else
                assert(state.next().ordinal() == state.ordinal()+1);
        }
    }


}

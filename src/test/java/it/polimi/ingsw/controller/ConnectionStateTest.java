package it.polimi.ingsw.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectionStateTest {

    //TODO refactor test code for states to avoid repeating similar code

    @Test
    //tests that no states were added, removed or modified and that their order was not changed@Test
    public void testEnumInstances(){
        ConnectionState[] expectedStates= new ConnectionState[9];
        expectedStates[0]=ConnectionState.MEMORIZE_VIEW;
        expectedStates[1]=ConnectionState.DISPLAY_NUM_PLAYERS_VIEW;
        expectedStates[2]=ConnectionState.VALIDATE_NUM_PLAYERS;
        expectedStates[3]=ConnectionState.ELABORATE_NUM_PLAYERS;
        expectedStates[4]=ConnectionState.DISPLAY_NAME_VIEW;
        expectedStates[5]=ConnectionState.VALIDATE_NAME;
        expectedStates[6]=ConnectionState.ELABORATE_NAME;
        expectedStates[7]=ConnectionState.CREATE_PLAYER;
        expectedStates[8]=ConnectionState.DISPLAY_CONNECTION_WAIT_VIEW;
        ConnectionState[] actualStates=ConnectionState.values();
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
        ConnectionState[] states=ConnectionState.values();
        for(ConnectionState state:states){
            if (state.equals(ConnectionState.VALIDATE_NUM_PLAYERS))
                assert(state.next()!=null&&state.next().equals(ConnectionState.DISPLAY_NUM_PLAYERS_VIEW)||state.next().equals(ConnectionState.ELABORATE_NUM_PLAYERS));
            else if(state.equals(ConnectionState.VALIDATE_NAME))
                assert(state.next()!=null&&state.next().equals(ConnectionState.DISPLAY_NAME_VIEW)||state.next().equals(ConnectionState.ELABORATE_NAME));
            else if(state.equals(ConnectionState.DISPLAY_CONNECTION_WAIT_VIEW))
                assert(state.next()==null);
            else
                assert(state.next().ordinal() == state.ordinal()+1);
        }
    }
}

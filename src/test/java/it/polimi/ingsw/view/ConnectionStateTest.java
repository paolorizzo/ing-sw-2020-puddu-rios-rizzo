package it.polimi.ingsw.view;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class ConnectionStateTest {


    //tests that no states were added, removed or modified
    @Test
    public void testStatesCorrect(){
        ConnectionState[] expectedStates = new ConnectionState[7];
        expectedStates[0]= ConnectionState.REQUEST_ID;
        expectedStates[1]= ConnectionState.READ_ID;
        expectedStates[2]= ConnectionState.REQUEST_NUM_PLAYERS;
        expectedStates[3]= ConnectionState.READ_NUM_PLAYERS;
        expectedStates[4]= ConnectionState.PUBLISH_NUM_PLAYERS;
        expectedStates[5]= ConnectionState.PUBLISH_NAME;
        expectedStates[6]= ConnectionState.END;
        ConnectionState[] actualStates = ConnectionState.values();
        assertEquals(actualStates.length, expectedStates.length);
        for(int i=0; i<actualStates.length; i++)
            assertEquals(expectedStates[i], actualStates[i]);
    }

    //tests that the next state function returns an acceptable state
    //does NOT test whether it is logically correct, just that it is acceptable
    @Test
    public void nextStateAcceptable(){
        ConnectionState[] states = ConnectionState.values();
        for(ConnectionState state:states){
            if(state.equals(ConnectionState.READ_ID)) {
                assertNotNull(state.next());
                assert (
                        state.next().equals(ConnectionState.REQUEST_NUM_PLAYERS)
                                ||
                                state.next().equals(ConnectionState.PUBLISH_NUM_PLAYERS)
                );
            }
            else if(state.equals(ConnectionState.PUBLISH_NUM_PLAYERS)) {
                assertNotNull(state.next());
                assert (
                        state.next().equals(ConnectionState.PUBLISH_NAME)
                                ||
                                state.next().equals(ConnectionState.PUBLISH_NUM_PLAYERS)
                );
            }
            else if(state.equals(ConnectionState.PUBLISH_NAME)) {
                assertNotNull(state.next());
                assert (
                        state.next().equals(ConnectionState.PUBLISH_NAME)
                                ||
                                state.next().equals(ConnectionState.END)
                );
            }
            else if(state.equals(ConnectionState.END)) {
                assertNull(state.next());
            }
            else{
                assertNotNull(state.next());
                assertEquals(state.next(), ConnectionState.values()[state.ordinal()+1]);
            }
        }
    }
}

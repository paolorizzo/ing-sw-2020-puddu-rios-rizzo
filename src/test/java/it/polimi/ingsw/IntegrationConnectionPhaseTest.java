package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.middleware.Client;
import it.polimi.ingsw.view.middleware.Server;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegrationConnectionPhaseTest extends MvcIntegrationTest {

    /**
     * Tests that multiple clients can correctly receive an id
     */
    @Test
    public void multipleIdTest()
    {
        int port = 40001;
        int n = 3;

        Server server = buildAndRunServer(port);

        Client[] clients = new Client[n];
        ClientView[] cws = new ClientView[n];

        //connects n clients sequentially, and saves them and their views
        for (int i=0;i<n;i++)
        {
            clients[i] = buildAndRunClient(port);
            cws[i] = clients[i].getClientView();

            safeWaitFor(1);
        }

        safeWaitFor(1);

        //asserts that ids are correct
        for(int i=0;i<n;i++)
        {
            assertEquals(i, cws[i].getId());
        }
    }

    /**
     * Tests that a client can access the game even if started before the server.
     */
    @Test
    public void checkClientBeforeServer()
    {
        int port = 12345;

        Client client = buildAndRunClient(port);

        safeWaitFor(2);

        Server server = buildAndRunServer(port);

        safeWaitFor(2);

        ClientView cw = client.getClientView();

        assert(cw.getId() == 0);
    }

    /**
     * Tests that multiple clients can concurrently connect and then receive an id.
     */
    @Test
    public void checkConcurrentJoin()
    {
        int port = 40777;
        int n = 2;

        Server server = buildAndRunServer(port);

        Client[] clients = new Client[n];
        ClientView[] cws = new ClientView[n];

        //connects n clients concurrently
        for (int i=0;i<n;i++)
        {
            clients[i] = buildAndRunClient(port);

        }

        safeWaitFor(2);
        for (int i=0;i<n;i++)
        {
            cws[i] = clients[i].getClientView();
        }
        //we don't know which thread will run first, so both cases are possible
        assert((cws[0].getId()==0 && cws[1].getId()==1) || (cws[0].getId()==1 && cws[1].getId()==0));
    }

    //TODO once the number of players is handled, we have to test the excluding process
    public void checkExcludedPlayers()
    {

    }
}

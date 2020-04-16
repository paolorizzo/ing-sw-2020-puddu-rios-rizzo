package it.polimi.ingsw;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.middleware.Client;
import it.polimi.ingsw.view.middleware.Server;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegrationConnectionPhaseTest extends MvcIntegrationTest {

    //tests that multiple clients can correctly receive an id
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
            assertEquals(i, cws[i].getID());
        }
    }

    //TODO why do these tests work only if executed one by one?
    @Test
    public void checkClientBeforeServer()
    {
        int port = 12345;

        Client client = buildAndRunClient(port);

        safeWaitFor(5);

        Server server = buildAndRunServer(port);

        safeWaitFor(1);

        ClientView cw = client.getClientView();

        assert(cw.getID() == 0);
    }



}

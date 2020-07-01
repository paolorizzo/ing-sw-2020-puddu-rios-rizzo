package it.polimi.ingsw.controller;

import it.polimi.ingsw.MvcIntegrationTest;
import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.StubView;
import it.polimi.ingsw.view.View;
import javafx.scene.control.Control;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class ControllerTest extends MvcIntegrationTest {

    //tests related to the connection phase

    /**
     * tests that addView works as expected if called a time when the view can actually be added
     */
    @Test
    public void testAddView(){
        Controller c = new Controller();
        View v = new StubView();
        try{
            c.addView(v);
        }catch (InterruptedException e){
            e.getStackTrace();
        }
        assertEquals(false, c.accept);
        assertEquals(0, c.getNextId());
        assert(v == c.getViewMap().get(0));
        assert(c.getViews().contains(v));
        assert(c.getModel().feed.hasObservers());
    }

    /**
     * tests that the viewMap is coherent
     */
    @Test
    public void testViewMap(){
        Controller c = new Controller();
        twoPlayersGame(c);
        assertEquals(0, ( (StubView)( c.getViewMap().get(0) ) ).id);
        assertEquals(1, ( (StubView)( c.getViewMap().get(1) ) ).id);
        connectThirdClient(c);
        assertEquals(2, ( (StubView)( c.getViewMap().get(2) ) ).id);
    }

    /**
     * tests that the ids given by the controller are strictly sequential
     */
    @Test
    public void testIdGeneration(){
        int n=10;
        StubView[] views = new StubView[n];
        Controller c = new Controller();
        for(int i=0;i<n;i++){
            views[i] = new StubView();

            try{
                c.addView(views[i]);
            }catch (InterruptedException e){
                e.getStackTrace();
            }
            c.generateId();
            assertEquals(i, views[i].id);
            c.ackId(i);
        }
    }

    /**
     * tests that receiving an ack puts the controller in a state where it can accept
     * further views, that the first ack puts it in a state where it can accept the
     * numPlayers, and that the number of acks received is correctly updated
     */
    @Test
    public void testAck(){
        Controller c = new Controller();
        int ackedSoFar = c.ackReceived;
        assert(!c.acceptNumPlayers);
        c.ackId(0);
        assert(c.accept);
        assert(c.acceptNumPlayers);
        assertEquals(ackedSoFar+1, c.ackReceived);
    }

    /**
     * tests that the setting of the numPlayers fails if the controller has not
     * yet fully connected the first client
     */
    @Test
    public void testSetNumPlayersFailure(){
        Controller c = new Controller();
        try{
            c.setNumPlayers(0, 2);
            assert(false);
        }
        catch(IncorrectStateException e){
            assert(true);
        }
        View v = new StubView();

        connectFirstClient(c);
        try{
            c.setNumPlayers(0, 2);
            assert(true);
        }
        catch(IncorrectStateException e){
            assert(false);
        }
    }

    /**
     * tests that, even if the controller is in a state where it can accept the
     * numPlayers, the set will have no effect if the id of the setter is not 0
     */
    @Test
    public void testSetNumPlayersWithoutRights(){
        Controller c = new Controller();
        View v = new StubView();
        connectFirstClient(c);
        assert(c.acceptNumPlayers);
        c.setNumPlayers(1, 2);
        assert(!c.getModel().numPlayersIsSet());
    }

    /**
     * tests that, even if the controller is in a state where it can accept the
     * numPlayers, and even if the setter has the right to set,
     * the set will have no effect if the value of numPlayers is not acceptable
     */
    @Test
    public void testSetWrongNumPlayers(){
        Controller c = new Controller();
        View v = new StubView();
        connectFirstClient(c);
        assert(c.acceptNumPlayers);
        for(int i=0;i<100;i++){
            c.setNumPlayers(0, i+4);
            assert(!c.getModel().numPlayersIsSet());
        }
        c.setNumPlayers(0, -1);
        assert(!c.getModel().numPlayersIsSet());
        c.setNumPlayers(0, 0);
        assert(!c.getModel().numPlayersIsSet());
        c.setNumPlayers(0, 1);
        assert(!c.getModel().numPlayersIsSet());
    }

    /**
     * tests that getNumPlayers notifies nothing until the numPlayers has actually been set
     * and that it actually notifies the correct numPlayers once it has been set
     */
    @Test
    public void testGetNumPlayers(){
        Controller c = new Controller();
        connectFirstClient(c);
        c.setNumPlayers(0, 2);
        connectSecondClient(c);
        assertEquals(0, ( (StubView)( c.getViews().get(1) ) ).numPlayers);
        c.getNumPlayers();
        assertEquals(2, ( (StubView)( c.getViews().get(1) ) ).numPlayers);
    }

    /**
     * tests that the information that all players are connected is unknown
     * by the views until they request it AFTER the numPLayers is set
     */
    @Test
    public void testAllPlayersConnected(){
        Controller c = new Controller();
        connectThreeClients(c);
        assert(!( (StubView)( c.getViews().get(0) ) ).allPlayersConnected);
        assert(!( (StubView)( c.getViews().get(1) ) ).allPlayersConnected);
        assert(!( (StubView)( c.getViews().get(2) ) ).allPlayersConnected);
        c.requestAllPlayersConnected();
        assert(!( (StubView)( c.getViews().get(0) ) ).allPlayersConnected);
        assert(!( (StubView)( c.getViews().get(1) ) ).allPlayersConnected);
        assert(!( (StubView)( c.getViews().get(2) ) ).allPlayersConnected);
        c.setNumPlayers(0, 3);
        c.requestAllPlayersConnected();
        assert(( (StubView)( c.getViews().get(0) ) ).allPlayersConnected);
        assert(( (StubView)( c.getViews().get(1) ) ).allPlayersConnected);
        assert(( (StubView)( c.getViews().get(2) ) ).allPlayersConnected);
    }

    /**
     * tests that the setting of the name fails if either:
     * 0: the requesting client's id is not either 0, 1, or 2
     * 1: the requesting client has not yet acked its id
     * 2: the numPlayers is not yet set
     * 3: there is already a declared name for the given id
     * 4: the id is 2 and the numPlayers is 2: in this case, the third player should not be able to get a name
     * 5: the given name has already been claimed by someone else
     */
    @Test
    public void testSetNameFailure(){
        Controller c = new Controller();

        c.setName(-1, "aldo");                      //0
        assert(c.getModel().getPlayers().isEmpty());

        c.setName(3, "aldo");                       //0
        assert(c.getModel().getPlayers().isEmpty());

        StubView v0 = new StubView();
        try{
            c.addView(v0);
        }catch (InterruptedException e){
            e.getStackTrace();
        }
        c.generateId();
        try{
            c.setName(0, "aldo");                   //1
            assert(false);
        }
        catch(IncorrectStateException e){
            assert(true);
        }
        assert(c.getModel().getPlayers().isEmpty());

        c.ackId(0);
        try{
            c.setName(0, "aldo");                   //2
            assert(false);
        }
        catch(IncorrectStateException e){
            assert(true);
        }
        assert(c.getModel().getPlayers().isEmpty());

        c.setNumPlayers(0, 2);
        c.setName(0, "aldo");
        assert(!c.getModel().getPlayers().isEmpty());
        try{
            c.setName(0, "giovanni");               //3
            assert(false);
        }
        catch(IllegalArgumentException e){
            assert(true);
        }
        assert(!v0.name.equals("giovanni"));

        StubView v1 = connectSecondClient(c);
        StubView v2 = connectThirdClient(c);
        try{
            c.setName(2, "giacomo");                //4
            assert(false);
        }
        catch(IllegalArgumentException e){
            assert(true);
        }
        assert(null == v2.name);

        c.setName(1, "aldo");                       //5
        assert(null == v1.name);

    }

    /**
     * tests that the setName actually results in the name being notified through the feed
     * if the proper conditions are met
     */
    @Test
    public void testSetName(){
        Controller c = new Controller();
        threePlayersGame(c);
        c.setName(0, "aldo");
        c.setName(1, "giovanni");
        c.setName(2, "giacomo");
        assertEquals(0, ( (StubView)( c.getViewMap().get(0) ) ).id);
        assertEquals("aldo", ( (StubView)( c.getViewMap().get(0) ) ).name);
        assertEquals("giovanni", ((StubView)(c.getViewMap().get(1))).name);
        assertEquals("giacomo", ((StubView)(c.getViewMap().get(2))).name);
    }

    /**
     * tests that after a deleteId the related view is no longer known by the controller
     */
    @Test
    public void testDeleteClient(){
        Controller c = new Controller();
        threePlayersGame(c);
        StubView v3 = new StubView();
        try{
            c.addView(v3);
        }catch (InterruptedException e){
            e.getStackTrace();
        }
        c.generateId();
        c.ackId(3);
        c.deleteId(3);
        assert(!c.getViews().contains(v3));
        assert(c.getViewMap().get(3) == null);
    }

    //tests related to the setup phase

    /**
     * tests that after a request deck all clients receive a correctly constructed deck
     */
    @Test
    public void testRequestDeck(){
        Controller c = new Controller();
        connectionPhase2(c);
        c.requestDeck();
        List<Card> expectedCards = new Deck().getCards();
        List<Card> cards0 = getStubView(c, 0).deck.getCards();
        List<Card> cards1 = getStubView(c, 1).deck.getCards();

        assertEquals(expectedCards.size(), cards0.size());
        assertEquals(expectedCards.size(), cards1.size());
        for(int i=0; i<expectedCards.size();i++){
            assertEquals(expectedCards.get(i), cards0.get(i));
            assertEquals(expectedCards.get(i), cards1.get(i));
        }
    }

    /**
     * tests that publishCards correctly fails if its conditions are not met. It should fail if:
     * 0: it isn't the first client that publishes them
     * 1: the number of cards chosen is not equal to the number of players in the game
     * 2: the cards are already chosen
     */
    @Test
    public void testPublishCardsFailure(){
        Controller c = new Controller();
        connectionPhase3(c);
        ArrayList<Integer> wrongCards = new ArrayList<Integer>();
        wrongCards.add(1);
        wrongCards.add(2);
        wrongCards.add(3);
        c.publishCards(1, wrongCards);                                 //0
        assert(!c.getModel().game.cardsAlreadyChosen());

        wrongCards.clear();
        wrongCards.add(1);
        wrongCards.add(2);
        c.publishCards(0, wrongCards);                                 //1
        assert(!c.getModel().game.cardsAlreadyChosen());
        wrongCards.add(3);
        wrongCards.add(4);
        c.publishCards(0, wrongCards);                                 //1
        assert(!c.getModel().game.cardsAlreadyChosen());

        ArrayList<Integer> rightCards = new ArrayList<Integer>();
        rightCards.add(1);
        rightCards.add(2);
        rightCards.add(3);
        c.publishCards(0, rightCards);
        wrongCards.clear();
        wrongCards.add(4);
        wrongCards.add(5);
        wrongCards.add(6);
        c.publishCards(0, wrongCards);                                  //2
        assert(c.getModel().game.cardsAlreadyChosen());
        List<Card> actualCards = c.getModel().game.getChosenCards();
        assert(!actualCards.equals(toCards(wrongCards)));
    }

    /**
     * tests that if the right conditions are met, publishing the cards results in them being saved on the model
     */
    @Test
    public void testPublishCards(){
        Controller c = new Controller();
        connectionPhase3(c);
        List<Integer> chosenNums = sampleNums(3);
        c.publishCards(0, chosenNums);
        assertEquals(toCards(chosenNums), c.getModel().game.getChosenCards());
    }

    /**
     * tests that if a thread requests the cards before they have been published,
     * they don't get a response
     * but after the cards are published, they get a response
     */
    @Test
    public void testPublishCardsWakeup(){
        final Controller c = new Controller();
        connectionPhase2(c);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    c.requestCards(1);
                }
                catch(InterruptedException e){
                    assert(false);
                }
            }
        }).start();

        assert(getStubView(c, 1).chosenCards == null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                c.publishCards(0, sampleNums(2));
            }
        }).start();

        safeWaitFor(1);
        assertEquals(toCards(sampleNums(2)), getStubView(c, 1).chosenCards);
    }

    /**
     * tests that the setCard fails if
     * 0: it is not the requesting client's turn or
     * 1: the card has already been claimed
     */
    @Test
    public void testSetCardFailure(){
        Controller c = new Controller();
        connectionPhase3(c);    //after this, it is considered player 1's turn
        c.setCard(2, 1);                                            //0
        assert(getStubView(c, 2).god == null);
        c.setCard(1, 1);    //it is now player 2's turn
        c.setCard(2, 1);                                            //1
        assert(getStubView(c, 2).god == null);
    }

    /**
     * tests that if the proper conditions are met the setCards results in the correct view receiving its god through the feed
     */
    @Test
    public void testSetCard(){
        Controller c = new Controller();
        connectionPhase3(c);    //after this, it is considered player 1's turn
        c.publishCards(0, sampleNums(3));
        c.setCard(1, 1);

        assertEquals( toCards(sampleNums(3)).get(0), getStubView(c, 1).god);
    }

    /**
     * tests that the request to setup a worker receives no answer until it is the requesting player's turn
     */
    @Test
    public void testRequestSetupWorkerWait(){
        final Controller c = new Controller();
        setupToGods(c, 3);  //player 1's turn

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    getStubView(c, 2).possibleActions = null;
                    c.requestToSetupWorker(2);                      //player 2 tries to request the setup while not in their turn
                }
                catch(InterruptedException e){
                    assert(false);
                }
            }
        }).start();

        assert(getStubView(c, 2).possibleActions == null);
        //printTurn(c);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    c.requestToSetupWorker(1);                      //player 1 requests the setup in their turn
                    c.setupWorker(1, new SetupAction(new Worker(Sex.FEMALE, c.getModel().getPlayers().get(1)).toString(), 0, 0 ) );
                    c.requestToSetupWorker(1);
                    c.setupWorker(1, new SetupAction(new Worker(Sex.MALE, c.getModel().getPlayers().get(1)).toString(), 0, 1 ) );
                }
                catch(InterruptedException e){
                    assert(false);
                }
            }
        }).start();

        safeWaitFor(1);
        //printTurn(c);
        assertNotEquals(null, getStubView(c, 2).possibleActions);
    }

    /**
     * tests that a setup fails if:
     * 0: it is not the client's turn
     * 1: the setup action is not possible
     */
    @Test
    public void testSetupFailure(){
        Controller c = new Controller();
        setupToGods(c, 3);
        c.setupWorker(2, new SetupAction(new Worker(Sex.FEMALE, c.getModel().getPlayers().get(2)).toString(), 0, 1 ) );         //0
        assert(!c.getModel().workerIsPlaced(2, Sex.FEMALE));

        try {
            c.requestToSetupWorker(1);
        }
        catch(InterruptedException e){
            assert false;
        }
        try{
            c.setupWorker(1, new SetupAction(new Worker(Sex.FEMALE, c.getModel().getPlayers().get(2)).toString(), 10, 1 ) );    //1
            assert(false);
        }
        catch(IllegalArgumentException e){
            assert(true);
        }

        assert(!c.getModel().workerIsPlaced(1, Sex.FEMALE));
    }

    /**
     * tests that it is possible to perform both the connection and setup phase for 2 players
     * by testing that all the worker end up placed in the correct position
     * and that the correct views have received the correct gods
     */
    @Test
    public void testSetupPhase2(){
        int n=2;
        Controller c = new Controller();
        fullSetupPhase(c, n);
        System.out.println("setup complete");
        int curr;
        for(int i=0;i<n;i++){
            curr = (1+i)%n;
            assertEquals(2*curr, c.getModel().getWorkerPos(curr, Sex.FEMALE, 'x'));
            assertEquals(0, c.getModel().getWorkerPos(curr, Sex.FEMALE, 'y'));
        }
        List<Card> gods = toCards(sampleNums(n));
        assertEquals(gods.get(0), getStubView(c, 1).god);
        assertEquals(gods.get(1), getStubView(c, 0).god);
    }

    /**
     * tests that it is possible to perform both the connection and setup phase for 3 players
     * by testing that all the worker end up placed in the correct position
     * and that the correct views have received the correct gods
     */
    @Test
    public void testSetupPhase3(){
        int n=3;
        Controller c = new Controller();
        fullSetupPhase(c, n);
        System.out.println("setup complete");
        int curr;
        for(int i=0;i<n;i++){
            curr = (1+i)%n;
            assertEquals(2*curr, c.getModel().getWorkerPos(curr, Sex.FEMALE, 'x'));
            assertEquals(0, c.getModel().getWorkerPos(curr, Sex.FEMALE, 'y'));
        }
        List<Card> gods = toCards(sampleNums(n));
        assertEquals(gods.get(0), getStubView(c, 1).god);
        assertEquals(gods.get(1), getStubView(c, 2).god);
        assertEquals(gods.get(2), getStubView(c, 0).god);
    }

    //turn phase tests

    /**
     * tests that the requestAction does nothing until it is the requester's turn
     */
    @Test
    public void testRequestActionWait(){
        final Controller c = new Controller();
        fullSetupPhase(c, 3);       //it is player 1's turn

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    getStubView(c, 2).possibleActions = null;
                    c.requestActions(2);
                }
                catch(InterruptedException e){
                    assert(false);
                }
            }
        }).start();

        safeWaitFor(1);
        assertEquals(null, getStubView(c, 2).possibleActions);
        c.getModel().game.nextTurn();
        synchronized (c){
            c.notifyAll();
        }
        safeWaitFor(1);
        printTurn(c);
        assertNotEquals(null, getStubView(c, 2).possibleActions);
    }

    /**
     * tests that a correct move publishAction results in the position of the worker being updated on the model
     */
    @Test
    public void testAction(){
        Controller c = new Controller();
        fullSetupPhase(c, 2);
        try{
            c.requestActions(1);
            getStubView(c, 1).printPossibleActions();
        }
        catch(InterruptedException e){
            assert(false);
        }
        assertEquals(2, c.getModel().getWorkerPos(1, Sex.FEMALE, 'x'));
        assertEquals(0, c.getModel().getWorkerPos(1, Sex.FEMALE, 'y'));     //initial position 2;0
        c.publishAction(1, new MoveAction( new Worker(Sex.FEMALE, c.getModel().getPlayer(1) ).toString(), 2, 1, Direction.SAME, 2, 0));
        assertEquals(2, c.getModel().getWorkerPos(1, Sex.FEMALE, 'x'));
        assertEquals(1, c.getModel().getWorkerPos(1, Sex.FEMALE, 'y'));     //final position 2;1
    }

    /**
     * tests that a move action has no effect if it is not the requesting player's turn
     */
    @Test
    public void testActionFailure(){
        Controller c = new Controller();
        fullSetupPhase(c, 2);
        assertEquals(0, c.getModel().getWorkerPos(0, Sex.FEMALE, 'x'));
        assertEquals(0, c.getModel().getWorkerPos(0, Sex.FEMALE, 'y'));     //initial position 0;0
        c.publishAction(0, new MoveAction( new Worker(Sex.FEMALE, c.getModel().getPlayer(0) ).toString(), 0, 1, Direction.SAME, 0, 0));
        assertEquals(0, c.getModel().getWorkerPos(0, Sex.FEMALE, 'x'));
        assertEquals(0, c.getModel().getWorkerPos(0, Sex.FEMALE, 'y'));     //final position 0;0
    }

    /**
     * tests that if the actions performed in a turn make it logically possible to finish it,
     * it is actually possible to voluntarily end it
     */
    @Test
    public void testVoluntaryEndOfTurn(){
        Controller c = new Controller();
        fullSetupPhase(c, 3);   //it is now player 1's turn

        //moving
        try{
            c.requestActions(1);
            getStubView(c, 1).printPossibleActions();
        }
        catch(InterruptedException e){
            assert(false);
        }
        c.publishAction(1, new MoveAction( new Worker(Sex.FEMALE, c.getModel().getPlayer(1) ).toString(), 2, 1, Direction.SAME, 2, 0));

        //building
        try{
            c.requestActions(1);
            getStubView(c, 1).printPossibleActions();
        }
        catch(InterruptedException e){
            assert(false);
        }
        c.publishAction(1, new BuildAction( new Worker(Sex.FEMALE, c.getModel().getPlayer(1) ).toString(), 2, 0, Piece.LEVEL1));

        c.publishVoluntaryEndOfTurn(1);
        assertEquals(2, c.getModel().game.getCurrentPlayerId());
    }

    /**
     * tests that it is possible to play some correct turns without getting any exceptions, and resulting in the correct position of the workers
     */
    @Test
    public void testSomeTurns(){
        Controller c = new Controller();
        playSomeTurns(c);
        printWorkerPositions(c);
        // checks the position of all workers
        assertEquals(4, c.getModel().getPlayer(0).getWorker(Sex.FEMALE).getSpace().getPosX());
        assertEquals(1, c.getModel().getPlayer(0).getWorker(Sex.FEMALE).getSpace().getPosY());
        assertEquals(0, c.getModel().getPlayer(0).getWorker(Sex.MALE).getSpace().getPosX());
        assertEquals(2, c.getModel().getPlayer(0).getWorker(Sex.MALE).getSpace().getPosY());

        assertEquals(2, c.getModel().getPlayer(1).getWorker(Sex.FEMALE).getSpace().getPosX());
        assertEquals(0, c.getModel().getPlayer(1).getWorker(Sex.FEMALE).getSpace().getPosY());
        assertEquals(0, c.getModel().getPlayer(1).getWorker(Sex.MALE).getSpace().getPosX());
        assertEquals(3, c.getModel().getPlayer(1).getWorker(Sex.MALE).getSpace().getPosY());

        assertEquals(4, c.getModel().getPlayer(2).getWorker(Sex.FEMALE).getSpace().getPosX());
        assertEquals(0, c.getModel().getPlayer(2).getWorker(Sex.FEMALE).getSpace().getPosY());
        assertEquals(3, c.getModel().getPlayer(2).getWorker(Sex.MALE).getSpace().getPosX());
        assertEquals(2, c.getModel().getPlayer(2).getWorker(Sex.MALE).getSpace().getPosY());
    }

    /**
     * does nothing
     */
    @Test
    public void testKill(){
        Controller c = new Controller();
        c.kill();
        assert(true);
    }

    /**
     * tests that if there is a disconnection and the game hasn't
     * ended, the game is not deleted
     */
    @Test
    public void testDisconnection(){
        Controller c = new Controller();
        playSomeTurns(c);
        c.getModel().isSaved();
        c.getModel().save();
        assert c.getModel().isSaved();
        c.handleDisconnection();
        assert c.getModel().isSaved();
    }

    /**
     * tests that the information about the availability
     * about a saved game is correctly communicated to the views
     */
    @Test
    public void testIsGameAvailable(){
        Controller c = new Controller();
        playSomeTurns(c);
        assert !getStubView(c, 0).gameSaved;
        c.getModel().isSaved();
        c.getModel().save();
        c.isGameAvailable();
        assert getStubView(c, 0).gameSaved;
    }

    /**
     * tests that if a restore happens, the views are notified
     */
    @Test
    public void testRestore(){
        Controller c1 = new Controller();
        playSomeTurns(c1);
        c1.getModel().isSaved();
        c1.getModel().save();
        Controller c2 = new Controller();
        connectionPhase2(c2);
        c2.restore(0, true);
        c2.willRestore();
        assert(getStubView(c2, 1).restore == true);

    }





    //utility methods

    /**
     * mimics the connection of 1 client to the given controller
     * @param c controller to which the fake clients will be connected
     * @return the added view
     */

    private StubView connectFirstClient(Controller c){
        StubView view0 = new StubView();
        try{
            c.addView(view0);
        }catch (InterruptedException e){
            e.getStackTrace();
        }
        c.generateId();
        c.ackId(0);
        return view0;
    }

    /**
     * mimics the connection of a second client to the given controller
     * works only after a first client has been connected
     * @param c controller to which the fake clients will be connected
     * @return the added view
     */
    private StubView connectSecondClient(Controller c){
        StubView view1 = new StubView();
        try{
            c.addView(view1);
        }catch (InterruptedException e){
            e.getStackTrace();
        }
        c.generateId();
        c.ackId(1);
        return view1;
    }

    /**
     * mimics the connection of a third client to the given controller
     * works only after a first and a second client have been connected
     * @param c controller to which the fake clients will be connected
     * @return the added view
     */
    private StubView connectThirdClient(Controller c){
        StubView view2 = new StubView();
        try{
            c.addView(view2);
        }catch (InterruptedException e){
            e.getStackTrace();
        }
        c.generateId();
        c.ackId(2);
        return view2;
    }

    /**
     * mimics the connection of 2 clients to the given controller
     * @param c controller to which the fake clients will be connected
     */
    private void connectTwoClients(Controller c){
        connectFirstClient(c);
        connectSecondClient(c);
    }

    /**
     * mimics the connection of 3 clients to the given controller
     * @param c controller to which the fake clients will be connected
     */
    private void connectThreeClients(Controller c){
        connectFirstClient(c);
        connectSecondClient(c);
        connectThirdClient(c);
    }

    /**
     * mimics the connection of 2 clients to the given controller
     * and the setting of 2 as numPlayers
     * @param c controller to which the fake clients will be connected
     */
    private void twoPlayersGame(Controller c){
        connectTwoClients(c);
        c.setNumPlayers(0, 2);
    }

    /**
     * mimics the connection of 3 clients to the given controller
     * and the setting of 3 as numPlayers
     * @param c controller to which the fake clients will be connected
     */
    private void threePlayersGame(Controller c){
        connectThreeClients(c);
        c.setNumPlayers(0, 3);
    }

    /**
     * mimics a full correct connection phase involving two clients
     * their names are "aldo" and "giovanni"
     * after this, it is player 1's turn
     */
    private void connectionPhase2(Controller c){
        twoPlayersGame(c);
        c.setName(0, "aldo");
        c.setName(1, "giovanni");
    }

    /**
     * mimics a full correct connection phase involving three clients
     * their names are "aldo", "giovanni" and "giacomo"
     * after this, it is player 1's turn
     */
    private void connectionPhase3(Controller c){
        threePlayersGame(c);
        c.setName(0, "aldo");
        c.setName(1, "giovanni");
        c.setName(2, "giacomo");
    }

    /**
     * encapsulates the steps need to acquire a view form a controller and cast it as a StubView
     * @param c controller from which to get the StubView
     * @param id id of the desired StubView
     * @return the desired view casted as StubView
     */
    private StubView getStubView(Controller c, int id){
        return (StubView)(c.getViewMap().get(id));
    }

    /**
     *
     * @param chosenNums a list of integer that represents the numbers of the cards chosen
     * @return a list of the chosen cards corresponding to the chosen numbers
     */
    private ArrayList<Card> toCards(List<Integer> chosenNums){
        List<Card> possibleCards = new Deck().getCards();
        ArrayList<Card> chosenCards = new ArrayList<Card>();
        for(int num:chosenNums){
            for(Card possibleCard:possibleCards){
                if(possibleCard.getNum() == num)
                    chosenCards.add(possibleCard);
            }
        }
        return chosenCards;
    }

    /**
     *
     * @param n the number of sample card numbers desired
     * @return a list of n integers that represent valid cards
     */
    private ArrayList<Integer> sampleNums(int n){
        ArrayList<Integer> sampleNums = new ArrayList<>();
        for(int i=0; i<n;i++)
            sampleNums.add(i+1);
        return sampleNums;
    }

    /**
     * performs the connection phase and the setup phase up to the choice of gods for n players
     * players 1 and 2 get cards 1 and 2, player 0 gets the remaining card 3
     * after this, it is player 1's turn
     * @param c the controller on which to perform the setup
     * @param n the number of players desired for the game
     */
    private void setupToGods(Controller c, int n){
        if(n == 2){
            connectionPhase2(c);
            c.publishCards(0, sampleNums(2));
            c.setCard(1, 1);
            c.setCard(0, 2);
        }
        else if(n == 3){
            connectionPhase3(c);
            c.publishCards(0, sampleNums(3));
            c.setCard(1, 1);
            c.setCard(2, 2);
            c.setCard(0, 3);
        }
        else
            throw new IllegalArgumentException("cannot perform setup phase for "+ n + " players");
    }

    /**
     * prints the turn
     * @param c the controller from which to get the game
     */
    private void printTurn(Controller c){
        System.out.println("" + c.getModel().game.getCurrentPlayerId() + " playing");
    }

    /**
     * prints the ownership of each worker
     * @param c the controller from which to get the info
     */
    private void printWorkerOwnership(Controller c){
        int n = c.getModel().game.getNumPlayers();
        for (int i=0;i<n;i++){
            System.out.println("player " + i + " has workers");
            System.out.println("F who thinks it belongs to " + c.getModel().getPlayer(i).getWorker(Sex.FEMALE).getPlayer());
            System.out.println("M who thinks it belongs to " + c.getModel().getPlayer(i).getWorker(Sex.MALE).getPlayer());
        }
    }

    /**
     * performs the entire connection and setup phase
     * after this, it is player 1's turn
     * @param c the controller on which to perform the phases
     * @param n the number of players
     */
    public void fullSetupPhase(Controller c, int n){
        if(!(n==2||n==3))
            throw new IllegalArgumentException("cannot perform setup phase for " + n + " players");

        setupToGods(c, n);
        printWorkerOwnership(c);
        int curr ;
        for(int i=0;i<n;i++){
            curr = (1+i)%n;
            printWorkerPositions(c);
            printTurn(c);
            System.out.println("setting up player " + curr);

            try{
                c.requestToSetupWorker(curr);
            }
            catch(InterruptedException e){
                throw new RuntimeException("request setup worker interrupted");
            }
            c.setupWorker(curr, new SetupAction(new Worker(Sex.FEMALE, c.getModel().getPlayers().get(curr)).toString(), 2*curr, 0 ) );
            printWorkerPositions(c);
            try{
                c.requestToSetupWorker(curr);
            }
            catch(InterruptedException e){
                throw new RuntimeException("request setup worker interrupted");
            }
            c.setupWorker(curr, new SetupAction(new Worker(Sex.MALE, c.getModel().getPlayers().get(curr)).toString(), 2*curr, 2 ) );
            printWorkerPositions(c);
        }
    }


    /**
     * executes some scripted actions, for 5 total turns
     * @param c the controller on which to operate
     */
    public void playSomeTurns(Controller c){
        int n = 3;
        fullSetupPhase(c, n);
        // initialize sexes
        Sex[] sexes = {Sex.FEMALE, Sex.MALE, Sex.MALE};
        // initialize positions
        int[][] xPositions = new int[3][];
        int[][] yPositions = new int[3][];
        xPositions[0] = new int[]{0, 0, 1, 2, 3, 4};
        yPositions[0] = new int[]{0, 1, 1, 1, 1, 1};
        xPositions[1] = new int[]{2, 1, 1, 1, 0, 0};
        yPositions[1] = new int[]{2, 2, 3, 4, 4, 3};
        xPositions[2] = new int[]{4, 4, 4, 3, 3, 3};
        yPositions[2] = new int[]{2, 3, 4, 4, 3, 2};
        //execute actions following positions
        for(int i=0;i<5;i++){
            for(int j=0;j<n;j++){
                int curr = (j+1)%n;
                safeAction(c, curr, sexes[curr], xPositions[curr][i], yPositions[curr][i], xPositions[curr][i+1], yPositions[curr][i+1]);
                printTurn(c);
            }
        }
    }


    /**
     * executes an action safely, building a level 1 block on the previous position
     * @param c the controller on which to operate
     * @param id the id of the player
     * @param s the sex of the worker chosen for the action
     * @param startX the starting x coordinate of the worker. Will be built upon
     * @param startY the starting y coordinate of the worker. Will be built upon
     * @param targetX the target x coordinate of the worker. No check is made
     * @param targetY the target y coordinate of the worker. No check is made
     */
    public void safeAction(Controller c, int id, Sex s, int startX, int startY, int targetX, int targetY){
        Worker w = c.getModel().getPlayer(id).getWorker(s);

        try{
            c.requestActions(id);
            //getStubView(c, id).printPossibleActions();
        }
        catch(InterruptedException e){
            throw new RuntimeException("request move action interrupted");
        }
        c.publishAction(id, new MoveAction( w.toString(), targetX, targetY, Direction.SAME, startX, startY));

        try{
            c.requestActions(id);
            //getStubView(c, id).printPossibleActions();
        }
        catch(InterruptedException e){
            throw new RuntimeException("request build action interrupted");
        }
        c.publishAction(id, new BuildAction( w.toString(), startX, startY, Piece.LEVEL1));

        try{
            c.requestActions(id);
            //getStubView(c, id).printPossibleActions();
        }
        catch(InterruptedException e){
            throw new RuntimeException("request build action interrupted");
        }
    }

    /**
     * prints the positions of all the workers
     * @param c the controller from which to get the info
     */
    public void printWorkerPositions(Controller c){
        int numPlayers = c.getModel().game.getNumPlayers();
        for(int i=0;i<numPlayers;i++){
            for(Sex s:Sex.values()){
                if(c.getModel().getPlayer(i).getWorker(s).getSpace() != null){
                    Space space = c.getModel().getPlayer(i).getWorker(s).getSpace();
                    System.out.println("id = " + i + ", sex = " + s.name() + ", x = " + space.getPosX() + ", y = " + space.getPosY());
                }
                else{
                    System.out.println("player " + i + " hasn't set up worker " + s.name());
                }
            }
        }
    }
}

package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Action;
import it.polimi.ingsw.model.SetupAction;
import it.polimi.ingsw.model.Sex;

import java.util.List;

public interface ControllerInterface {

    //connection phase
    public void generateId();
    public void ackId(int id);
    public void setNumPlayers(int id, int numPlayers);
    public void getNumPlayers();
    public void requestAllPlayersConnected();
    public void setName(int id, String name);
    //restore phase
    public void isGameAvailable();
    public void restore(int id, boolean intentToRestore);
    public void willRestore();
    //setup phase
    public void requestDeck();
    public void publishCards(int id, List<Integer> numCards);
    public void requestCards(int id) throws InterruptedException;
    public void setCard(int id, int numCard);
    public void requestToSetupWorker(int id) throws InterruptedException;
    public void setupWorker(int id, SetupAction action);
    //game phase
    public void requestActions(int id) throws InterruptedException;
    public void publishAction(int id, Action action);
    public void publishVoluntaryEndOfTurn(int id);
    public void deleteId(int id);
    public void kill();

}

package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observation.UserInterfaceObserver;
import it.polimi.ingsw.view.middleware.NetworkInterface;

import java.util.List;
import java.util.Map;

/**
 * A class that takes the role of View in the MVC pattern on the client
 * On the client, the Client class takes the role of both Controller and model
 * and communicates with the ClientView accordingly, with the exception
 * of interactions that are outside of the MVC pattern, such as the setting of the user interface
 */
public class ClientView extends View implements UserInterfaceObserver
{
    //finite state machine states
    ConnectionState currentConnectionState;
    RestoreState currentRestoreState;
    SetupState currentSetupState;
    GameState currentGameState;
    //updates will modify these objects
    private int id;
    private UserInterface ui;
    private NetworkInterface net;

    /**
     * constructs the ClientView, with info regarding its controller
     * and its network interface
     * @param controller the controller of this view, which in this case is the Client
     * @param net the network interface
     */
    public ClientView(ControllerInterface controller, NetworkInterface net){
        super(controller);
        this.net = net;
        id = -1;
    }

    /**
     * constructs the ClientView, with info about its controller,
     * which in this case is the Client
     * sets the id to a default value
     * @param controller the controller of this View, which in this case is the Client
     */
    public ClientView(ControllerInterface controller){
        super(controller);
        id = -1;
    }

    /**
     * notifies the loss of the connection to the client view
     */
    public void connectionLost()
    {
        //TODO inform the client whether the game has been saved
        net.closeConnection();
        getUi().showDisconnection("Sorry, it is impossible to reach the server at this time!");
    }

    /**
     * setter for the ui
     * @param ui the reference to the ui
     */
    public void setUi(UserInterface ui)
    {
        this.ui = ui;
    }

    /**
     * getter for the ui that's being used
     * @return the ui
     */
    public UserInterface getUi()
    {
        return ui;
    }

    /**
     * getter for the id
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * asks the user to insert an id and a port through the ui
     */
    public void askIpAndPort()
    {
        getUi().askIpAndPort();
    }

    /**
     * setter for the number of players
     * @param numPlayers the value to be given to the number of players
     */
    public void setNumPlayers(int numPlayers)
    {
        getUi().setNumPlayers(numPlayers);
    }

    /**
     * setter for the id
     * @param id the value to be given to the id
     */
    public void setID(int id)
    {
        this.id = id;
    }

    //updates relative to UserInterface
    /**
     * sets the ip address that the user chose
     * @param ip the chosen ip address
     */
    @Override
    public synchronized void updateReadIp(String ip)
    {
        String ipv4_regex = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";
        if(!ip.matches(ipv4_regex)){
            getUi().showError("Wrong IP format!");
            getUi().askIpAndPort();
            return;
        }
        net.setIp(ip);
    }

    /**
     * sets the port that the user chose
     * @param port the chosen port
     */
    @Override
    public synchronized void updateReadPort(String port)
    {
        if(port.length() == 0 || !port.chars().allMatch( Character::isDigit ) || Integer.parseInt(port)<1024 || Integer.parseInt(port) > 65535){
            getUi().showError("Wrong port format!");
            getUi().askIpAndPort();
            return;
        }

        if(net.getIp() != null)
            net.setPort(Integer.parseInt(port));
    }

    /**
     * forwards to the Connection FSM the number of players that the client chose
     * @param numPlayers the chosen number of players
     */
    @Override
    public synchronized void updateReadNumPlayers(int numPlayers){
        if(currentConnectionState.equals(ConnectionState.READ_NUM_PLAYERS))
            currentConnectionState.execute(this, numPlayers);
    }

    /**
     * forwards to the connection FSM the name that the player chose
     * @param name the chosen name
     */
    @Override
    public synchronized void updateReadName(String name){
        if(name == null || name.length()<1 || name.length()>8){
            getUi().showError("Length must be between 1 and 8 characters!");
            getUi().askUsername();
            return;
        }
        if(currentConnectionState.equals(ConnectionState.READ_NAME))
            currentConnectionState.execute(this, name);
    }

    /**
     * forwards the choice regarding the restoration of a previous game to the Restore FSM
     * @param restore the choice regarding the possible restoration
     */
    @Override
    public synchronized void updateReadRestore(boolean restore){
        if(currentRestoreState.equals(RestoreState.READ_RESTORE))
            currentRestoreState.execute(restore);
    }

    /**
     * forwards to the setup FSM the info regarding the card chosen from the deck by the player
     * @param numCard the number of the chosen card
     */
    @Override
    public synchronized void updateReadNumCard(int numCard) {
        if(currentSetupState.equals(SetupState.READ_CARD))
            currentSetupState.execute(this, numCard);
    }

    /**
     * forwards to the Setup FSM the number of the card that the player picked from the possible cards
     * @param numCard the chosen card
     */
    @Override
    public synchronized void updateReadGod(int numCard) {
        if(currentSetupState.equals(SetupState.READ_GOD))
            currentSetupState.execute(this, numCard);
    }

    /**
     * forwards the action the player took to the correct FSM
     * this is either the setup FSM if the action is a SetupAction,
     * and the Game FSM otherwise
     * @param action the chosen action
     */
    @Override
    public synchronized void updateReadAction(Action action) {
        if(currentSetupState!= null && currentSetupState.equals(SetupState.READ_SETUP_WORKER))
            currentSetupState.execute(this, action);
        if(currentGameState!= null && currentGameState.equals(GameState.READ_ACTION))
            currentGameState.execute(this, action);
    }

    /**
     * forwards the decision to end the turn prematurely to the Game FSM
     */
    @Override
    public synchronized void updateReadVoluntaryEndOfTurn() {
        if(currentGameState!= null && currentGameState.equals(GameState.READ_ACTION)){
            currentGameState = GameState.PUBLISH_VOLUNTARY_END_OF_TURN;
            currentGameState.execute(this, null);
        }
    }

    //general server updates

    /**
     * if the ok is for this client, forwards to the right FSM if it is expected
     * or throws an exception if it was not expected
     * @param id the id of the client that the ok is directed to
     */
    @Override
    public synchronized void updateOk(int id) {
        if(id == this.id){
            if(currentConnectionState.equals(ConnectionState.RECEIVE_CHECK)){
                currentConnectionState.execute(this, null);
            }
            else if(currentSetupState!=null && currentSetupState.equals(SetupState.RECEIVE_CHECK)){
                currentSetupState.execute(this, null);
            }
            else if(currentRestoreState != null && currentRestoreState.equals(RestoreState.RECEIVE_CHECK)){
                currentRestoreState.execute(null);
            }
            else{
                throw new IncorrectStateException("Received an okay for some communication, but was not waiting for it. I am in state " + currentConnectionState.name());
            }
        }
    }

    /**
     * if the ko is directed to this client, forwards it to the FSM that is expecting it,
     * or else throws an exception if no feedback was expected
     * @param id the id of the client that the ko is directed to
     * @param problem a description of the reason of the failure
     */
    @Override
    public synchronized void updateKo(int id, String problem){
        if(id == this.id){
            getUi().showError(problem);
            if(currentConnectionState.equals(ConnectionState.RECEIVE_CHECK)){
                currentConnectionState.execute(this, problem);
            }
            else if(currentRestoreState != null && currentRestoreState.equals(RestoreState.RECEIVE_CHECK)){
                currentRestoreState.execute(problem);
            }
            else{
                throw new IncorrectStateException("Received a ko for some communication, but was not waiting for it. I am in state " + currentConnectionState.name());
            }
        }
    }

    /**
     * logs the info regarding whose turn it is.
     * if it is this client's turn, selects the appropriate FSM and state to run
     * @param id the id of the client whose turn it is
     * @param possibleActions a list of possible actions for that client
     * @param canEndOfTurn true if the client can decide to end their turn at this time
     */
    @Override
    public synchronized void updateCurrentPlayer(int id, List<Action> possibleActions, boolean canEndOfTurn) {
        getUi().setCurrentPlayer(id);
        if(id == getId() && currentSetupState!=null && currentSetupState.equals(SetupState.ASK_SETUP_WORKER))
            currentSetupState.execute(this, possibleActions);
        if(id == getId() && currentGameState!=null && currentGameState.equals(GameState.RECEIVE_ACTIONS)){
            if(canEndOfTurn){
                currentGameState = GameState.ASK_OPTIONAL_ACTION;
            }
            currentGameState.execute(this, possibleActions);
        }
    }

    //connection phase updates

    /**
     * starts the Connection FSM
     */
    @Override
    public synchronized void updateStart(){
        if(currentConnectionState.equals(ConnectionState.READY))
            currentConnectionState.execute(this, null);
    }

    /**
     * forwards the id to the Connection FSM, if it was expecting it
     * @param id the id given by the server
     */
    @Override
    public synchronized void updateID(int id){

        if(currentConnectionState.equals(ConnectionState.RECEIVE_ID))
            currentConnectionState.execute(this, id);
    }

    /**
     * forwards the number of players of the game to the Connection FSM
     * @param numPlayers the number of players of the game
     */
    @Override
    public synchronized void updateNumPlayers(int numPlayers){
        if(currentConnectionState.equals(ConnectionState.RECEIVE_NUM_PLAYERS))
            currentConnectionState.execute(this, numPlayers);
    }

    /**
     * forwards to the Connection FSM the information that all the expected players are connected
     */
    @Override
    public synchronized void updateAllPlayersConnected(){
        if(currentConnectionState.equals(ConnectionState.RECEIVE_ALL_PLAYERS_CONNECTED))
            currentConnectionState.execute(this, null);
    }

    /**
     * forwards the information regarding the association between
     * an id and a name to the user interface and the Connection FSM
     * @param id the id of a player
     * @param name the name corresponding to that player
     */
    @Override
    public synchronized void updateName(int id, String name){
        getUi().registerPlayer(id, name);
        if(getUi().getNumPlayersRegister() == getUi().getNumPlayers() && currentConnectionState == ConnectionState.WAIT_ALL_PLAYERS_NAME){
            currentConnectionState = ConnectionState.END;
            currentConnectionState.execute(this, null);
        }
    }

    //updates relating to the Restore Phase

    /**
     * checks whether the Restore fsm is in the right state to receive this update, and if so executes it
     * @param available a boolean representing whether a saved game is available
     */
    @Override
    public synchronized void updateGameAvailable(boolean available){
        if(currentRestoreState == RestoreState.RECEIVE_GAME_AVAILABLE){
            currentRestoreState.execute(available);
        }
    }

    /**
     * checks whether the Restore fsm is in the right state to receive this update, and if so executes it
     * @param intentToRestore a boolean representing whether a saved game will be restored or not
     */
    @Override
    public synchronized void updateRestore(boolean intentToRestore){
        if(currentRestoreState == RestoreState.RECEIVE_RESTORE){
            currentRestoreState.execute(intentToRestore);
        }
    }

    /**
     * remaps the id of the client
     * @param idMap a map containing info regarding the new id
     */
    @Override
    public synchronized void updateRemap(Map<Integer, Integer> idMap){
        this.id = idMap.get(id);
    }

    /**
     * Resumes the game after a restore
     */
    @Override
    public synchronized void updateResume(){
        if(currentRestoreState.equals(RestoreState.RECEIVE_ALL)){
            currentRestoreState.execute(null);
        }

    }

    //Setup Phase Updates

    /**
     * forwards the deck to the Setup FSM, if it was expecting it
     * @param deck the deck coming from the server
     */
    @Override
    public synchronized void updateDeck(Deck deck) {

        getUi().setCurrentPlayer(0);
        if(currentSetupState == SetupState.RECEIVE_DECK){
            currentSetupState.execute(this, deck);
        }
    }

    /**
     * Inform the user interface that another player has disconnected from the server.
     */
    @Override
    public synchronized void updateDisconnection()
    {
        //TODO inform the client whether the game has been saved or not
        net.closeConnection();
        getUi().showDisconnection("Sorry, another player has disconnected from the game!");
    }

    /**
     * forwards to the Setup FSM the list of cards from which the client can choose theirs
     * @param id the id of the player for whom this list is valid
     * @param cards the cards from which the player can choose from
     */
    @Override
    public synchronized void updateCards(int id, List<Card> cards) {
        getUi().setCurrentPlayer(id);
        if(id == getId() && currentSetupState == SetupState.RECEIVE_CARDS){
            currentSetupState.execute(this, cards);
        }
    }

    /**
     * registers the connection between an id and a card
     * @param id the id of the player
     * @param card the card the player chose
     */
    @Override
    public synchronized void updateGod(int id, Card card){
        getUi().registerGod(id, card);
    }

    // updates relative to the Game Phase

    /**
     * forwards the information regarding another player's end of turn
     * to the Game FSM. If this results in it being this client's turn,
     * this client starts to serve it
     * @param id the id of the current player
     */
    @Override
    public void updateEndOfTurnPlayer(int id){
        if(id == getId() && currentGameState!=null && currentGameState.equals(GameState.RECEIVE_ACTIONS)){
            //torno in attesa del mio turno
            currentGameState = GameState.REQUEST_ACTIONS;
            currentGameState.execute(this, null);
        }
    }

    /**
     * executes the action on the user interface
     * @param id the id of the player that performed the action
     * @param action the action taken
     */
    @Override
    public synchronized void updateAction(int id, Action action){
        getUi().executeAction(action);
    }

    /**
     * announces a win or a loss depending on whether the winning id
     * matches the client id
     * also puts the Game FSM in the proper state
     * @param id the id of the winning client
     */
    @Override
    public synchronized void updatePlayerWin(int id){
        if(id == getId()){
            getUi().winAnnounce(getId());
            currentGameState = GameState.WIN_STATE;
        }else{
            if(currentGameState != GameState.LOSE_STATE){
                getUi().loseAnnounce(getId());
            }
            currentGameState = GameState.LOSE_STATE;
        }
        currentGameState.execute(this, null);
    }

    /**
     * notifies the loss if the losing player's id matches the client's id
     * puts the losing client in the correct state,
     * and removes the losing client's workers from the ui
     * @param id the id of the losing player
     */
    @Override
    public synchronized void updatePlayerLose(int id){
        if(id == getId()){
            getUi().loseAnnounce(id);
            currentGameState = GameState.LOSE_STATE;
            currentGameState.execute(this, null);
        }
        getUi().removeWorkersOfPlayer(id);
    }

    //methods used to start the client and its FSMs

    /**
     * primes the Connection FSM for execution
     * It will actually be started by an update
     */
    private void startConnectionFSM(){
        currentConnectionState = ConnectionState.READY;
    }

    /**
     * starts the game restore FSM
     */
    public void startRestoreFSM() {
        currentRestoreState = RestoreState.START_RESTORE;
        RestoreState.view = this;
        currentRestoreState.execute(null);
    }

    /**
     * starts the Setup FSM
     */
    public void startSetupFSM() {
        currentSetupState = SetupState.START_SETUP;
        currentSetupState.execute(this, null);
    }

    /**
     * starts the Game FSM
     */
    public void startGameFSM() {
        currentGameState = GameState.START_GAME;
        currentGameState.execute(this, null);
    }

    /**
     * starts the ui and the Connection FSM
     */
    public void start(){
        startConnectionFSM();
    }


}

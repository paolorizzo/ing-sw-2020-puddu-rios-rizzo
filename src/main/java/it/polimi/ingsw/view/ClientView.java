package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.exception.IncorrectStateException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observation.UserInterfaceObserver;
import it.polimi.ingsw.view.middleware.NetworkInterface;

import java.util.List;
import java.util.Map;

//TODO test basically everything in this class
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


    public ClientView(ControllerInterface controller, NetworkInterface net){
        super(controller);
        this.net = net;
        id = -1;
    }

    public ClientView(ControllerInterface controller){
        super(controller);
        id = -1;
    }

    public void connectionLost()
    {

        System.out.println("The server is not reachable");
    }

    public void setUi(UserInterface ui)
    {

        this.ui = ui;
    }

    public UserInterface getUi()
    {

        return ui;
    }

    public int getId()
    {

        return id;
    }

    public void getIp()
    {

        getUi().askIp();
    }

    public void getPort()
    {

        getUi().askPort();
    }

    public void setNumPlayers(int numPlayers)
    {

        getUi().setNumPlayers(numPlayers);
    }

    //TODO should not be accepted if ID is already set, meaning it is still -1
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
        net.setIp(ip);
    }

    /**
     * sets the port that the user chose
     * @param port the chosen port
     */
    @Override
    public synchronized void updateReadPort(int port)
    {
        net.setPort(port);
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
        //System.out.println("currplayer "+id);
        //System.out.println("my id "+this.id);
        if(id == getId() && currentSetupState!=null && currentSetupState.equals(SetupState.ASK_SETUP_WORKER))
            currentSetupState.execute(this, possibleActions);
        if(id == getId() && currentGameState!=null && currentGameState.equals(GameState.RECEIVE_ACTIONS)){
            //System.out.println("Entering actions receival");
            if(canEndOfTurn){
                currentGameState = GameState.ASK_OPTIONAL_ACTION;
                //System.out.println("Posso terminare il turno, invece di fare la mossa");
            }
            currentGameState.execute(this, possibleActions);
        }
        //System.out.println("GameState: " + currentGameState);

    }

    //connection phase updates

    @Override
    public synchronized void updateNumPlayers(int numPlayers){
        //System.out.println("received number of players: " + numPlayers);
        if(currentConnectionState.equals(ConnectionState.RECEIVE_NUM_PLAYERS))
            currentConnectionState.execute(this, numPlayers);
    }

    @Override
    public void updateEndOfTurnPlayer(int id){
        if(id == getId() && currentGameState!=null && currentGameState.equals(GameState.RECEIVE_ACTIONS)){
            //torno in attesa del mio turno
            currentGameState = GameState.REQUEST_ACTIONS;
            currentGameState.execute(this, null);
        }
    }

    @Override
    public synchronized void updateAction(int id, Action action){
        getUi().executeAction(action);
        //System.out.println("Execute action: "+action.toString());
    }

    //updates relative to PlayersObserver

    @Override
    public synchronized void updateID(int id){

        /*
        if(this.id != -1)
            System.out.println("ClientView.updateID with id "+id+", but was already "+this.id);
        else
            System.out.println("ClientView.updateID with new id "+id);

         */
        if(currentConnectionState.equals(ConnectionState.RECEIVE_ID))
            currentConnectionState.execute(this, id);
    }

    @Override
    public synchronized void updateAllPlayersConnected(){
        if(currentConnectionState.equals(ConnectionState.RECEIVE_ALL_PLAYERS_CONNECTED))
            currentConnectionState.execute(this, null);
    }

    @Override
    public synchronized void updateStart(){
        //System.out.println("starting this client");
        if(currentConnectionState.equals(ConnectionState.READY))
            currentConnectionState.execute(this, null);
    }

    //this method supposes that only valid names are received
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
        //System.out.println("Remapping id from " + id + " to " + idMap.get(id));
        this.id = idMap.get(id);
    }

    /**
     * Resumes the game after a restore
     */
    @Override
    public synchronized void updateResume(){
        //System.out.println("Trying to resume the game");
        if(currentRestoreState.equals(RestoreState.RECEIVE_ALL)){
            currentRestoreState.execute(null);
        }
        else{
            System.out.println(currentRestoreState.name());
        }

    }

    //Setup Phase Updates
    @Override
    public synchronized void updateDeck(Deck deck) {

        getUi().setCurrentPlayer(0);
        if(currentSetupState == SetupState.RECEIVE_DECK){
            currentSetupState.execute(this, deck);
        }
    }


    @Override
    public synchronized void updateCards(int id, List<Card> cards) {
        getUi().setCurrentPlayer(id);
        if(id == getId() && currentSetupState == SetupState.RECEIVE_CARDS){
            currentSetupState.execute(this, cards);
        }
    }

    @Override
    public synchronized void updateGod(int id, Card card){
        getUi().registerGod(id, card);
    }

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
    @Override
    public synchronized void updatePlayerLose(int id){
        if(id == getId()){
            getUi().loseAnnounce(id);
            currentGameState = GameState.LOSE_STATE;
            currentGameState.execute(this, null);
        }
        getUi().removeWorkersOfPlayer(id);
    }

    //sets up the first state for the connection FSM and does NOT execute it, since it will be awakened by
    //an update
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
    public void startSetupFSM() {
        currentSetupState = SetupState.START_SETUP;
        currentSetupState.execute(this, null);
    }
    public void startGameFSM() {
        currentGameState = GameState.START_GAME;
        currentGameState.execute(this, null);
    }
    //start the first FSM
    public void start(){
        ui.showLogo();
        startConnectionFSM();
    }


}

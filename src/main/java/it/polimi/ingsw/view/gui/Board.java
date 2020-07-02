package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observation.UserInterfaceObservable;
import it.polimi.ingsw.observation.UserInterfaceObserver;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.cli.CliUtils;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

import java.util.HashMap;
import java.util.List;

import it.polimi.ingsw.model.Color;

/**
 * The main class of GUI. It contains the scene 3D for the game graphics and a sub scene for the 2D menus
 */
public class Board extends UserInterfaceObservable implements UserInterface {
    Group groupRoot, group3d;
    Scene scene;

    private Tower [][] towers = new Tower[5][5];
    private HashMap<Integer, Player> players;

    PieceBag pieceBag;

    ClientView clientView;

    ActionFSM actionFSM;

    ErrorVisualizer errorVisualizer;

    AskNumPlayersMenu askNumPlayersMenu;
    AskNameMenu askNameMenu;
    AskIpAndPortMenu askIpAndPortMenu;
    AskRestoreMenu askRestoreMenu;
    PlayersMenu playersMenu;
    AskCardMenu askCardMenu;
    AskGodMenu askGodMenu;
    SelectTypeActionMenu selectTypeActionMenu;
    SelectPieceMenu selectPieceMenu;
    EndOfTurnMenu endOfTurnMenu;
    WinMenu winMenu;
    LoseMenu loseMenu;
    DisconnectionMenu disconnectedMenu;

    private final int WIDTH = 1200;
    private final int HEIGHT = 700;
    private int numPlayers;

    /**
     * Constructor of board
     * It initializes all scenes, menus, pieceBag for the game
     * @param cw the client view
     */
    public Board(ClientView cw) {
        clientView = cw;
        numPlayers = -1;

        this.addObserver(cw);

        players = new HashMap<>();
        pieceBag = new PieceBag();

        groupRoot = new Group();
        group3d = new Group3D();

        scene = new Scene(groupRoot, WIDTH, HEIGHT, true);

        SubScene sub = new SubScene(group3d,WIDTH,HEIGHT,true, SceneAntialiasing.BALANCED);

        groupRoot.getChildren().add(sub);

        Camera camera = new PerspectiveCamera(true);
        camera.translateXProperty().set(0);
        camera.translateYProperty().setValue(0);
        camera.translateZProperty().set(-1300);
        camera.setNearClip(0.1);
        camera.setFarClip(8000);
        sub.setCamera(camera);

        //create fsm
        actionFSM = new ActionFSM();
        prepareBoard();
        initializeMenus();
    }
    /**
     * It prepares and place all initial 3D object of board
     */
    private void prepareBoard() {
        MeshView island = new MeshView(GraphicsLoader.instance().getMesh("ISLAND"));
        MeshView islands = new MeshView(GraphicsLoader.instance().getMesh("ISLANDS"));

        PhongMaterial islandTexture = GraphicsLoader.instance().getTexture("ISLAND");

        island.setMaterial(islandTexture);
        islands.setMaterial(islandTexture);

        MeshView seaUp = new MeshView(GraphicsLoader.instance().getMesh("SEA"));
        MeshView seaDown = new MeshView(GraphicsLoader.instance().getMesh("SEADOWN"));

        PhongMaterial seaUpTexture = GraphicsLoader.instance().getTexture("SEA");
        seaUp.setMaterial(seaUpTexture);
        PhongMaterial seaDownTexture = new PhongMaterial();
        seaDownTexture.setDiffuseColor(new javafx.scene.paint.Color((float)59/255, (float)191/255, (float)241/255, 1));
        seaDown.setMaterial(seaDownTexture);

        MeshView innerWall = new MeshView((GraphicsLoader.instance().getMesh("INNERWALL")));
        MeshView outerWall = new MeshView((GraphicsLoader.instance().getMesh("OUTERWALL")));
        group3d.getChildren().addAll(island, islands, innerWall, seaUp, seaDown, outerWall);

        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                towers[i][j] = new Tower(i, j, new Point3D(i*100-200, 0, j*100-200));
                group3d.getChildren().add(towers[i][j]);
                towers[i][j].setOnMouseClicked(actionFSM);
                towers[i][j].setOnMouseEntered(actionFSM);
                towers[i][j].setOnMouseExited(actionFSM);
            }
        }
    }
    /**
     * It initializes all menus
     */
    public void initializeMenus(){
        errorVisualizer = new ErrorVisualizer(WIDTH, HEIGHT);
        groupRoot.getChildren().add(errorVisualizer);

        askNumPlayersMenu = new AskNumPlayersMenu(WIDTH, HEIGHT);
        askNumPlayersMenu.addObserver(clientView);
        groupRoot.getChildren().add(askNumPlayersMenu.getGroup());

        askIpAndPortMenu = new AskIpAndPortMenu(WIDTH, HEIGHT);
        askIpAndPortMenu.addObserver(clientView);
        groupRoot.getChildren().add(askIpAndPortMenu.getGroup());

        askNameMenu = new AskNameMenu(WIDTH, HEIGHT);
        askNameMenu.addObserver(clientView);
        groupRoot.getChildren().add(askNameMenu.getGroup());

        askRestoreMenu = new AskRestoreMenu(WIDTH, HEIGHT);
        askRestoreMenu.addObserver(clientView);
        groupRoot.getChildren().add(askRestoreMenu.getGroup());

        playersMenu = new PlayersMenu(WIDTH, HEIGHT);
        groupRoot.getChildren().add(playersMenu.getGroup());

        askCardMenu = new AskCardMenu(WIDTH, HEIGHT);
        askCardMenu.addObserver(clientView);
        groupRoot.getChildren().add(askCardMenu.getGroup());

        askGodMenu = new AskGodMenu(WIDTH, HEIGHT);
        askGodMenu.addObserver(clientView);
        groupRoot.getChildren().add(askGodMenu.getGroup());

        selectTypeActionMenu = new SelectTypeActionMenu(WIDTH, HEIGHT, actionFSM);
        groupRoot.getChildren().add(selectTypeActionMenu.getGroup());

        selectPieceMenu = new SelectPieceMenu(WIDTH, HEIGHT, actionFSM, pieceBag);
        groupRoot.getChildren().add(selectPieceMenu.getGroup());

        endOfTurnMenu = new EndOfTurnMenu(WIDTH, HEIGHT, actionFSM);
        groupRoot.getChildren().add(endOfTurnMenu.getGroup());

        winMenu = new WinMenu(WIDTH, HEIGHT);
        groupRoot.getChildren().add(winMenu.getGroup());

        loseMenu = new LoseMenu(WIDTH, HEIGHT);
        groupRoot.getChildren().add(loseMenu.getGroup());

        disconnectedMenu = new DisconnectionMenu(WIDTH, HEIGHT);
        groupRoot.getChildren().add(disconnectedMenu.getGroup());

        //assigning menus
        actionFSM.setMenus(selectTypeActionMenu, selectPieceMenu, endOfTurnMenu);
    }

    /**
     * It returns the main Scene
     * @return
     */
    public Scene getScene(){
        return scene;
    }

    /**
     * reads an action and changes the state of the board
     * based on it
     * @param action the given action taken by some player
     */
    public synchronized void executeAction(Action action){
        if(action instanceof SetupAction)
            this.executeAction((SetupAction)action);
        else if(action instanceof MoveAndForceAction)
            this.executeAction((MoveAndForceAction)action);
        else if(action instanceof MoveAction)
            this.executeAction((MoveAction)action);
        else if(action instanceof BuildAction)
            this.executeAction((BuildAction)action);
        else
            throw new IllegalArgumentException("Can't execute a normal action!");
    }

    /**
     * reads a setup action and changes the state of the board
     * based on it
     * @param action the given setup action taken by some player
     */
    public void executeAction(SetupAction action){
        Color color = players.get(action.getWorkerID().charAt(1)-'0').getColor();
        Worker worker = new Worker(action.getWorkerID(), color.toString());
        //aggiungo worker
        towers[action.getTargetX()][action.getTargetY()].setWorker(worker);
    }
    /**
     * reads a move action and changes the state of the board
     * based on it
     * @param action the given move action taken by some player
     */
    public void executeAction(MoveAction action){
        //salvo worker
        Worker worker = towers[action.getStartX()][action.getStartY()].getWorker();
        //tolgo worker
        towers[action.getStartX()][action.getStartY()].removeWorker();
        //aggiungo worker
        towers[action.getTargetX()][action.getTargetY()].setWorker(worker);
    }
    /**
     * reads a move and force action and changes the state of the board
     * based on it
     * @param action the given move and force action taken by some player
     */
    public void executeAction(MoveAndForceAction action){
        //salvo workers
        Worker worker = towers[action.getStartX()][action.getStartY()].getWorker();
        Worker forcedWorker = towers[action.getForcedStartX()][action.getForcedStartY()].getWorker();
        //tolgo workers
        towers[action.getStartX()][action.getStartY()].removeWorker();
        towers[action.getForcedStartX()][((MoveAndForceAction) action).getForcedStartY()].removeWorker();
        //aggiungo workers
        towers[action.getTargetX()][action.getTargetY()].setWorker(worker);
        towers[action.getForcedTargetX()][action.getForcedTargetY()].setWorker(forcedWorker);
    }
    /**
     * reads a build action and changes the state of the board
     * based on it
     * @param action the given build action taken by some player
     */
    public void executeAction(BuildAction action){
        pieceBag.pickPiece(action.getPiece());
        towers[action.getTargetX()][action.getTargetY()].addBuilding(new Building(action.getPiece()));
    }
    /**
     * reads an action and shows a preview of the next state of the board
     * based on it
     * @param action the given action taken by some player
     */
    public void previewAction(Action action){
        if(action instanceof SetupAction)
            this.previewAction((SetupAction)action);
        else if(action instanceof MoveAndForceAction)
            this.previewAction((MoveAndForceAction)action);
        else if(action instanceof MoveAction)
            this.previewAction((MoveAction)action);
        else if(action instanceof BuildAction)
            this.previewAction((BuildAction)action);
        else
            throw new IllegalArgumentException("Can't execute a normal action!");
    }
    /**
     * reads a setup action and shows a preview of the next state of the board
     * based on it
     * @param action the given setup action that want to be taken by some player
     */
    public void previewAction(SetupAction action){
        executeAction(action);
        towers[action.getTargetX()][action.getTargetY()].getWorker().setToPreview();
    }
    /**
     * reads a move action and shows a preview of the next state of the board
     * based on it
     * @param action the given move action that want to be taken by some player
     */
    public void previewAction(MoveAction action){
        executeAction(action);
        towers[action.getTargetX()][action.getTargetY()].getWorker().setToPreview();
    }
    /**
     * reads a move and force action and shows a preview of the next state of the board
     * based on it
     * @param action the given move and force action that want to be taken by some player
     */
    public void previewAction(MoveAndForceAction action){
        executeAction(action);
        towers[action.getTargetX()][action.getTargetY()].getWorker().setToPreview();
        towers[action.getForcedTargetX()][action.getForcedTargetY()].getWorker().setToDefaultView();
    }
    /**
     * reads a build action and shows a preview of the next state of the board
     * based on it
     * @param action the given build action that want to be taken by some player
     */
    public void previewAction(BuildAction action){
        executeAction(action);
        towers[action.getTargetX()][action.getTargetY()].getLastBuilding().setToPreview();
    }

    /**
     * reads an action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given action
     */
    public void undoExecuteAction(Action action){
        if(action instanceof SetupAction)
            this.undoExecuteAction((SetupAction)action);
        else if(action instanceof MoveAndForceAction)
            this.undoExecuteAction((MoveAndForceAction)action);
        else if(action instanceof MoveAction)
            this.undoExecuteAction((MoveAction)action);
        else if(action instanceof BuildAction)
            this.undoExecuteAction((BuildAction)action);
        else
            throw new IllegalArgumentException("Can't undo a normal action!");
    }
    /**
     * reads a setup action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given setup action
     */
    public void undoExecuteAction(SetupAction action){
        towers[action.getTargetX()][action.getTargetY()].removeWorker();
    }
    /**
     * reads a move action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given move action
     */
    public void undoExecuteAction(MoveAction action){
        //salvo worker
        Worker w = towers[action.getTargetX()][action.getTargetY()].getWorker();
        //tolgo worker
        towers[action.getTargetX()][action.getTargetY()].removeWorker();
        //aggiungo worker
        towers[action.getStartX()][action.getStartY()].setWorker(w);
    }
    /**
     * reads a move and force action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given move and force action
     */
    public void undoExecuteAction(MoveAndForceAction action){
        //salvo workers
        Worker worker = towers[action.getTargetX()][action.getTargetY()].getWorker();
        Worker forcedWorker = towers[action.getForcedTargetX()][action.getForcedTargetY()].getWorker();
        //tolgo workers
        towers[action.getTargetX()][action.getTargetY()].removeWorker();
        towers[action.getForcedTargetX()][action.getForcedTargetY()].removeWorker();
        //aggiungo workers
        towers[action.getStartX()][action.getStartY()].setWorker(worker);
        towers[action.getForcedStartX()][action.getForcedStartY()].setWorker(forcedWorker);
    }
    /**
     * reads a build action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given build action
     */
    public void undoExecuteAction(BuildAction action){
        pieceBag.undoPickPiece(action.getPiece());
        towers[action.getTargetX()][action.getTargetY()].removeLastBuilding();
    }

    /**
     * reads an preview action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given preview action
     */
    public void undoPreviewAction(Action action){
        if(action instanceof SetupAction)
            this.undoPreviewAction((SetupAction)action);
        else if(action instanceof MoveAndForceAction)
            this.undoPreviewAction((MoveAndForceAction)action);
        else if(action instanceof MoveAction)
            this.undoPreviewAction((MoveAction)action);
        else if(action instanceof BuildAction)
            this.undoPreviewAction((BuildAction)action);
        else
            throw new IllegalArgumentException("Can't undo a normal action!");
    }
    /**
     * reads a preview setup action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given preview setup action
     */
    public void undoPreviewAction(SetupAction action){
        undoExecuteAction(action);
    }
    /**
     * reads a preview move action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given preview move action
     */
    public void undoPreviewAction(MoveAction action){
        undoExecuteAction(action);
        towers[action.getStartX()][action.getStartY()].setToDefaultView();
    }
    /**
     * reads a preview move and force action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given preview move and force action
     */
    public void undoPreviewAction(MoveAndForceAction action){
        undoExecuteAction(action);
        towers[action.getStartX()][action.getStartY()].setToDefaultView();
        towers[action.getForcedStartX()][action.getForcedStartY()].setToEnableView();
    }
    /**
     * reads a preview build action and calculates the inverse of the effect it would
     * have on the board
     * @param action the given preview build action
     */
    public void undoPreviewAction(BuildAction action) {
        undoExecuteAction(action);
    }

    /**
     * Getter of a tower x, y
     * @param targetX the row
     * @param targetY the col
     * @return the tower on row x and on col y
     */
    public Tower getTower(int targetX, int targetY) {
        return towers[targetX][targetY];
    }

    /**
     * It sets all towers to default view
     */
    public void setAllToDefaultView(){
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                towers[i][j].setToDefaultView();
            }
        }
    }


    /**
     * Asks for the IP address and port number of the desired game server showing the menu.
     */
    @Override
    public void askIpAndPort() {
        askIpAndPortMenu.show();
    }
    /**
     * Informs the user interface about the currently active player.
     * @param id the player's id.
     */
    @Override
    public void setCurrentPlayer(int id){
        playersMenu.setCurrentPlayer(id);
    }
    /**
     * Informs the user about an occurred disconnection through a error visualizer menu.
     * @param e a String containing details about the cause of the disconnection.
     */
    @Override
    public void showError(String e){
        errorVisualizer.showError(e);
    }
    /**
     * Asks for the desired number of players showing the menu.
     */
    @Override
    public void askNumPlayers() {
        askNumPlayersMenu.show();
        System.out.println("Ask num player");
    }
    /**
     * Asks for the desired username showing the menu.
     */
    @Override
    public void askUsername() {
        askNameMenu.show();
        System.out.println("Ask name");
    }
    /**
     * Asks if the user wants to restore the stored game showing the menu.
     */
    @Override
    public void askRestore(){
        askRestoreMenu.show();
    }
    /**
     * Asks the user to select a card from the deck, setting the deck in the menu and showing it.
     * @param deck the collection of cards.
     */
    @Override
    public void askCard(Deck deck) {
        askCardMenu.setDeck(deck);
        askCardMenu.show();

    }
    /**
     * Asks the user to select a god for the game showing the menu.
     * @param cards the set of available cards.
     */
    @Override
    public void askGod(List<Card> cards) {
        askGodMenu.setCards(cards);
        askGodMenu.show();

    }
    /**
     * Asks the user to set up a worker on the board initializing the action FSM
     * @param possibleActions the possible setup choices list.
     */
    @Override
    public void askSetupWorker(List<Action> possibleActions) {
        actionFSM.setPossibleActions(this, possibleActions, false);
    }
    /**
     * Asks the user to perform an action initializing the action FSM
     * @param possibleActions the possible actions list.
     * @param canEndOfTurn a boolean flag indicating the possibility to end the current turn within the current action.
     */
    @Override
    public void askAction(List<Action> possibleActions, boolean canEndOfTurn){
        actionFSM.setPossibleActions(this, possibleActions, canEndOfTurn);
    }
    /**
     * Removes all the active workers of a player from the board.
     * @param id the player's id.
     */
    @Override
    public void removeWorkersOfPlayer(int id){
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(towers[i][j].hasWorker() && (towers[i][j].getWorker().getWorkerId().charAt(1)-'0' == id)){
                    towers[i][j].removeWorker();
                }
            }
        }
    }
    /**
     * Displays the win menu.
     * @param id the winner's id.
     */
    @Override
    public void winAnnounce(int id){
        winMenu.setGodView(players.get(id).getCard());
        winMenu.show();
    }
    /**
     * Displays the lose menu.
     * @param id the loser's id.
     */
    @Override
    public void loseAnnounce(int id){
        loseMenu.setGodView(players.get(id).getCard());
        loseMenu.show();
    }
    /**
     * Sets the number of players in the player's menu.
     * @param numPlayers the number of players.
     */
    @Override
    public void setNumPlayers(int numPlayers) {
        if(this.numPlayers == -1)
        playersMenu.setNumPlayers(numPlayers);
        playersMenu.show();
        this.numPlayers = numPlayers;
    }

    /**
     * Informs the user about an occurred disconnection.
     * @param message a String containing details about the cause of the disconnection.
     */
    @Override
    public void showDisconnection(String message)
    {
        disconnectedMenu.show(message);
    }

    /**
     * Retrieves the current number of active players in the game.
     * @return the number of active players.
     */
    @Override
    public int getNumPlayers() {
        return numPlayers;
    }
    /**
     * Adds a new player to the game and in the player's menu
     * @param id the players's id.
     * @param name the player's username.
     */
    @Override
    public void registerPlayer(int id, String name) {
        players.put(id, new Player(id, name));
        playersMenu.addNamePlayer(players.get(id));
    }
    /**
     * Registers the card chosen by player.
     * @param id the player's id.
     * @param card the chosen power card.
     */
    @Override
    public void registerGod(int id, Card card){
        players.get(id).setCard(card);
        playersMenu.addGodPlayer(players.get(id));
    }

    /**
     * Retrieves the current number of active players in the game.
     * @return the number of active players.
     */
    @Override
    public int getNumPlayersRegister() {
        return players.size();
    }

}

package it.polimi.ingsw.model;

import it.polimi.ingsw.observation.*;

import java.io.File;
import java.util.*;

/**
 * Master class of the model package, exposes most methods
 * and is the head of the reference structure of the model package
 */
public class Model {
    public static Model instance;

    public Game game;
    HashMap<Integer, Player> players;
    //feeds
    public FeedObservable feed;
    public Board board;

    /**
     * constructs an empty model
     */
    public Model(){
        board = new Board();
        players = new HashMap<>();
        feed = new FeedObservable();
        game = new Game(this);
    }

    //general methods

    /**
     * returns the feed used for communication towards the views
     * @return the feed
     */
    FeedObservable getFeed(){
        return feed;
    }

    /**
     * returns the map of ids to players
     * @return the map of ids to players
     */
    public HashMap<Integer, Player> getPlayers(){
        return players;
        /*
        HashMap<Integer, Player> copy = new HashMap<Integer, Player>();
        for(HashMap.Entry<Integer, Player> entry:players.entrySet()){
            copy.put(entry.getKey(), entry.getValue().lightClone());
        }
        return copy;

         */
    }

    /**
     * returns the player for a given id
     * @param id the given id
     * @return the player corresponding to the given id
     */
    public Player getPlayer(int id){
        /*
        return players.get(id).lightClone();

        */
        return players.get(id);
    }

    /**
     * sets the number of players on the game, and notifies the views of it
     * @param numPlayers the chosen number of players
     */
    public void setNumPlayers(int numPlayers){
        game.setNumPlayers(numPlayers);
        feed.notifyNumPlayers(numPlayers);
    }

    /**
     * relies on the controller to only add valid players
     * adds the player in its right place in the ArrayList
     * publishes the notify for all known names so that the last client can know every other client
     * that has already posted their name, possibly even before the last one joined
     * @param player
     */
    public void addPlayer(Player player){
        int id = player.getId();
        players.put(id, player);
        board.createPlayerWorkers(player);
        //important to notify every name to allow late clients to know all other players before
        feed.notifyName(player.getId(), player.getNickname());
    }

    /**
     * returns true if someone already claimed the nickname
     * @param name the name that is being checked
     * @return true if someone already claimed the nickname
     */
    public boolean nicknamePresent(String name){
        boolean alreadyPresent = false;
        for(Player p: players.values()) {
            if (p.getNickname().equals(name))
                alreadyPresent = true;
        }
        return alreadyPresent;
    }

    //returns true if there is already a player with that id
    //if the get fails because it is impossible to get a player with that id
    //it catches the relative exception and returns false because
    //the fact that it is impossible to get it means that it is not present

    /**
     * returns true if there is already a player with that id
     * @param id the id that is the object of the query
     * @return true if there is already a player with that id
     */
    public boolean playerPresent(int id){
        return players.get(id) != null;
    }

    /**
     * saves the association between the id and the card
     * ADVANCES THE TURN
     * notifies the assiociation through the feed
     * @param id
     * @param numCard
     */
    public void setCardPlayer(int id, int numCard){
        Card card = game.getChosenCard(numCard);
        players.get(id).setCard(card);
        game.removeChosenCard(card);
        game.nextTurn();
        feed.notifyGod(id, card);
    }

    /**
     * returns the number of players
     * @return the number of players
     */
    public int getNumPlayers(){
        return game.getNumPlayers();
    }

    /**
     * returns true if the number of players has been set
     * @return true if the number of players has been set
     */
    public boolean numPlayersIsSet(){
        return game.numPlayersIsSet();
    }

    /**
     * add an observer to the feed
     * @param obs the observer to be added
     */
    public void addObserver(FeedObserver obs){
        feed.addObserver(obs);
        feed.notifyStart();
    }

    public void removeObserver(FeedObserver obs){
        feed.removeObserver(obs);
    }

    /**
     * MAY ADVANCE THE TURN
     * updates the board with the setup action, adds it to the game, and if the setup turn is complete advances the turn
     * also notifies the action through the feed
     * @param id
     * @param setupAction
     */
    public void executeSetupAction(int id, SetupAction setupAction){
        board.executeAction(setupAction);
        game.addSetupAction(setupAction);
        if(players.get(id).getWorker(Sex.FEMALE).getSpace() != null && players.get(id).getWorker(Sex.MALE).getSpace() != null){
            game.nextTurn();
        }

        feed.notifyAction(id, setupAction);
    }

    /**
     * returns true if a worker for the given id and sex has been placed
     * @param id of the player that owns the worker
     * @param s sex of the worker
     * @return true if worker of player id of sex s has been placed
     */
    public boolean workerIsPlaced(int id, Sex s){
        return getPlayers().get(id).getWorker(Sex.FEMALE).getSpace() != null;
    }

    /**
     * returns position of the requested worker on the given axis
     * @param id of the player that owns the worker
     * @param s sex of the worker
     * @param axis on which to get the position
     * @return position on the requested worker on the given axis
     */
    public int getWorkerPos(int id, Sex s, char axis){
        if(!workerIsPlaced(id, s))
            throw new IllegalArgumentException("the worker has not been placed yet");
        if(!(axis == 'x' || axis =='y'))
            throw new IllegalArgumentException("worker has no position on axis " + axis);
        if(axis == 'x')
            return players.get(id).getWorker(s).getSpace().getPosX();
        return players.get(id).getWorker(s).getSpace().getPosY();
    }

    /**
     * executes the action on the board and saves it in the current turn
     * @param id
     * @param action
     */
    public void executeAction(int id, Action action) {
        board.executeAction(action);
        game.addAction(action);
        feed.notifyAction(id, action);
    }

    /**
     * instantiates a PersistenceGame, and saves both that and the TurnArchive to a json file
     * the files that are saved have a name that is non ambiguous for any given set of players
     */
    public void save(){
        PersistenceGame pg = new PersistenceGame(game);
        TurnArchive ta = game.turnArchive;
        String saveName = saveName();
        System.out.println("Saving game persistence files with name suffix:");
        System.out.println("\t" + saveName);
        pg.save(saveName);
        ta.save(saveName);
    }

    /**
     * retrieves the names of the players, and concatenates them to act a suffix for the file names
     * @return a concatenation of the names of the players, sorted alphabetically
     */
    String saveName(){
        List<String> names = new ArrayList<>();
        int numPlayers = game.getNumPlayers();
        for(int i=0;i<numPlayers;i++){
            names.add(players.get(i).getNickname());
        }
        Collections.sort(names);
        String saveName = "";
        for(String name:names){
            saveName += "_" + name;
        }
        return saveName;
    }

    /**
     * checks if there exist save files for the current players
     * @return true if there exist save files for the current players
     */
    public boolean isSaved(){
        String persistencePath = "./persistence";
        String names = saveName();
        String gamePath = PersistenceGame.savePath(names);
        String turnsPath = TurnArchive.savePath(names);
        File persistenceDirectory = new File(persistencePath);
        if (!persistenceDirectory.exists())
            persistenceDirectory.mkdir();
        File gameFile = new File(gamePath);
        File turnsFile = new File(turnsPath);
        return (gameFile.exists() && turnsFile.exists());
    }

    /**
     * loads the model from file
     */
    public void load(){
        PersistenceGame pg = PersistenceGame.load(saveName());
        TurnArchive ta = TurnArchive.load(saveName());
        restoreConnectionPhase(pg);
        restoreSetupPhase(pg);
        restoreGamePhase(ta);
        game.turnArchive = ta;
        feed.notifyResume();
        System.out.println("Restore phase complete");
    }

    /**
     * deletes persistence files for this game
     */
    public void deleteFiles(){
        String names = saveName();
        TurnArchive.delete(names);
        PersistenceGame.delete(names);
    }

    /**
     * restores all the things that have happened in the connection phase of the saved game
     * @param pg the PersistenceGame from which to restore
     */
    void restoreConnectionPhase(PersistenceGame pg){
        System.out.println("Restoring connection phase");
        remapIds(pg);
        game.setNumPlayers(pg.numPlayers);
        int numPlayers = game.getNumPlayers();
        players.clear();
        for(int i=0;i<numPlayers;i++){
            addPlayer(new Player(pg.ids[i], pg.names[i]));
        }
    }

    /**
     * Communicates their old ids to the clients
     * @param pg the PersistenceGame from which to get the info
     */
    void remapIds(PersistenceGame pg){
        Map<Integer, Integer> idMap = new HashMap<>();
        for(int i=0;i<game.getNumPlayers();i++){
            Player currP = players.get(i);
            String name = currP.getNickname();
            int currId = currP.getId();
            int oldId = -1;
            for(int j=0;j<pg.numPlayers;j++){
                if(pg.names[j].equals(name))
                    oldId = pg.ids[j];
            }
            if(oldId == -1){
                throw new IllegalStateException("A game is being restored but the saved game contains no info regarding player " + name);
            }
            idMap.put(currId, oldId);
        }
        feed.notifyRemap(idMap);
    }

    /**
     * restores the game up to the choice of gods
     * @param pg the PersistenceGame from whihch to perform the restoration
     */
    void restoreSetupPhase(PersistenceGame pg){
        System.out.println("Restoring setup phase");
        int numPlayers = game.getNumPlayers();
        List<Integer> chosenCards = new ArrayList<>();
        for(int i=0;i<numPlayers;i++){
            chosenCards.add(pg.numGods[i]);
        }
        game.setChosenCards(chosenCards);
        for(int i=0;i<numPlayers;i++){
            setCardPlayer(pg.ids[i], pg.numGods[i]);
        }
    }

    /**
     * restores the game phase from a previous game
     * @param ta the TurnArchive from which to get the turns
     */
    void restoreGamePhase(TurnArchive ta){
        System.out.println("Restoring game phase");
        for(Turn t:ta.turns){
            restoreTurn(t);
            //safeWait();
        }

    }

    /**
     * restores a single turn
     * @param t the turn to restore
     */
    void restoreTurn(Turn t){
        for(Action a:t.actions){
            System.out.println("\tRestoring " + a.toString());
            if (a instanceof SetupAction){
                game.getPossibleSetupActions(t.playerId);
                executeSetupAction(t.playerId, (SetupAction) a);
            }
            else{
                game.getPossibleActions(t.playerId);
                executeAction(t.playerId, a);
            }
            safeWait();
        }
        if(! (t.actions.get(0) instanceof SetupAction)){
            game.getPossibleActions(t.playerId);
            if(game.getCurrentPlayerId() == t.playerId){
                game.nextTurn();
            }
        }
    }

    /**
     * returns true if all the workers have been placed
     * @return true if all the workers have been placed
     */
    public boolean allWorkersPlaced(){
        boolean placed = true;
        try{
            for(int i=0;i<game.getNumPlayers();i++){
                Player p = players.get(i);
                Worker f =p.getWorker(Sex.FEMALE);
                Worker m =p.getWorker(Sex.MALE);
                placed &= (f.getSpace() != null && m.getSpace() != null);
            }
        }
        catch(NullPointerException npe){
            placed = false;
        }
        return placed;
    }

    /**
     * deep comparison that sets off comparisons of all the objects that should be restored
     * after loading a previous game
     * @param that the other Model
     * @return true if the two models are functionally indistinguishable
     */
    public boolean fullEquals(Model that){
        boolean equality = true;
        equality &= this.game.fullEquals(that.game);
        equality &= this.players.keySet().equals(that.players.keySet());
        if(!equality)
            return equality;
        for(int key:players.keySet())
            equality &= this.players.get(key).fullEquals(that.players.get(key));
        equality &= this.feed != null && that.feed != null;
        equality &= this.board.fullEquals(that.board);
        return equality;
    }

    /**
     * waits for a predetermined amount of time
     */
    private void safeWait(){
        try{
            Thread.sleep(400);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * stores memory of all the actions taken by the players during the game
 */
public class TurnArchive extends MapConvertible{
    List<Turn> turns;

    /**
     * constructor, initializes the turn list
     */
    public TurnArchive(){
        turns = new ArrayList<>();
    }

    /**
     * adds a turn to the turn list
     * @param turn the turn that is added to the list
     */
    public void addTurn(Turn turn){
        turns.add(turn);
    }

    /**
     * returns the last turn recorded by the archive of a given player
     * @param player the player for which to retrieve the turn
     * @return the last turn of the given player
     */
    public Turn getLastTurnOf(Player player){
        for(int i=turns.size()-1;i>=0;i--){
            if(turns.get(i).getPlayerId() == player.getId())
                return turns.get(i);
        }
        //not found
        return null;
    }

    /**
     * saves the turn archive to a json file
     * @return true if the operations raise no exceptions
     */
    boolean save(String names){
        Gson gson = new Gson();
        String pathToTurnsJson = savePath(names);
        try{
            FileWriter writer = new FileWriter(pathToTurnsJson);
            Map<String, Object> turnArchiveMap = this.toMap();
            //System.out.println(turnArchiveMap.toString());
            gson.toJson(turnArchiveMap, writer);
            writer.flush();
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * constructs a valid TurnArchive object from a json file that stores its contents
     * @return a TurnArchive object loaded from storage
     */
    static TurnArchive load(String names){
        Gson gson = new Gson();
        String pathToTurnsJson = savePath(names);
        try{
            JsonReader reader = new JsonReader(new FileReader(pathToTurnsJson));
            Type mapType = new TypeToken<HashMap<String, Object>>(){}.getType();
            Map<String, Object> turnArchiveMap =gson.fromJson(reader, mapType);
            TurnArchive instance = TurnArchive.fromMap(turnArchiveMap);
            return instance;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * deletes the persistence file for the given set of names
     * @param names the set of names
     */
    static void delete(String names){
        String path = savePath(names);
        File file = new File(path);
        if(file.exists())
            file.delete();
    }

    /**
     * converts the turn archive to a map
     * @return a map representing the turn archive
     */
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        putEntries(map);
        return map;
    }

    /**
     * puts into the map all the info regarding the action
     * @param map the map in which to put the info
     */
    public void putEntries(Map<String, Object> map){
        super.putEntries(map);;
        int size = this.turns.size();
        map.put("size", size);
        for(int i=0;i<size;i++){
            map.put(String.valueOf(i), this.turns.get(i).toMap());
        }
    }

    /**
     * converts a map into a valid TurnArchive object
     * @param map the map to convert
     * @return a TurnArchive object as described in the map
     */
    static public TurnArchive fromMap(Map<String, Object> map){
        checkTypeCorrectness(map, TurnArchive.class);
        TurnArchive turnArchive = new TurnArchive();
        int size = getInt(map, "size");
        for(int i=0;i<size;i++){
            Map<String, Object> turnMap = (Map<String, Object>) map.get(String.valueOf(i));
            Turn turn = Turn.fromMap(turnMap);
            turnArchive.addTurn(turn);
        }
        return turnArchive;
    }

    /**
     * builds a string that represents the turn archive by concatenating the strings representing the turns
     * @return a string that represents the turn archive
     */
    @Override
    public String toString(){
        String s = "";
        for(Turn t:turns)
            s += t.toString();
        return s;
    }

    /**
     * compares this object against any other
     * @param o the other object
     * @return true if o is a TurnArchive, and their toString match
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof TurnArchive)) return false;
        TurnArchive that = (TurnArchive) o;
        return this.toString().equals(that.toString());
    }

    /**
     * returns the save path of a turnArchive json file, given the concatenated names of the players
     * @param names the concatenated names of the players of a game
     * @return the path to a json file that serializes the turns for a game with those players
     */
    static String savePath(String names){
        //return "src/main/resources/persistence/turns" + names + ".json";
        return "./persistence/turns" + names + ".json";
    }

}

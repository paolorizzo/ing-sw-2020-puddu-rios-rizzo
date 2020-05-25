package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * stores memory of all the actions taken by the players during the game
 */
public class TurnArchive {
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
    boolean save(){
        Gson gson = new Gson();
        String pathToTurnsJson = "src/main/resources/persistence/turns.json";
        try{
            FileWriter writer = new FileWriter(pathToTurnsJson);
            gson.toJson(this, writer);
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
    static TurnArchive load(){
        Gson gson = new Gson();
        String pathToTurnsJson = "src/main/resources/persistence/turns.json";
        try{
            JsonReader reader = new JsonReader(new FileReader(pathToTurnsJson));
            TurnArchive instance = gson.fromJson(reader, TurnArchive.class);
            return instance;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
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
}

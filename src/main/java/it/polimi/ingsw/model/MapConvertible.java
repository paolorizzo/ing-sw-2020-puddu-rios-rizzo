package it.polimi.ingsw.model;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * groups similar snippets of code from all the class that support map conversion
 */
public class MapConvertible {

    /**
     * puts the dynamic type of the object in the map, as a string
     * @param map the map in which to put the class
     */
    public void putClass(Map<String, Object> map){
        map.put("class", this.getClass().getName());
    }

    /**
     * retrieves the dynamic type of the object that was converted into this map
     * @param map the map from which to retrieve the type
     * @return the class of the object that was converted into the given map
     */
    public static Class<?> getClass(Map<String, Object> map){
        String mapClassName = (String) map.get("class");
        Class<?> mapClass = null;
        try {
            mapClass = Class.forName(mapClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mapClass;
    }

    /**
     * checks that the class information inside the map matches the class of the calling object
     * @param map the map from which to get the dynamic typing info
     */
    public static void checkTypeCorrectness(Map<String, Object> map, Class<?> actualClass){
        Class<?> mapClass = getClass(map);
        if(!actualClass.isAssignableFrom(mapClass))
            throw new IllegalArgumentException("Tried to build an object of type " + actualClass.toString() + " from a map of type " + mapClass.toString());
    }

    /**
     * handles the possibility that an int value is actually saved as a double in the map
     * this happens when the map is converted to json through the gson library
     * @param map the map from which to retrieve the int value
     * @param key the key of the int value in the map
     * @return the value as an int
     */
    public static int getInt(Map<String, Object> map, String key){
        try{
            return (int) map.get(key);
        }
        catch(ClassCastException e) {
            double val = (double) map.get(key);
            return (int) val;
        }
    }

    /**
     * converts an object to a map, by writing its fields and its class
     * @return a map representing the object
     */
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        putEntries(map);
        return map;
    }

    /**
     * fills the map with the relevant entries
     * will need to be overridden in the inheriting classes
     * in this root version, simply puts the class in the map
     * @param map
     */
    public void putEntries(Map<String, Object> map){
        putClass(map);
    }

}

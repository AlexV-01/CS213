package photos;

import java.io.*;
import java.util.*;

/**
 * A class used to serialize existing data.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class InfoLoader implements Serializable {
    /**
     * An ArrayList of all usernames.
     */
    private ArrayList<String> userNames = new ArrayList<String>();
    
    /**
     * An ArrayList of all users.
     */
    private ArrayList<User> users = new ArrayList<User>();

    /**
     * An ArrayList of ArrayLists of photos. Each nested ArrayList represents a different user.
     */
    private ArrayList<ArrayList<Photo>> photos = new ArrayList<ArrayList<Photo>>();

    /**
     * An ArrayList of ArrayLists of the number of instances of each photo currently in use for a user.
     * Each nested ArrayList represents a different user.
     */
    private ArrayList<ArrayList<Integer>> useCount = new ArrayList<ArrayList<Integer>>();

    /**
     * An ArrayList of ArrayLists of directories. Each nested ArrayList represents a different user.
     */
    private ArrayList<ArrayList<String>> directories = new ArrayList<ArrayList<String>>();

    /**
     * Loads data from utility variables.
     */
    public void loadFromUtility() {
        userNames = Utility.userNames;
        users = Utility.users;
        photos = Utility.photos;
        useCount = Utility.useCount;
        directories = Utility.directories;
    }
    
    /**
     * Loads data to utility variables.
     */
    public void loadToUtility() {
        Utility.userNames = userNames;
        Utility.users = users;
        Utility.photos = photos;
        Utility.useCount = useCount;
        Utility.directories = directories;
    }
}
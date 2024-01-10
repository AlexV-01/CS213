package photos;

import java.io.Serializable;
import java.util.*;

import photos.controllers.UserController;

/**
 * Our class for users.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class User implements Serializable {
    private String name;
    private ArrayList<Album> albums = new ArrayList<Album>();

    /**
     * An ArrayList of type <code>Tag</code> containing the tags that belong to this user.
     */
    public ArrayList<Tag> tags = new ArrayList<Tag>();
    
    /**
     * A constructor that takes a username.
     * @param n a string representing the username of this user
     */
    public User(String n) {
        name = n;
    }

    /**
     * Adds an album to this user.
     * @param a the album to be added
     */
    public void addAlbum(Album a) {
        albums.add(a);
    }

    /**
     * Renames an album of this user.
     * @param a the album to be renamed
     * @param n a string representing the new name
     */
    public void renameAlbum(Album a, String n) {
        albums.get(albums.indexOf(a)).setName(n);
    }

    /**
     * Removes an album of this user.
     * @param a the album to be removed
     * @return the <code>Album</code> that was removed
     */
    public Album deleteAlbum(Album a) {
        albums.remove(a);
        return a;
    }

    /**
     * Gets all albums of this user.
     * @return an ArrayList of type <code>Album</code> containing all albums of this user
     */
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    /**
     * Gets the names of all albums of this user.
     * @return an ArrayList of type <code>String</code> containing all album names
     */
    public ArrayList<String> getAlbumNames() {
        ArrayList<String> albumNames = new ArrayList<String>();
        for (Album a : albums) {
            albumNames.add(a.getName() + " | " + a.getPhotos().size() + " photos" + UserController.sortDates(a));
        }
        return albumNames;
    }

    /**
     * Determines whether or not an album of a given name exists.
     * @param n a string representing the name to be checked
     * @return a boolean indicating whether or not the album exists
     */
    public boolean albumExistsByName(String n) {
        for (Album a : albums) {
            if (a.getName().equals(n)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
package photos;

import java.io.Serializable;
import java.util.*;

/**
 * Our class for photo albums.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class Album implements Serializable {
    /**
     * Album name.
     */
    private String name;

    /**
     * An ArrayList of all photos in this album.
     */
    private ArrayList<Photo> photos = new ArrayList<Photo>();

    /**
     * Gets all photos in this album.
     * @return an ArrayList of type <code>Photo</code>
     */
    public ArrayList<Photo> getPhotos() { return photos; }

    /**
     * A constructor that takes the name of a new album.
     * @param n a string indicating album name
     */
    public Album(String n) {
        name = n;
    }

    /**
     * A constructor that takes the name of a new album and a collection of existing photos.
     * @param n a string indicating album name
     * @param photos an ArrayList of type <code>Photo</code>
     */
    public Album(String n, ArrayList<Photo> photos) {
        this(n);
        this.photos = photos;
    }

    /**
     * Adds a photo to this album.
     * @param p the <code>Photo</code> to be added to this album
     */
    public void addPhoto(Photo p) {
        photos.add(p);
    }

    /**
     * Removes a photo from this album.
     * @param p the <code>Photo</code> to be removed from this album
     * @return the removed <code>Photo</code>
     */
    public Photo removePhoto(Photo p) {
        photos.remove(p);
        return p;
    }

    /**
     * Gets all names of photos in this album.
     * @return an ArrayList of type <code>Photo</code>
     */
    public ArrayList<String> getPhotoNames() {
        ArrayList<String> photoNames = new ArrayList<String>();
        for (Photo p : photos) {
            photoNames.add(p.name);
        }
        return photoNames;
    }

    /**
     * Gets the name of this album.
     * @return a string indicating this album name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this album.
     * @param n a string indicating the new album name
     */
    public void setName(String n) {
        name = n;
    }

    @Override
    public String toString() {
        return getName();
    }
}
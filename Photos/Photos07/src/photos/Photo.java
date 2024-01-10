package photos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Our photo class.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class Photo implements Serializable {
    /**
     * A string representing the name of this photo.
     */
    String name;

    /**
     * A string representing the caption of this photo.
     */
    String caption;

    /**
     * An ArrayList of type <Code>Tag</Code> containing all tags for this photo.
     */
    ArrayList<Tag> tags = new ArrayList<Tag>();

    /**
     * An ArrayList of type <Code>Album</Code> containing all albums in which this photo exists.
     */
    public ArrayList<Album> albums = new ArrayList<Album>();
    
    /**
     * A constructor that takes the name of a new photo.
     * @param n a string representing the name of this photo
     */
    public Photo(String n) {
        name = n;
        caption = "";
    }

    /**
     * Gets the name of this photo.
     * @return a string representing the name of this photo
     */
    public String getName() { return name; }

    /**
     * Gets the caption of this photo.
     * @return a string representing the caption of this photo
     */
    public String getCaption() { return caption; }

    /**
     * Sets the caption of this photo.
     * @param s a string representing the new caption of this photo
     */
    public void setCaption(String s) { caption = s; }

    /**
     * Gets all of the tags of this photo.
     * @return an ArrayList of type <code>Tag</code> containing tags
     */
    public ArrayList<Tag> getTags() { return tags; }

    /**
     * Gets all of the tag values of this photo.
     * @return an ArrayList of type <code>TagValue</code> containing tag values
     */
    public ArrayList<TagValue> getTagValues() {
        ArrayList<TagValue> names = new ArrayList<TagValue>();
        for (Tag t : tags) {
            for (TagValue s : t.values) {
                names.add(s);
            }
        }
        return names;
    }

    @Override
    public String toString() { return name; }

    /**
     * Gets the caption in a format for listing.
     * @return a string representing caption
     */
    public String getListCaption() {
        if (caption == "") return "";
        return " | " + caption;
    }
}
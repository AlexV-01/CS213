package photos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class for photo tags.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class Tag implements Serializable {
    private String name;

    /**
     * A boolean value representing whether or not a tag is removable.
     */
    public boolean removable;

    /**
     * A boolean value representing whether or not a tag is singular.
     */
    public boolean singular;

    /**
     * An ArrayList of type <code>TagValue</code> containing values of this tag.
     */
    public ArrayList<TagValue> values = new ArrayList<TagValue>();

    /**
     * A constructor that takes singular and name information.
     * @param singular a boolean representing the singularity of this tag
     * @param name a string representing the name of this tag
     */
    public Tag(boolean singular, String name) {
        this.singular = singular;
        if (name != null) { this.name = String.valueOf(Character.toUpperCase(name.charAt(0))); if (name.length() > 1) this.name += name.substring(1).toLowerCase(); }
        this.removable = true;
    }

    /**
     * A constructor that takes singular, name, and removable information.
     * @param singular a boolean representing the singularity of this tag
     * @param name a string representing the name of this tag
     * @param removable a boolean representing the removability of this tag
     */
    public Tag(boolean singular, String name, boolean removable) {
        this.singular = singular;
        if (name != null) { this.name = String.valueOf(Character.toUpperCase(name.charAt(0))); if (name.length() > 1) this.name += name.substring(1).toLowerCase(); }
        this.removable = removable;
    }

    /**
     * Adds a value to the list of values of this tag.
     * @param val a string containing the value to add
     * @return a boolean that indicates whether or not the add was successful
     */
    public boolean add(String val) {
        if (singular && values.size() == 1) {
            return false;
        }
        return values.add(new TagValue(val, this));
    }

    /**
     * Removes a value from the list of values of this tag.
     * @param val a string containing the value to remove
     * @return a boolean that indicates whether or not the removal was successful
     */
    public boolean remove(String val) {
        if (!removable) return false;
        return values.remove(new TagValue(val, this));
    }

    /**
     * Gets the name of this tag.
     * @return a string representing the name of this tag
     */
    public String getName() { return name; }

    @Override
    public String toString() { if (name == null) { return "None"; } return getName(); }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Tag)) return false;
        Tag obj = (Tag)other;
        if (name == null && obj.name == null) return true;
        if (name == null) return false;
        return name.equals(obj.getName());
    }
}

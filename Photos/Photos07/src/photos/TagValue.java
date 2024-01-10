package photos;

import java.io.Serializable;

/**
 * Our class for tag values.
 * @author Nima Fallah
 * @author Alex Varjabedian
 */
public class TagValue implements Serializable {
    /**
     * A string representing the value of this tag.
     */
    public String value;

    /**
     * A <code>Tag</code> indicating the tag to which this value belongs.
     */
    public Tag parent;

    /**
     * A constructor that takes a value and parent tag.
     * @param value a string representing the value of this tag.
     * @param parent a <code>Tag</code> indicating the tag to which this value belongs
     */
    public TagValue(String value, Tag parent)  {
        this.value = value;
        this.parent = parent;
    }

    @Override
    public String toString() { return parent.getName() + " | " + value; }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof TagValue)) return false;
        TagValue obj = (TagValue)other;
        return parent.equals(obj.parent) && value.equals(obj.value);
    }
}

package chess;

/**
 * Our point class. Stores information about a spot on the board.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class Point {
    /**
     * The row index of this point.
     */
    public int i;

    /**
     * The column index of this point.
     */
    public int j;

    /**
     * A string that contains information about whether or not an input contains a resign or draw request.
     */
    public String r = "";

    /**
     * A string that contains information about a possible promotion piece.
     */
    public String promotion = "";

    /**
     * A constructor that takes row and column indexes.
     * @param i the row index of this point
     * @param j the column index of this point
     */
    public Point(int i, int j) {
        this.i = i;
        this.j = j;
    }

    /**
     * A constructor that takes a string indicating resignation or drawing.
     * @param r a string indicating resignation
     */
    public Point(String r) {
        this.r = r;
    }

    /**
     * A constructor that takes row and column indexes. Also takes information about promotion.
     * Only to be used for promotions.
     * @param i the row index of this point
     * @param j the column index of this point
     * @param p a string containing information about the new piece after promotion
     */
    public Point(int i, int j, String p) {
        this.i = i;
        this.j = j;
        this.promotion = p;
    }

    @Override
    public String toString() {
        return "(" + i + ", " + j + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Point)) return false;
        Point p = (Point) o;
        return this.i == p.i && this.j == p.j;
    }
}
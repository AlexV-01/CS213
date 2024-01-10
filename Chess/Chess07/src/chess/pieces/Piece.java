package chess.pieces;

import java.util.*;
import chess.*;

/**
 * Our piece abstract class. It provides a model/frame for all chess pieces.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public abstract class Piece {
    /**
     * The row index of this piece.
     */
    int row;

    /**
     * The column index of this piece.
     */
    int col;

    /**
     * A string representing the symbol of this piece. Used in logic and for printing board.
     */
    String symbol;

    /**
     * An ArrayList of type <code>Point</code> containing all possible spots to which this piece can move.
     * Spots at which this piece can take another piece are not included.
     */
    ArrayList<Point> possibleMoves = new ArrayList<Point>();

    /**
     * An ArrayList of type <code>Point</code> containing all possible spots to which this piece can move AND
     * at which this piece can take another piece.
     */
    ArrayList<Point> possibleTakes = new ArrayList<Point>();

    /**
     * An abstract method that calculates all possible moves (excluding takes) of a piece. It populates
     * the <code>possibleMoves</code> ArrayList.
     */
    public abstract void calculatePossibleMoves();

    /**
     * An abstract method that calculates all possible spots at which this piece can take another piece. It
     * populates the <code>possibleTakes</code> ArrayList.
     */
    public abstract void calculatePossibleTakes();

    /**
     * Determines whether or not this piece can move to a desired spot on the chess board.
     * @param i the desired row index
     * @param j the desired column index
     * @return <code>true</code> if this piece can move to the spot, <code>false</code> otherwise
     */
    public boolean canMove(int i, int j) {
        for (int x = 0; x < possibleMoves.size(); x++) {
            if (possibleMoves.get(x).i == i && possibleMoves.get(x).j == j) return true;
        }
        return false;
    }

    /**
     * Determines whether or not this piece can take a piece at a desired spot on the chess board.
     * @param i the desired row index
     * @param j the desired column index
     * @return <code>true</code> if this piece can take at the spot, <code>false</code> otherwise
     */
    public boolean canTake(int i, int j) {
        for (int x = 0; x < possibleTakes.size(); x++) {
            if (possibleTakes.get(x).i == i && possibleTakes.get(x).j == j) return true;
        }
        return false;
    }
    
    /**
     * Returns the <code>possibleMoves</code> ArrayList.
     * @return an ArrayList containing <code>Point</code> objects
     */
    public ArrayList<Point> getPossibleMoves() {
        return possibleMoves;
    }

    /**
     * Returns the <code>possibleTakes</code> ArrayList.
     * @return an ArrayList containing <code>Point</code> objects
     */
    public ArrayList<Point> getPossibleTakes() {
        return possibleTakes;
    }

    /**
     * Clears the <code>possibleMoves</code> ArrayList.
     */
    public void clearPossibleMoves() {
        possibleMoves.clear();
    }

    /**
     * Clears the <code>possibleTakes</code> ArrayList.
     */
    public void clearPossibleTakes() {
        possibleTakes.clear();
    }

    /**
     * Returns the row index of this piece.
     * @return an <code>int</code> representing the row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index of this piece.
     * @return an <code>int</code> representing the column index
     */
    public int getCol() {
        return col;
    }

    /**
     * Changes the <code>row</code> and <code>col</code> fields of this piece.
     * @param row the desired row index
     * @param col the desired column index
     */
    public void setPos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
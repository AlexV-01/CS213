package chess.pieces;

import chess.*;
import java.util.ArrayList;

/**
 * Our class for a black pawn.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class BlackPawn extends BlackPiece {
    /**
     * An ArrayList of <code>Point</code> objects containing spots that may be attacking the enemy king.
     */
    ArrayList<Point> possibleAttackingKing = new ArrayList<Point>();

    /**
     * If this pawn qualifies for en passant, this <code>Point</code> represents the en passant spot.
     * Otherwise, it is left as <code>null</code>.
     */
    public Point enPessant = null;

    /**
     * Two <code>boolean</code> variables. <code>doubleStep</code> indicates whether or not this pawn's last
     * move was a double step.
     * 
     * Due to the nature of the program, the status of any pawn will be updated right after it could be marked
     * as a pawn that just moved two units. Therefore, if <code>doubleStep</code> was the only step variable,
     * it would never be <code>true</code> because it would become <code>false</code> after being updated.
     * Therefore, there is an additional variable, <code>fakeDoubleStep</code> that becomes <code>false</code>
     * first such that, once this variable is <code>false</code>, the <code>doubleStep</code> also becomes
     * <code>false</code>. This ensures that, for one turn after a pawn moves, it will be marked as a
     * double-stepped pawn so that en pessant can work properly.
     */
    public boolean fakeDoubleStep = false, doubleStep = false;
    
    /**
     * The <code>BlackPawn</code> constructor. Takes a row index and a column index.
     * @param i the row index
     * @param j the column index
     */
    public BlackPawn(int i, int j) {
        row = i;
        col = j;
        symbol = "bp";
    }

    @Override
    public void calculatePossibleMoves() {
        if (row == 7 || Board.getPiece(row + 1, col) != null) return;
        possibleMoves.add(new Point(row + 1, col));
        if (row == 1 && Board.getPiece(row + 2, col) == null) possibleMoves.add(new Point(row + 2, col));
    }

    @Override
    public void calculatePossibleTakes() {
        if (enPessant != null) { enPessant = null; }
        if (fakeDoubleStep) { fakeDoubleStep = false; } else if (doubleStep) { doubleStep = false; } 
        if (row == 7) return;
        if (col + 1 < 8 && Board.getPiece(row + 1, col + 1) instanceof WhitePiece) possibleTakes.add(new Point(row + 1, col + 1));
        if (col - 1 >= 0 && Board.getPiece(row + 1, col - 1) instanceof WhitePiece) possibleTakes.add(new Point(row + 1, col - 1));
        // en pessant
        if (row == 4) {
            if (col - 1 >= 0 && Board.getPiece(4, col - 1) != null && Board.getPiece(4, col - 1).symbol.equals("wp") && ((WhitePawn)(Board.getPiece(4, col - 1))).doubleStep && Board.getPiece(5, col - 1) == null) {
                possibleTakes.add(new Point(5, col - 1));
                enPessant = new Point(5, col - 1);
            }

            else if (col + 1 < 8 && Board.getPiece(4, col + 1) != null && Board.getPiece(4, col + 1).symbol.equals("wp") && ((WhitePawn)(Board.getPiece(4, col + 1))).doubleStep && Board.getPiece(5, col + 1) == null) {
                possibleTakes.add(new Point(5, col + 1));
                enPessant = new Point(5, col + 1);
            }
        }
    }
    
    /**
     * Populates <code>possibleAttackingKing</code> with spaces that may be attacking the enemy king.
     */
    public void updatePossibleAttackingKing() {
        if (row == 7) return;
        possibleAttackingKing.clear();
        if (col + 1 < 8 && Board.getPiece(row + 1, col + 1) == null) possibleAttackingKing.add(new Point(row + 1, col + 1));
        if (col - 1 >= 0 && Board.getPiece(row + 1, col - 1) == null) possibleAttackingKing.add(new Point(row + 1, col - 1));
    }

    /**
     * Returns the <code>possibleAttackingKing</code> ArrayList.
     * @return an ArrayList of type <code>Point</code>
     */
    public ArrayList<Point> getPossibleAttackingKing() {
        return possibleAttackingKing;
    }
}
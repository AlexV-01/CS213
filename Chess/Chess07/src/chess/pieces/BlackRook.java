package chess.pieces;

import chess.*;

/**
 * Our class for a black rook.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class BlackRook extends BlackPiece {
    /**
     * A <code>boolean</code> indicating whether or not this piece has moved.
     */
    public boolean hasMoved = false;

    /**
     * The <code>BlackRook</code> constructor. Takes a row index and a column index.
     * @param i the row index
     * @param j the column index
     */
    public BlackRook(int i, int j) {
        row = i;
        col = j;
        symbol = "bR";
    }

    @Override
    public void calculatePossibleMoves() {
        for (int j = col + 1; j < 8; j++) { // from rook to end of row
            if (Board.getPiece(row, j) != null) break;
            possibleMoves.add(new Point(row, j));
        }
        for (int j = col - 1; j >= 0; j--) { // from rook to beginning of row
            if (Board.getPiece(row, j) != null) break;
            possibleMoves.add(new Point(row, j));
        }
        for (int i = row + 1; i < 8; i++) { // from rook to end of column
            if (Board.getPiece(i, col) != null) break;
            possibleMoves.add(new Point(i, col));
        }
        for (int i = row - 1; i >= 0; i--) { // from rook to beginning of column
            if (Board.getPiece(i, col) != null) break;
            possibleMoves.add(new Point(i, col));
        }
    }

    @Override
    public void calculatePossibleTakes() {
        for (int j = col + 1; j < 8; j++) { // from rook to end of row
            if (Board.getPiece(row, j) instanceof BlackPiece) break;
            if (Board.getPiece(row, j) instanceof WhitePiece) {
                possibleTakes.add(new Point(row, j));
                break;
            }
        }
        for (int j = col - 1; j >= 0; j--) { // from rook to beginning of row
            if (Board.getPiece(row, j) instanceof BlackPiece) break;
            if (Board.getPiece(row, j) instanceof WhitePiece) {
                possibleTakes.add(new Point(row, j));
                break;
            }
        }
        for (int i = row + 1; i < 8; i++) { // from rook to end of column
            if (Board.getPiece(i, col) instanceof BlackPiece) break;
            if (Board.getPiece(i, col) instanceof WhitePiece) {
                possibleTakes.add(new Point(i, col));
                break;
            }
        }
        for (int i = row - 1; i >= 0; i--) { // from rook to beginning of column
            if (Board.getPiece(i, col) instanceof BlackPiece) break;
            if (Board.getPiece(i, col) instanceof WhitePiece) {
                possibleTakes.add(new Point(i, col));
                break;
            }
        }
    }
}
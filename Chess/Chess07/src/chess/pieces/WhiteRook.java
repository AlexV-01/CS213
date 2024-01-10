package chess.pieces;

import chess.*;

/**
 * Our class for a white rook.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class WhiteRook extends WhitePiece {
    /**
     * A <code>boolean</code> indicating whether or not this piece has moved.
     */
    public boolean hasMoved = false;

    /**
     * The <code>WhiteRook</code> constructor. Takes a row index and a column index.
     * @param i the row index
     * @param j the column index
     */
    public WhiteRook(int i, int j) {
        row = i;
        col = j;
        symbol = "wR";
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
            if (Board.getPiece(row, j) instanceof WhitePiece) break;
            if (Board.getPiece(row, j) instanceof BlackPiece) {
                possibleTakes.add(new Point(row, j));
                break;
            }
        }
        for (int j = col - 1; j >= 0; j--) { // from rook to beginning of row
            if (Board.getPiece(row, j) instanceof WhitePiece) break;
            if (Board.getPiece(row, j) instanceof BlackPiece) {
                possibleTakes.add(new Point(row, j));
                break;
            }
        }
        for (int i = row + 1; i < 8; i++) { // from rook to end of column
            if (Board.getPiece(i, col) instanceof WhitePiece) break;
            if (Board.getPiece(i, col) instanceof BlackPiece) {
                possibleTakes.add(new Point(i, col));
                break;
            }
        }
        for (int i = row - 1; i >= 0; i--) { // from rook to beginning of column
            if (Board.getPiece(i, col) instanceof WhitePiece) break;
            if (Board.getPiece(i, col) instanceof BlackPiece) {
                possibleTakes.add(new Point(i, col));
                break;
            }
        }
    }
}
package chess.pieces;

import chess.*;

/**
 * Our class for a black queen.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class BlackQueen extends BlackPiece {
    /**
     * The <code>BlackQueen</code> constructor. Takes a row index and a column index.
     * @param i the row index
     * @param j the column index
     */
    public BlackQueen(int i, int j) {
        row = i;
        col = j;
        symbol = "bQ";
    }

    @Override
    public void calculatePossibleMoves() {
        // DIAGONAL MOVEMENT
        // to top-right
        int i = row - 1;
        int j = col + 1;
        while (i >= 0 && j < 8) {
            if (Board.getPiece(i, j) != null) break;
            possibleMoves.add(new Point(i, j));
            i--;
            j++;
        }
        // to top-left
        i = row - 1;
        j = col - 1;
        while (i >= 0 && j >= 0) {
            if (Board.getPiece(i, j) != null) break;
            possibleMoves.add(new Point(i, j));
            i--;
            j--;
        }
        // to bottom-left
        i = row + 1;
        j = col - 1;
        while (i < 8 && j >= 0) {
            if (Board.getPiece(i, j) != null) break;
            possibleMoves.add(new Point(i, j));
            i++;
            j--;
        }
        // to bottom-right
        i = row + 1;
        j = col + 1;
        while (i < 8 && j < 8) {
            if (Board.getPiece(i, j) != null) break;
            possibleMoves.add(new Point(i, j));
            i++;
            j++;
        }
        // HORIZONTAL AND VERTICAL MOVEMENT
        for (j = col + 1; j < 8; j++) { // from queen to end of row
            if (Board.getPiece(row, j) != null) break;
            possibleMoves.add(new Point(row, j));
        }
        for (j = col - 1; j >= 0; j--) { // from queen to beginning of row
            if (Board.getPiece(row, j) != null) break;
            possibleMoves.add(new Point(row, j));
        }
        for (i = row + 1; i < 8; i++) { // from queen to end of column
            if (Board.getPiece(i, col) != null) break;
            possibleMoves.add(new Point(i, col));
        }
        for (i = row - 1; i >= 0; i--) { // from queen to beginning of column
            if (Board.getPiece(i, col) != null) break;
            possibleMoves.add(new Point(i, col));
        }
    }

    @Override
    public void calculatePossibleTakes() {
        // DIAGONAL MOVEMENT
        // to top-right
        int i = row - 1;
        int j = col + 1;
        while (i >= 0 && j < 8) {
            if (Board.getPiece(i, j) instanceof BlackPiece) break;
            if (Board.getPiece(i, j) instanceof WhitePiece) {
                possibleTakes.add(new Point(i, j));
                break;
            }
            i--;
            j++;
        }
        // to top-left
        i = row - 1;
        j = col - 1;
        while (i >= 0 && j >= 0) {
            if (Board.getPiece(i, j) instanceof BlackPiece) break;
            if (Board.getPiece(i, j) instanceof WhitePiece) {
                possibleTakes.add(new Point(i, j));
                break;
            }
            i--;
            j--;
        }
        // to bottom-left
        i = row + 1;
        j = col - 1;
        while (i < 8 && j >= 0) {
            if (Board.getPiece(i, j) instanceof BlackPiece) break;
            if (Board.getPiece(i, j) instanceof WhitePiece) {
                possibleTakes.add(new Point(i, j));
                break;
            }
            i++;
            j--;
        }
        // to bottom-right
        i = row + 1;
        j = col + 1;
        while (i < 8 && j < 8) {
            if (Board.getPiece(i, j) instanceof BlackPiece) break;
            if (Board.getPiece(i, j) instanceof WhitePiece) {
                possibleTakes.add(new Point(i, j));
                break;
            }
            i++;
            j++;
        }
        // HORIZONTAL AND VERTICAL MOVEMENT
        for (j = col + 1; j < 8; j++) { // from queen to end of row
            if (Board.getPiece(row, j) instanceof BlackPiece) break;
            if (Board.getPiece(row, j) instanceof WhitePiece) {
                possibleTakes.add(new Point(row, j));
                break;
            }
        }
        for (j = col - 1; j >= 0; j--) { // from queen to beginning of row
            if (Board.getPiece(row, j) instanceof BlackPiece) break;
            if (Board.getPiece(row, j) instanceof WhitePiece) {
                possibleTakes.add(new Point(row, j));
                break;
            }
        }
        for (i = row + 1; i < 8; i++) { // from queen to end of column
            if (Board.getPiece(i, col) instanceof BlackPiece) break;
            if (Board.getPiece(i, col) instanceof WhitePiece) {
                possibleTakes.add(new Point(i, col));
                break;
            }
        }
        for (i = row - 1; i >= 0; i--) { // from queen to beginning of column
            if (Board.getPiece(i, col) instanceof BlackPiece) break;
            if (Board.getPiece(i, col) instanceof WhitePiece) {
                possibleTakes.add(new Point(i, col));
                break;
            }
        }
    }
}
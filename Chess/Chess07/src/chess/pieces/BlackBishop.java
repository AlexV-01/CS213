package chess.pieces;

import chess.*;

/**
 * Our class for a black bishop.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class BlackBishop extends BlackPiece {
    /**
     * The <code>BlackBishop</code> constructor. Takes a row index and a column index.
     * @param i the row index
     * @param j the column index
     */
    public BlackBishop(int i, int j) {
        row = i;
        col = j;
        symbol = "bB";
    }

    @Override
    public void calculatePossibleMoves() {
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
    }

    @Override
    public void calculatePossibleTakes() {
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
    }
}
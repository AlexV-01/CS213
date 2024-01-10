package chess.pieces;

import chess.*;

/**
 * Our class for a white bishop.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class WhiteBishop extends WhitePiece {
    /**
     * The <code>WhiteBishop</code> constructor. Takes a row index and a column index.
     * @param i the row index
     * @param j the column index
     */
    public WhiteBishop(int i, int j) {
        row = i;
        col = j;
        symbol = "wB";
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
            if (Board.getPiece(i, j) instanceof WhitePiece) break;
            if (Board.getPiece(i, j) instanceof BlackPiece) {
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
            if (Board.getPiece(i, j) instanceof WhitePiece) break;
            if (Board.getPiece(i, j) instanceof BlackPiece) {
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
            if (Board.getPiece(i, j) instanceof WhitePiece) break;
            if (Board.getPiece(i, j) instanceof BlackPiece) {
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
            if (Board.getPiece(i, j) instanceof WhitePiece) break;
            if (Board.getPiece(i, j) instanceof BlackPiece) {
                possibleTakes.add(new Point(i, j));
                break;
            }
            i++;
            j++;
        }
    }
}
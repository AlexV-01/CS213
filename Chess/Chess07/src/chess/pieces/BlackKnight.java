package chess.pieces;

import chess.*;

/**
 * Our class for a black knight.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class BlackKnight extends BlackPiece {
    /**
     * The <code>BlackKnight</code> constructor. Takes a row index and a column index.
     * @param i the row index
     * @param j the column index
     */
    public BlackKnight(int i, int j) {
        row = i;
        col = j;
        symbol = "bN";
    }

    @Override
    public void calculatePossibleMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Board.getPiece(i, j) != null) continue;
                if ((i == row - 1 && j == col - 2) || (i == row - 2 && j == col - 1) || (i == row - 2 && j == col + 1) || (i == row - 1 && j == col + 2) ||
                (i == row + 1 && j == col - 2) || (i == row + 2 && j == col - 1) || (i == row + 2 && j == col + 1) || (i == row + 1 && j == col + 2)) possibleMoves.add(new Point(i, j));
            }
        }
    }

    @Override
    public void calculatePossibleTakes() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!(Board.getPiece(i, j) instanceof WhitePiece)) continue;
                if ((i == row - 1 && j == col - 2) || (i == row - 2 && j == col - 1) || (i == row - 2 && j == col + 1) || (i == row - 1 && j == col + 2) ||
                (i == row + 1 && j == col - 2) || (i == row + 2 && j == col - 1) || (i == row + 2 && j == col + 1) || (i == row + 1 && j == col + 2)) possibleTakes.add(new Point(i, j));
            }
        }
    }
}
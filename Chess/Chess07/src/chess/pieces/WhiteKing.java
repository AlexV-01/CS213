package chess.pieces;

import chess.*;

/**
 * Our class for a white king.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class WhiteKing extends WhitePiece {
    /**
     * A <code>boolean</code> indicating whether or not this piece has moved.
     */
    public boolean hasMoved = false;

    /**
     * The <code>WhiteKing</code> constructor. Takes a row index and a column index.
     * @param i the row index
     * @param j the column index
     */
    public WhiteKing(int i, int j) {
        row = i;
        col = j;
        symbol = "wK";
    }

    @Override
    public void calculatePossibleMoves() {
        for (int i = 0; i < 8; i++) {
            outerloop: // IF DANGER SPACE DETECTED, GO TO NEXT ITERATION
            for (int j = 0; j < 8; j++) {
                if (Board.getPiece(i, j) != null) continue;
                for (Piece p : Board.getAllPieces()) {
                    if (p instanceof WhitePiece) continue;
                    if (p.toString().equals("bp")) {
                        for (Point t : ((BlackPawn) p).getPossibleAttackingKing()) {
                            if (t.i == i && t.j == j) continue outerloop;
                        }
                    } else {
                        for (Point t : p.getPossibleMoves()) {
                            if (t.i == i && t.j == j) continue outerloop;
                        }
                    }
                }
                if ((i == row - 1 && j == col - 1) || (i == row - 1 && j == col) || (i == row - 1 && j == col + 1) ||
                (i == row && j == col - 1) || (i == row && j == col + 1) || (i == row + 1 && j == col - 1) ||
                (i == row + 1 && j == col) || (i == row + 1 && j == col + 1)) possibleMoves.add(new Point(i, j));
            }
        }
        // CASTLING
        if (!hasMoved && Board.getPiece(7, 7) != null && Board.getPiece(7, 7).toString().equals("wR") && !((WhiteRook) Board.getPiece(7, 7)).hasMoved &&
        Board.getPiece(7, 5) == null && Board.getPiece(7, 6) == null && !Board.whiteInCheck() &&
        !Board.blackAttackingSpot(new Point(7, 5)) && !Board.blackAttackingSpot(new Point(7, 6))) possibleMoves.add(new Point(7, 6));

        if (!hasMoved && Board.getPiece(7, 0) != null && Board.getPiece(7, 0).toString().equals("wR") && !((WhiteRook) Board.getPiece(7, 0)).hasMoved &&
        Board.getPiece(7, 3) == null && Board.getPiece(7, 2) == null && Board.getPiece(7, 1) == null && !Board.whiteInCheck() &&
        !Board.blackAttackingSpot(new Point(7, 3)) && !Board.blackAttackingSpot(new Point(7, 2))) possibleMoves.add(new Point(7, 2));
    }

    @Override
    public void calculatePossibleTakes() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!(Board.getPiece(i, j) instanceof BlackPiece)) continue;
                outerloop: // IF ATTACKED SPACE DETECTED, GO TO NEXT ITERATION
                for (Piece p : Board.getAllPieces()) {
                    if (p instanceof WhitePiece) continue;
                    if (p.toString().equals("bp")) {
                        for (Point t : ((BlackPawn) p).getPossibleAttackingKing()) {
                            if (t.i == i && t.j == j) continue outerloop;
                        }
                    } else {
                        for (Point t : p.getPossibleMoves()) {
                            if (t.i == i && t.j == j) continue outerloop;
                        }
                    }
                }
                if ((i == row - 1 && j == col - 1) || (i == row - 1 && j == col) || (i == row - 1 && j == col + 1) ||
                (i == row && j == col - 1) || (i == row && j == col + 1) || (i == row + 1 && j == col - 1) ||
                (i == row + 1 && j == col) || (i == row + 1 && j == col + 1)) possibleTakes.add(new Point(i, j));
            }
        }
    }
}
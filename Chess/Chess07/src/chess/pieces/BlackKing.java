package chess.pieces;

import chess.*;

/**
 * Our class for a black king.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class BlackKing extends BlackPiece {
    /**
     * A <code>boolean</code> indicating whether or not this piece has moved.
     */
    public boolean hasMoved = false;

    /**
     * The <code>BlackKing</code> constructor. Takes a row index and a column index.
     * @param i the row index
     * @param j the column index
     */
    public BlackKing(int i, int j) {
        row = i;
        col = j;
        symbol = "bK";
    }

    @Override
    public void calculatePossibleMoves() {
        for (int i = 0; i < 8; i++) {
            outerloop: // IF DANGER SPACE DETECTED, GO TO NEXT ITERATION
            for (int j = 0; j < 8; j++) {
                if (Board.getPiece(i, j) != null) continue;
                for (Piece p : Board.getAllPieces()) {
                    if (p instanceof BlackPiece) continue;
                    if (p.toString().equals("wp")) {
                        for (Point t : ((WhitePawn) p).getPossibleAttackingKing()) {
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
        if (!hasMoved && Board.getPiece(0, 7) != null && Board.getPiece(0, 7).toString().equals("bR") && !((BlackRook) Board.getPiece(0, 7)).hasMoved &&
        Board.getPiece(0, 5) == null && Board.getPiece(0, 6) == null && !Board.blackInCheck() &&
        !Board.whiteAttackingSpot(new Point(0, 5)) && !Board.whiteAttackingSpot(new Point(0, 6))) possibleMoves.add(new Point(0, 6));

        if (!hasMoved && Board.getPiece(0, 0) != null && Board.getPiece(0, 0).toString().equals("bR") && !((BlackRook) Board.getPiece(0, 0)).hasMoved &&
        Board.getPiece(0, 3) == null && Board.getPiece(0, 2) == null && Board.getPiece(0, 1) == null && !Board.blackInCheck() &&
        !Board.whiteAttackingSpot(new Point(0, 3)) && !Board.whiteAttackingSpot(new Point(0, 2))) possibleMoves.add(new Point(0, 2));
    }

    @Override
    public void calculatePossibleTakes() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!(Board.getPiece(i, j) instanceof WhitePiece)) continue;
                outerloop: // IF ATTACKED SPACE DETECTED, GO TO NEXT ITERATION
                for (Piece p : Board.getAllPieces()) {
                    if (p instanceof BlackPiece) continue;
                    if (p.toString().equals("wp")) {
                        for (Point t : ((WhitePawn) p).getPossibleAttackingKing()) {
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
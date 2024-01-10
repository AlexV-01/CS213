package chess;

import chess.pieces.*;

/**
 * Our main class. Runs the program.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class Chess {
    /**
     * Our main method. It contains the cycle of the game.
     * @param args the commmand line arguments
     */
    public static void main(String[] args) {
        Board.initialize();
        Board.printBoard();
        System.out.println();

        outer:
        while (true) {
            /*
             *  WHITE'S TURN
             */

            // get white input
            System.out.print("White's move: ");
            Point[] input = Board.getInputAndConvert();

            // check for resign
            if (input[0].r.equals("resign")) {
                System.out.println("Black wins");
                break;
            }

            // check for legal move
            while (!(Board.getPiece(input[0]) instanceof WhitePiece) || Board.getPiece(input[1]) instanceof WhitePiece ||
            (Board.getPiece(input[1]) instanceof BlackPiece && !Board.getPiece(input[0]).canTake(input[1].i, input[1].j)) ||
            (Board.getPiece(input[1]) == null && ((Board.getPiece(input[0]) instanceof WhitePawn && (!Board.getPiece(input[0]).canMove(input[1].i, input[1].j) && !Board.getPiece(input[0]).canTake(input[1].i, input[1].j))) || (!(Board.getPiece(input[0]) instanceof WhitePawn) && !Board.getPiece(input[0]).canMove(input[1].i, input[1].j)))) ||
            Board.whiteWillBeInCheck(input[0], input[1])) {
                System.out.print("Illegal move, try again\nWhite's move: ");
                input = Board.getInputAndConvert();
                // check for resign
                if (input[0].r.equals("resign")) {
                    System.out.println("Black wins");
                    break outer;
                }
            }

            // check for draw
            if (input[2] != null) {
                System.out.println("draw");
                break;
            }

            // move piece
            Board.movePiece(input[0], input[1]);

            // possible castle
            if (Board.getPiece(input[1]).toString().charAt(1) == 'K' && input[1].j - input[0].j == 2) Board.movePiece(new Point(7, 7), new Point(7, 5));
            if (Board.getPiece(input[1]).toString().charAt(1) == 'K' && input[1].j - input[0].j == -2) Board.movePiece(new Point(7, 0), new Point(7, 3));

            // possible promotion
            if (Board.getPiece(input[1]).toString().charAt(1) == 'p' && Board.getPiece(input[1]).getRow() == 0) {
                Board.promoteWhite(input[1], input[0].promotion); // input[1] used to identify piece on board since it already moved
            }

            // mark moved pieces
            if (Board.getPiece(input[1]).toString().charAt(1) == 'K') ((WhiteKing) Board.getPiece(input[1])).hasMoved = true;
            if (Board.getPiece(input[1]).toString().charAt(1) == 'R') ((WhiteRook) Board.getPiece(input[1])).hasMoved = true;
            
            // re-calculate possible moves and possible takes
            System.out.println();
            Board.calculateAll();

            // print board
            Board.printBoard();
            System.out.println();

            // check for checkmate
            if (Board.blackInCheckmate()) {
                System.out.println("Checkmate");
                System.out.println("White wins");
                break;
            }
            
            // check for check
            if (Board.blackInCheck()) System.out.println("Check");

            /*
             *  BLACK'S TURN
             */

            // get black input
            System.out.print("Black's move: ");
            input = Board.getInputAndConvert();

            // check for resign
            if (input[0].r.equals("resign")) {
                System.out.println("White wins");
                break;
            }

            // check for legal move
            while (!(Board.getPiece(input[0]) instanceof BlackPiece) || Board.getPiece(input[1]) instanceof BlackPiece ||
            (Board.getPiece(input[1]) instanceof WhitePiece && !Board.getPiece(input[0]).canTake(input[1].i, input[1].j)) ||
            (Board.getPiece(input[1]) == null && ((Board.getPiece(input[0]) instanceof BlackPawn && (!Board.getPiece(input[0]).canMove(input[1].i, input[1].j) && !Board.getPiece(input[0]).canTake(input[1].i, input[1].j))) || (!(Board.getPiece(input[0]) instanceof BlackPawn) && !Board.getPiece(input[0]).canMove(input[1].i, input[1].j)))) ||
            Board.blackWillBeInCheck(input[0], input[1])) {
                System.out.print("Illegal move, try again\nBlack's move: ");
                input = Board.getInputAndConvert();
                // check for resign
                if (input[0].r.equals("resign")) {
                System.out.println("Black wins");
                    break outer;
                }
            }

            // check for draw
            if (input[2] != null) {
                System.out.println("draw");
                break;
            }

            // move piece
            Board.movePiece(input[0], input[1]);

            // possible castle
            if (Board.getPiece(input[1]).toString().charAt(1) == 'K' && input[1].j - input[0].j == 2) Board.movePiece(new Point(0, 7), new Point(0, 5));
            if (Board.getPiece(input[1]).toString().charAt(1) == 'K' && input[1].j - input[0].j == -2) Board.movePiece(new Point(0, 0), new Point(0, 3));

            // possible promotion
            if (Board.getPiece(input[1]).toString().charAt(1) == 'p' && Board.getPiece(input[1]).getRow() == 7) {
                Board.promoteBlack(input[1], input[0].promotion); // input[1] used to identify piece on board since it already moved
            }

            // mark moved pieces
            if (Board.getPiece(input[1]).toString().charAt(1) == 'K') ((BlackKing) Board.getPiece(input[1])).hasMoved = true;
            if (Board.getPiece(input[1]).toString().charAt(1) == 'R') ((BlackRook) Board.getPiece(input[1])).hasMoved = true;

            // re-calculate possible moves and possible takes
            System.out.println();
            Board.calculateAll();

            // print board
            Board.printBoard();
            System.out.println();

            // check for checkmate
            if (Board.whiteInCheckmate()) {
                System.out.println("Checkmate");
                System.out.println("Black wins");
                break;
            }

            // check for check
            if (Board.whiteInCheck()) System.out.println("Check");
        }
    }
}
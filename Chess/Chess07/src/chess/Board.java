package chess;

import java.util.ArrayList;
import java.util.Scanner;

import chess.pieces.*;

/**
 * Our board class. Functions as a utility class for controlling the chess board.
 * @author Alex Varjabedian
 * @author Nima Fallah
 */
public class Board {
    /**
     * The chess board.
     */
    private static Piece[][] board = new Piece[8][8];

    /**
     * The scanner object.
     */
    private static Scanner scan = new Scanner(System.in);
    
    /**
     * Initializes the board with pieces. Then, it calls <code>calculateAll()</code>.
     */
    public static void initialize() {
        board[0][4] = new BlackKing(0, 4);
        board[0][3] = new BlackQueen(0, 3);
        board[0][0] = new BlackRook(0, 0);
        board[0][7] = new BlackRook(0, 7);
        board[0][2] = new BlackBishop(0, 2);
        board[0][5] = new BlackBishop(0, 5);
        board[0][1] = new BlackKnight(0, 1);
        board[0][6] = new BlackKnight(0, 6);
        for (int j = 0; j < 8; j++) board[1][j] = new BlackPawn(1, j);

        board[7][4] = new WhiteKing(7, 4);
        board[7][3] = new WhiteQueen(7, 3);
        board[7][0] = new WhiteRook(7, 0);
        board[7][7] = new WhiteRook(7, 7);
        board[7][2] = new WhiteBishop(7, 2);
        board[7][5] = new WhiteBishop(7, 5);
        board[7][1] = new WhiteKnight(7, 1);
        board[7][6] = new WhiteKnight(7, 6);
        for (int j = 0; j < 8; j++) board[6][j] = new WhitePawn(6, j);

        calculateAll();
    }
    
    /**
     * Calculates all possible moves and takes of all pieces (except for kings).
     * Also accounts for pawns taking differently than they move. Then calls
     * <code>calculateKings()</code>.
     */
    public static void calculateAll() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getPiece(i, j) != null && !(getPiece(i, j) instanceof WhiteKing) && !(getPiece(i, j) instanceof BlackKing)) {
                    getPiece(i, j).clearPossibleMoves();
                    getPiece(i, j).clearPossibleTakes();
                    getPiece(i, j).calculatePossibleMoves();
                    getPiece(i, j).calculatePossibleTakes();
                    if (getPiece(i, j).toString().equals("wp")) {
                        ((WhitePawn) getPiece(i, j)).updatePossibleAttackingKing();
                    }
                    if (getPiece(i, j).toString().equals("bp")) {
                        ((BlackPawn) getPiece(i, j)).updatePossibleAttackingKing();
                    }
                }
            }
        }
        calculateKings();
    }

    /**
     * Calculates all possible moves and takes of kings.
     */
    public static void calculateKings() { // KINGS COME LAST BECAUSE THEY CANNOT WALK INTO CHECK
        for (Piece p : getAllPieces()) {
            if (p.toString().equals("wK") || p.toString().equals("bK")) {
                p.clearPossibleMoves();
                p.clearPossibleTakes();
                p.calculatePossibleMoves();
                p.calculatePossibleTakes();
            }
        }
    }

    // UTILITY METHODS

    /**
     * Gets a row of pieces from the board for any given row.
     * @param row the row number as an index
     * @return the array of <code>Piece</code> objects determined by row index
     */
    public static Piece[] getRow(int row) {
        return board[row];
    }

    /**
     * Gets a column of pieces from the board for any given column.
     * @param col the column number as an index
     * @return the array of <code>Piece</code> objects determined by column index
     */
    public static Piece[] getCol(int col) {
        Piece[] c = new Piece[8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (j == col) {
                    c[i] = board[i][j];
                }
            }
        }
        return c;
    }
    
    /**
     * Gets the whole chess board as a 2D array.
     * @return a 2D array of <code>Piece</code> objects
     */
    public static Piece[][] getBoard() {
        return board;
    }

    /**
     * Prints the whole chess board.
     */
    public static void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null && (i + j) % 2 == 1) {
                    System.out.print("## ");
                } else if (board[i][j] == null) {
                    System.out.print("   ");
                } else {
                System.out.print(board[i][j] + " ");
                }
            }
            System.out.println(8-i);
        }
        System.out.println(" a  b  c  d  e  f  g  h");
    }

    /**
     * Gets the piece at a given spot. Determined by indexes.
     * @param i the desired row index
     * @param j the desired column index
     * @return a <code>Piece</code> object at the given spot
     */
    public static Piece getPiece(int i, int j) {
        return board[i][j];
    }

    /**
     * Gets the piece at a given spot. Determined by a <code>Point</code> object.
     * @param p a <code>Point</code> object indicating the desired point on the board
     * @return a <code>Piece</code> object at the given spot
     */
    public static Piece getPiece(Point p) {
        return getPiece(p.i, p.j);
    }

    /**
     * Gets all pieces that are still active on the board.
     * @return an ArrayList of type <code>Piece</code>
     */
    public static ArrayList<Piece> getAllPieces() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getPiece(i, j) != null) {
                    pieces.add(getPiece(i, j));
                }
            }
        }
        return pieces;
    }

    /**
     * Sets piece on the board at a given spot. Determined by indexes.
     * @param i the desired row index
     * @param j the desired column index
     * @param p a <code>Piece</code> object indicating which piece to set
     */
    public static void setPiece(int i, int j, Piece p) {
        board[i][j] = p;
    }

    /**
     * Sets piece on the baord at a given spot. Determined by a <code>Point</code> object.
     * @param po a <code>Point</code> object indicating where to set the piece
     * @param p a <code>Piece</code> object indicating which piece to set
     */
    public static void setPiece(Point po, Piece p) {
        int i = po.i;
        int j = po.j;
        board[i][j] = p;
    }

    /**
     * Takes an input from a player in chess notation. Then, converts to standard
     * index notation in order for the program to be simpler in its calculations.
     * Stores converted notation in <code>Point</code> objects.
     * @return an array of three <code>Point</code> objects
     */
    public static Point[] getInputAndConvert() {
        String input = scan.nextLine();
        if (input.equals("resign")) return new Point[] {new Point("resign")};
        int i1 = 8 - Integer.valueOf(input.substring(1, 2));
        int j1 = ((int) input.charAt(0)) - 97;
        int i2 = 8 - Integer.valueOf(input.substring(4, 5));
        int j2 = ((int) input.charAt(3)) - 97;
        Point[] conversion;
        if (input.length() == 7) { // if the length is 7, it initializes a promotion field in friendly piece point
            conversion = new Point[] {new Point(i1, j1, input.substring(6, 7)), new Point(i2, j2), null};
        } else { // otherwise, proceed normally
            conversion = new Point[] {new Point(i1, j1), new Point(i2, j2), null};
        }
        // if a draw request is initiated, initialize the third point as a draw indicator
        if (input.length() > 8) conversion[2] = new Point("draw?");
        return conversion;
    }

    /**
     * Moves a piece at one spot to another spot.
     * @param p1 a <code>Point</code> object representing the source
     * @param p2 a <code>Point</code> object representing the destination
     */
    public static void movePiece(Point p1, Point p2) {
        Piece p = getPiece(p1);
        if (p instanceof WhitePawn && ((WhitePawn)p).enPessant != null && p2.equals(((WhitePawn)p).enPessant)) {
            setPiece(p2, p);
            setPiece(p1, null);
            setPiece(new Point(3, p2.j), null);
            p.setPos(p2.i, p2.j);
            return;
        }
        else if (p instanceof WhitePawn && p2.i-p1.i == -2) { ((WhitePawn)p).fakeDoubleStep = true; ((WhitePawn)p).doubleStep = true;}
        if (p instanceof BlackPawn && ((BlackPawn)p).enPessant != null && p2.equals(((BlackPawn)p).enPessant)) {
            setPiece(p2, p);
            setPiece(p1, null);
            setPiece(new Point(4, p2.j), null);
            p.setPos(p2.i, p2.j);
            return;
        }
        else if (p instanceof BlackPawn && p2.i-p1.i == 2) { ((BlackPawn)p).fakeDoubleStep = true; ((BlackPawn)p).doubleStep = true; }
        setPiece(p2, p);
        setPiece(p1, null);
        p.setPos(p2.i, p2.j);
    }

    /**
     * Promotes a white piece at a spot to a possibly specified piece.
     * @param p a <code>Point</code> object representing the spot of the promoted pawn
     * @param piece a possibly empty string possibly containing the new piece
     */
    public static void promoteWhite(Point p, String piece) {
        String type = piece;
        Piece newPiece;
        if (type.equals("") || type.equals("Q")) {
            newPiece = new WhiteQueen(p.i, p.j);
        } else if (type.equals("R")) {
            newPiece = new WhiteRook(p.i, p.j);
        } else if (type.equals("B")) {
            newPiece = new WhiteBishop(p.i, p.j);
        } else if (type.equals("N")) {
            newPiece = new WhiteKnight(p.i, p.j);
        } else {
            newPiece = new WhiteQueen(p.i, p.j); // default to queen
        }
        Board.setPiece(p, newPiece);
    }

    /**
     * Promotes a black piece at a spot to a possibly specified piece.
     * @param p a <code>Point</code> object representing the spot of the promoted pawn
     * @param piece a possibly empty string possibly containing the new piece
     */
    public static void promoteBlack(Point p, String piece) {
        String type = piece;
        Piece newPiece;
        if (type.equals("") || type.equals("Q")) {
            newPiece = new BlackQueen(p.i, p.j);
        } else if (type.equals("R")) {
            newPiece = new BlackRook(p.i, p.j);
        } else if (type.equals("B")) {
            newPiece = new BlackBishop(p.i, p.j);
        } else if (type.equals("N")) {
            newPiece = new BlackKnight(p.i, p.j);
        } else {
            newPiece = new BlackQueen(p.i, p.j); // default to queen
        }
        Board.setPiece(p, newPiece);
    }

    /**
     * Determines whether or not white is attacking a specified spot.
     * @param po a <code>Point</code> object indicating which spot might be attacked
     * @return <code>true</code> if white is attacking specified spot, and <code>false</code> otherwise
     */
    public static boolean whiteAttackingSpot(Point po) {
        for (Piece p : getAllPieces()) {
            if (p instanceof BlackPiece) continue;
            for (Point t : p.getPossibleMoves()) {
                if (t.equals(po)) return true;
            }
        }
        return false;
    }

    /**
     * Determines whether or not black is attacking a specified spot.
     * @param po a <code>Point</code> object indicating which spot might be attacked
     * @return <code>true</code> if black is attacking specified spot, and <code>false</code> otherwise
     */
    public static boolean blackAttackingSpot(Point po) {
        for (Piece p : getAllPieces()) {
            if (p instanceof WhitePiece) continue;
            for (Point t : p.getPossibleMoves()) {
                if (t.equals(po)) return true;
            }
        }
        return false;
    }

    // CHECK AND CHECKMATE

    /**
     * Determines whether or not white is in check.
     * @return <code>true</code> if white is in check, and <code>false</code> otherwise
     */
    public static boolean whiteInCheck() {
        for (Piece p : getAllPieces()) {
            for (Point t : p.getPossibleTakes()) {
                if (Board.getPiece(t) != null && Board.getPiece(t).toString().equals("wK")) return true;
            }
        }
        return false;
    }

    /**
     * Determines whether or not black is in check.
     * @return <code>true</code> if black is in check, and <code>false</code> otherwise
     */
    public static boolean blackInCheck() {
        for (Piece p : getAllPieces()) {
            for (Point t : p.getPossibleTakes()) {
                if (Board.getPiece(t) != null && Board.getPiece(t).toString().equals("bK")) return true;
            }
        }
        return false;
    }

    /**
     * Determines whether or not white will be in check after a move.
     * @param start a <code>Point</code> object that represents the source of the move
     * @param end a <code>Point</code> object that represents the destination of the move
     * @return <code>true</code> if white will be in check after a move, and <code>false</code> otherwise
     */
    public static boolean whiteWillBeInCheck(Point start, Point end) {
        // FIRST, MOVE EVERYTHING AS IF THE MOVE WILL BE DONE
        Piece temp = null;
        if (getPiece(end) != null) {
            temp = getPiece(end);
        }
        movePiece(start, end);
        calculateAll();
        // THEN, VERIFY THAT EVERYTHING IS FINE. AFTERWARD, UNDO ALL CHANGES.
        if (whiteInCheck()) {
            movePiece(end, start);
            setPiece(end, temp);
            calculateAll();
            return true;            
        }
        movePiece(end, start);
        setPiece(end, temp);
        calculateAll();
        return false;
    }

    /**
     * Determines whether or not black will be in check after a move.
     * @param start a <code>Point</code> object that represents the source of the move
     * @param end a <code>Point</code> object that represents the destination of the move
     * @return <code>true</code> if black will be in check after a move, and <code>false</code> otherwise
     */
    public static boolean blackWillBeInCheck(Point start, Point end) {
        // FIRST, MOVE EVERYTHING AS IF THE MOVE WILL BE DONE
        Piece temp = null;
        if (getPiece(end) != null) {
            temp = getPiece(end);
        }
        movePiece(start, end);
        calculateAll();
        // THEN, VERIFY THAT EVERYTHING IS FINE. AFTERWARD, UNDO ALL CHANGES.
        if (blackInCheck()) {
            movePiece(end, start);
            setPiece(end, temp);
            calculateAll();
            return true;            
        }
        movePiece(end, start);
        setPiece(end, temp);
        calculateAll();
        return false;
    }

    /**
     * Determines whether white king has a safe move.
     * @return <code>true</code> if white king has a safe move, and <code>false</code> otherwise
     */
    private static boolean whiteHasSafeMove() { // ONLY TO BE CALLED WHEN IN CHECK
        for (Piece p : getAllPieces()) {
            if (p instanceof BlackPiece) continue;
            for (int i = 0; i < p.getPossibleMoves().size(); i++) {
                if (!whiteWillBeInCheck(new Point(p.getRow(), p.getCol()), p.getPossibleMoves().get(i))) return true;
            }
            for (int i = 0; i < p.getPossibleTakes().size(); i++) {
                if (!whiteWillBeInCheck(new Point(p.getRow(), p.getCol()), p.getPossibleTakes().get(i))) return true;
            }
        }
        return false;
    }

    /**
     * Determines whether black king has a safe move.
     * @return <code>true</code> if black king has a safe move, and <code>false</code> otherwise
     */
    private static boolean blackHasSafeMove() { // ONLY TO BE CALLED WHEN IN CHECK
        for (Piece p : getAllPieces()) {
            if (p instanceof WhitePiece) continue;
            for (int i = 0; i < p.getPossibleMoves().size(); i++) {
                if (!blackWillBeInCheck(new Point(p.getRow(), p.getCol()), p.getPossibleMoves().get(i))) return true;
            }
            for (int i = 0; i < p.getPossibleTakes().size(); i++) {
                if (!blackWillBeInCheck(new Point(p.getRow(), p.getCol()), p.getPossibleTakes().get(i))) return true;
            }
        }
        return false;
    }

    /**
     * Determines whether white is in checkmate.
     * @return <code>true</code> if white is in checkmate, and <code>false</code> otherwise
     */
    public static boolean whiteInCheckmate() {
        return whiteInCheck() && !whiteHasSafeMove();
    }

    /**
     * Determines whether black is in checkmate.
     * @return <code>true</code> if black is in checkmate, and <code>false</code> otherwise
     */
    public static boolean blackInCheckmate() {
        return blackInCheck() && !blackHasSafeMove();
    }
}
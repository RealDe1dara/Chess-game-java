package chess_game;

import java.util.List;

import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.GameState;
import chess_game.enums.MoveResult;
import chess_game.enums.MoveType;
import chess_game.enums.PieceType;
import chess_game.pieces.Bishop;
import chess_game.pieces.King;
import chess_game.pieces.Knight;
import chess_game.pieces.Pawn;
import chess_game.pieces.Piece;
import chess_game.pieces.Queen;
import chess_game.pieces.Rook;

public class GameController {

    private final Board board;
    private Piece selectedPiece;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private Player currentPlayer;
    private GameState gameState;
    private Move lastMove;
    private final GameAnalyzer analyzer;

    public GameController() {
        this.board = new Board();
        this.whitePlayer = new Player(Color.WHITE);
        this.blackPlayer = new Player(Color.BLACK);
        this.analyzer = new GameAnalyzer(board);
        setupInitialPosition();
        this.currentPlayer = whitePlayer;
        this.gameState = GameState.ACTIVE;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void selectPiece(Piece piece) {
        selectedPiece = piece;
        System.out.println("Selected piece: " + Math.abs(piece.getSquare().getRow() - 8) + "-" + (piece.getSquare().getColumn() + 1));
    }

    public void clearSelection() {
        selectedPiece = null;
    }

    public MoveResult movePiece(Move move) {

        if (move == null || move.getMovedPiece() == null || move.getNewSquare() == null) {
            return MoveResult.INVALID;
        }

        Piece piece = move.getMovedPiece();
        Square target = move.getNewSquare();
        Square oldSquare = piece.getSquare();
        Piece capturedPiece = move.getCapturedPiece();

        if (piece.getColor() != currentPlayer.getColor()) {
            return MoveResult.INVALID;
        }
        boolean valid = piece.getValidMoves(board, lastMove).stream()
                .anyMatch(m -> m.getNewSquare() == move.getNewSquare() && m.getMovedPiece() == move.getMovedPiece());
        if (!valid) {
            return MoveResult.INVALID;
        }

        if (capturedPiece != null) {
            if (capturedPiece.getColor() == Color.BLACK) {
                blackPlayer.removePiece(capturedPiece);
            } else {
                whitePlayer.removePiece(capturedPiece);
            }

            if (move.hasType(MoveType.EN_PASSANT)) {
                Square capturedSquare = capturedPiece.getSquare();
                capturedSquare.setPiece(null);
            }
        }

        if (move.hasType(MoveType.CASTLING)) {
            castle(oldSquare, target);
        }

        oldSquare.setPiece(null);
        target.setPiece(piece);
        piece.setSquare(target);

        piece.onMove();

        lastMove = move;

        if (capturedPiece != null) {
            System.out.println("Captured: " + capturedPiece.getClass().getSimpleName()
                    + " at " + (8 - target.getRow()) + "-" + (target.getColumn() + 1));
        } else {
            System.out.println("Moved to: " + (8 - target.getRow()) + "-" + (target.getColumn() + 1));
        }

        if (move.hasType(MoveType.PROMOTION)) {
            return MoveResult.PROMOTION_PENDING;
        }
        switchCurrentPlayer();
        updateGameState();

        return (capturedPiece != null) ? MoveResult.CAPTURED : MoveResult.MOVED;
    }

    private void updateGameState() {
        King king = currentPlayer.getKing();
        if (king == null) {
            gameState = GameState.ACTIVE;
            return;
        }
        if (analyzer.isSquareAttacked(king.getSquare(), getOpponentPlayer(), lastMove)) {
            gameState = GameState.CHECK;
            System.out.println("CHECK");
            return;
        }
    }

    public Move getLastMove() {
        return lastMove;
    }

    public Piece promotePawn(Pawn pawn, PieceType promotionType) {
        Square square = pawn.getSquare();
        Piece newPiece;
        switch (promotionType) {
            case QUEEN ->
                newPiece = new Queen(pawn.getColor(), square);
            case ROOK ->
                newPiece = new Rook(pawn.getColor(), square);
            case BISHOP ->
                newPiece = new Bishop(pawn.getColor(), square);
            case KNIGHT ->
                newPiece = new Knight(pawn.getColor(), square);
            default ->
                throw new IllegalArgumentException("Unknown promotion type");

        }

        square.setPiece(newPiece);
        if (pawn.getColor() == Color.WHITE) {
            whitePlayer.removePiece(pawn);
            whitePlayer.addPiece(newPiece);
        } else {
            blackPlayer.removePiece(pawn);
            blackPlayer.addPiece(newPiece);
        }
        switchCurrentPlayer();
        updateGameState();
        return newPiece;
    }

    public Move findValidMove(Piece piece, Square target) {

        List<Move> validMoves = piece.getValidMoves(board, lastMove);

        for (Move move : validMoves) {
            if (move.getNewSquare() == target) {
                return move;
            }
        }

        return null;
    }

    private void castle(Square oldSquare, Square target) {
        int row = oldSquare.getRow();
        int diff = target.getColumn() - oldSquare.getColumn();

        if (diff == 2) {
            Square rookFrom = board.getSquare(row, 7);
            Square rookTo = board.getSquare(row, 5);

            Rook rook = (Rook) rookFrom.getPiece();
            rookFrom.setPiece(null);
            rookTo.setPiece(rook);
            rook.setSquare(rookTo);

        } else if (diff == -2) {
            Square rookFrom = board.getSquare(row, 0);
            Square rookTo = board.getSquare(row, 3);

            Rook rook = (Rook) rookFrom.getPiece();
            rookFrom.setPiece(null);
            rookTo.setPiece(rook);
            rook.setSquare(rookTo);
        }
    }

    private void switchCurrentPlayer() {
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponentPlayer() {
        return (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    public Board getBoard() {
        return board;
    }

    private void setupInitialPosition() {
        //White
        for (int col = 0; col < 8; col++) {
            Square pawnSquare = board.getSquare(6, col);
            Pawn pawn = new Pawn(Color.WHITE, pawnSquare);
            Square square = board.getSquare(7, col);
            Piece piece = null;
            switch (col) {
                case 0, 7 ->
                    piece = new Rook(Color.WHITE, square);
                case 1, 6 ->
                    piece = new Knight(Color.WHITE, square);
                case 2, 5 ->
                    piece = new Bishop(Color.WHITE, square);
                case 3 ->
                    piece = new Queen(Color.WHITE, square);
                case 4 ->
                    piece = new King(Color.WHITE, square);
            }

            if (piece != null) {
                square.setPiece(piece);
                whitePlayer.addPiece(piece);

                pawnSquare.setPiece(pawn);
                whitePlayer.addPiece(pawn);
            }
        }
        // Black 
        for (int col = 0; col < 8; col++) {
            Square pawnSquare = board.getSquare(1, col);
            Pawn pawn = new Pawn(Color.BLACK, pawnSquare);
            Piece piece = null;
            Square square = board.getSquare(0, col);
            switch (col) {
                case 0, 7 ->
                    piece = new Rook(Color.BLACK, square);
                case 1, 6 ->
                    piece = new Knight(Color.BLACK, square);
                case 2, 5 ->
                    piece = new Bishop(Color.BLACK, square);
                case 3 ->
                    piece = new Queen(Color.BLACK, square);
                case 4 ->
                    piece = new King(Color.BLACK, square);
            }
            if (piece != null) {
                square.setPiece(piece);
                blackPlayer.addPiece(piece);
            }
            pawnSquare.setPiece(pawn);
            blackPlayer.addPiece(pawn);
        }
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

}

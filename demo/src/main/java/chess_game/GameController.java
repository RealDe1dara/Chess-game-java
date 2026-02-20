package chess_game;

import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.GameState;
import chess_game.enums.MoveResult;
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

    public GameController() {
        this.board = new Board();
        this.whitePlayer = new Player(Color.WHITE);
        this.blackPlayer = new Player(Color.BLACK);
        setupInitialPosition();
        this.currentPlayer = whitePlayer;
        this.gameState = GameState.ACTIVE;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void selectPiece(Piece piece) {
        selectedPiece = piece;
    }

    public void clearSelection() {
        selectedPiece = null;
    }

    public MoveResult movePiece(Piece piece, Square target) {
        if (piece == null || target == null) {
            return MoveResult.INVALID;
        }

        if (!piece.getValidMoves(board).contains(target) || piece.getColor() != currentPlayer.getColor()) {
            return MoveResult.INVALID;
        }

        Square oldSquare = piece.getSquare();
        Piece targetPiece = target.getPiece();
        if(targetPiece!=null){
            if(targetPiece.getColor() == Color.BLACK){
                blackPlayer.removePiece(targetPiece);
            }
            else{
                whitePlayer.removePiece(targetPiece);
            }
        }
        oldSquare.setPiece(null);
        target.setPiece(piece);
        piece.setSquare(target);
        switchCurrentPlayer();

        return (targetPiece != null) ? MoveResult.CAPTURED : MoveResult.MOVED;
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

    public Board getBoard() {
        return board;
    }

    private void setupInitialPosition() {
        //White
        for (int col = 0; col < 8; col++) {
            Square square = board.getSquare(7, col);
            Square pawnSquare = board.getSquare(6, col);
            Piece piece = null;
            Pawn pawn = new Pawn(Color.WHITE, pawnSquare);
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
            }
            pawnSquare.setPiece(pawn);
            whitePlayer.addPiece(pawn);
        }
        // Black 
        for (int col = 0; col < 8; col++) {
            Square square = board.getSquare(0, col);
            Square pawnSquare = board.getSquare(1, col);
            Piece piece = null;
            Pawn pawn = new Pawn(Color.BLACK, pawnSquare);
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

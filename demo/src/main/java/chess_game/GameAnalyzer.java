package chess_game;

import java.util.List;

import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.pieces.Piece;

public class GameAnalyzer {

    private final Board board;

    public GameAnalyzer(Board board) {
        this.board = board;

    }

    public boolean isSquareAttacked(Square square, Player opponentPlayer, Move lastMove) {
        List<Piece> opponentPieces = opponentPlayer.getPieces();
        for (Piece piece : opponentPieces) {
            for (Move move : piece.getValidMoves(board, lastMove)) {
                if (square == move.getNewSquare()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckMate(Square square, Player opponentPlayer, Move lastMove) {
        List<Piece> opponentPieces = opponentPlayer.getPieces();
        for (Piece piece : opponentPieces) {
            for (Move move : piece.getValidMoves(board, lastMove)) {
                if (square == move.getNewSquare()) {
                    return true;
                }
            }
        }
        return false;
    }
}

package chess_game;

import java.util.ArrayList;
import java.util.List;

import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.MoveType;
import chess_game.pieces.Bishop;
import chess_game.pieces.Knight;
import chess_game.pieces.Piece;

public class GameAnalyzer {

    private final Board board;

    public GameAnalyzer(Board board) {
        this.board = board;

    }

    public boolean isSquareAttacked(Square square, Player opponentPlayer, Move lastMove) {
        List<Piece> opponentPieces = opponentPlayer.getPieces();
        for (Piece piece : opponentPieces) {
            if (piece.getSquare() == null) {
                continue;
            }
            for (Move move : piece.getValidMoves(board, lastMove)) {
                if (square == move.getNewSquare()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(Player current, Player opponent, Move lastMove) {

        if (isSquareAttacked(current.getKing().getSquare(), opponent, lastMove)) {
            List<Move> allLegalMoves = new ArrayList<>();
            for (Piece piece : current.getPieces()) {
                allLegalMoves.addAll(getLegalMoves(piece, current, opponent, lastMove));
            }
            if (allLegalMoves.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public boolean isDraw(Player current, Player opponent, Move lastMove) {

        if (!isSquareAttacked(current.getKing().getSquare(), opponent, lastMove)) {
            List<Move> allLegalMoves = new ArrayList<>();
            for (Piece piece : current.getPieces()) {
                allLegalMoves.addAll(getLegalMoves(piece, current, opponent, lastMove));
            }
            if (allLegalMoves.isEmpty()) {
                return true;
            }
        }

        List<Piece> currentPieces = current.getPieces();
        List<Piece> opponentPieces = opponent.getPieces();

        return isInsufficientMaterial(currentPieces, opponentPieces);
    }

    private boolean isInsufficientMaterial(List<Piece> currentPieces, List<Piece> opponentPieces) {
        int currentSize = currentPieces.size();
        int opponentSize = opponentPieces.size();

        if (currentSize == 1 && opponentSize == 1) {
            return true;
        }
        if (currentSize == 1 && opponentSize == 2) {
            for (Piece piece : opponentPieces) {
                if (piece instanceof Bishop || piece instanceof Knight) {
                    return true;
                }
            }
        }
        if (currentSize == 2 && opponentSize == 1) {
            for (Piece piece : currentPieces) {
                if (piece instanceof Bishop || piece instanceof Knight) {
                    return true;
                }
            }
        }
        if (currentSize == 2 && opponentSize == 2) {
            Bishop currentBishop = null;
            Bishop opponentBishop = null;
            for (Piece piece : currentPieces) {
                if (piece instanceof Bishop b) {
                    currentBishop = b;
                }
            }
            for (Piece piece : opponentPieces) {
                if (piece instanceof Bishop b) {
                    opponentBishop = b;
                }
            }
            if (currentBishop != null && opponentBishop != null) {
                if (currentBishop.getColor() == opponentBishop.getColor()) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Move> getLegalMoves(Piece piece, Player current, Player opponent, Move lastMove) {
        List<Move> legalMoves = new ArrayList<>();

        for (Move move : piece.getValidMoves(board, lastMove)) {

            simulateMove(move);

            boolean kingInCheck = isSquareAttacked(current.getKing().getSquare(), opponent, lastMove);

            undoMove(move);

            if (!kingInCheck) {
                if (move.hasType(MoveType.CASTLING)) {
                    if (isSquareAttacked(current.getKing().getSquare(), opponent, lastMove)) {
                        continue;
                    }
                    int kingCol = move.getOldSquare().getColumn();
                    int targetCol = move.getNewSquare().getColumn();
                    int direction = (targetCol > kingCol) ? 1 : -1;
                    int row = move.getOldSquare().getRow();
                    boolean passesThroughAttack = false;
                    for (int col = kingCol + direction; col != targetCol; col += direction) {
                        Square intermediate = board.getSquare(row, col);
                        if (isSquareAttacked(intermediate, opponent, lastMove)) {
                            passesThroughAttack = true;
                            break;
                        }
                    }
                    if (passesThroughAttack) {
                        continue;
                    }
                }
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }

    private void simulateMove(Move move) {
        move.getOldSquare().setPiece(null);
        move.getNewSquare().setPiece(move.getMovedPiece());
        move.getMovedPiece().setSquare(move.getNewSquare());

        if (move.getCapturedPiece() != null) {
            if (move.hasType(MoveType.EN_PASSANT)) {
                Square capturedSquare = move.getCapturedPiece().getSquare();
                if (capturedSquare != null) {
                    capturedSquare.setPiece(null);
                }
            }
            move.getCapturedPiece().setSquare(null);
        }
        move.getMovedPiece().onMove();
    }

    private void undoMove(Move move) {
        move.getMovedPiece().setSquare(move.getOldSquare());
        move.getOldSquare().setPiece(move.getMovedPiece());

        if (move.hasType(MoveType.EN_PASSANT) && move.getCapturedPiece() != null) {
            move.getNewSquare().setPiece(null);
            Square epSquare = board.getSquare(move.getOldSquare().getRow(), move.getNewSquare().getColumn());
            epSquare.setPiece(move.getCapturedPiece());
            move.getCapturedPiece().setSquare(epSquare);
        } else {
            move.getNewSquare().setPiece(move.getCapturedPiece());
            if (move.getCapturedPiece() != null) {
                move.getCapturedPiece().setSquare(move.getNewSquare());
            }
        }
        move.getMovedPiece().undoOnMove();
    }
}

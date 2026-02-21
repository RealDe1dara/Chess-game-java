package chess_game.actions;

import chess_game.board.Square;
import chess_game.enums.MoveType;
import chess_game.pieces.Piece;

public class Move {

    private final Piece movedPiece;
    private final Piece capturedPiece;
    private final Square oldSquare;
    private final Square newSquare;
    private final MoveType type;

    public Move(Piece movedPiece, Piece capturedPiece, Square oldSquare, Square newSquare, MoveType type) {
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.oldSquare = oldSquare;
        this.newSquare = newSquare;
        this.type = type;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Square getOldSquare() {
        return oldSquare;
    }

    public Square getNewSquare() {
        return newSquare;
    }

    public MoveType getType() {
        return type;
    }
}

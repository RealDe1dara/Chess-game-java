package chess_game.actions;

import java.util.Arrays;
import java.util.EnumSet;

import chess_game.board.Square;
import chess_game.enums.MoveType;
import chess_game.pieces.Piece;

public class Move {

    private final Piece movedPiece;
    private final Piece capturedPiece;
    private final Square oldSquare;
    private final Square newSquare;
    private final EnumSet<MoveType> types;

    public Move(Piece movedPiece, Piece capturedPiece, Square oldSquare, Square newSquare, MoveType... moveTypes) {
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.oldSquare = oldSquare;
        this.newSquare = newSquare;
        this.types = EnumSet.noneOf(MoveType.class);
        this.types.addAll(Arrays.asList(moveTypes));
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

    public boolean hasType(MoveType type) {
        return types.contains(type);
    }
}

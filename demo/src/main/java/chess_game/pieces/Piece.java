package chess_game.pieces;

import java.util.List;

import chess_game.Visualizable;
import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;

public abstract class Piece implements Visualizable {

    private final Color color;
    private Square square;

    public Piece(Color color, Square square) {
        this.square = square;
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    @Override
    public abstract String getSrc();

    public abstract List<Move> getValidMoves(Board board, Move lastMove);

    public void onMove() {
    }
}

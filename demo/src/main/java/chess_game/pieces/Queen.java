package chess_game.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveDirection;
import chess_game.enums.MoveType;

public class Queen extends Piece {

    private final int distance;
    private List<MoveDirection> moveTypes;

    public Queen(Color color, Square square) {
        super(color, square);
        this.moveTypes = List.of(MoveDirection.VERTICAL, MoveDirection.HORIZONTAL);
        this.distance = 7;
    }

    @Override
    public List<Move> getValidMoves(Board board, Move lastMove) {
        List<Move> validMoves = new ArrayList<>();

        int row = getSquare().getRow();
        int column = getSquare().getColumn();

        int[][] directions = {
            {1, 1}, // down - right
            {1, -1}, // down - left
            {-1, 1}, // up - right
            {-1, -1}, // up - left
            {1, 0}, // down
            {-1, 0}, // up
            {0, 1}, // right
            {0, -1} // left
        };

        for (int[] dir : directions) {
            for (int i = 1; i <= distance; i++) {

                Square target = board.getSquare(row + i * dir[0], column + i * dir[1]);

                if (target == null) {
                    break;
                }

                if (target.getPiece() == null) {
                        validMoves.add(new Move(this, null, this.getSquare(), target, MoveType.NORMAL));
                } else {
                    if (target.getPiece().getColor() != this.getColor()) {
                        validMoves.add(new Move(this, target.getPiece(), this.getSquare(), target, MoveType.CAPTURE));
                    }
                    break;
                }

            }
        }
        return validMoves;
    }

    @Override
    public String getSrc() {
        return (getColor() == Color.WHITE) ? "/img/white-queen.png"
                : "/img/black-queen.png";
    }
}

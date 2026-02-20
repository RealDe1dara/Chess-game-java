package chess_game.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveType;

public class Bishop extends Piece {

    private final int distance;
    private List<MoveType> moveTypes;

    public Bishop(Color color, Square square) {
        super(color, square);
        this.moveTypes = List.of(MoveType.VERTICAL, MoveType.HORIZONTAL);
        this.distance = 7;
    }

    @Override
    public List<Square> getValidMoves(Board board) {
        List<Square> validMoves = new ArrayList<>();

        int row = getSquare().getRow();
        int column = getSquare().getColumn();

        int[][] directions = {
            {1, 1}, // down - right
            {1, -1}, // down - left
            {-1, 1}, // up - right
            {-1, -1} // up - left
        };

        for (int[] dir : directions) {
            for (int i = 1; i <= distance; i++) {

                Square target = board.getSquare(row + i * dir[0], column + i * dir[1]);

                if (target == null) {
                    break;
                }

                if (target.getPiece() == null) {
                    validMoves.add(target);
                } else {
                    if (target.getPiece().getColor() != this.getColor()) {
                        validMoves.add(target);
                    }
                    break;
                }

            }
        }
        return validMoves;
    }

    @Override
    public String getSrc() {
        return (getColor() == Color.WHITE) ? "/img/white-bishop.png"
                : "/img/black-bishop.png";
    }
}

package chess_game.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveType;

public class Rook extends Piece {

    private final int distance;
    private List<MoveType> moveTypes;

    public Rook(Color color, Square square) {
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
        return (getColor() == Color.WHITE) ? "/img/white-rook.png"
                : "/img/black-rook.png";
    }
}

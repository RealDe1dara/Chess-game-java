package chess_game.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveType;

public class Knight extends Piece {

    private List<MoveType> moveTypes;

    public Knight(Color color, Square square) {
        super(color, square);
        this.moveTypes = List.of(MoveType.L_SHAPE);
    }

    @Override
    public List<Square> getValidMoves(Board board) {
        List<Square> validMoves = new ArrayList<>();

        int row = getSquare().getRow();
        int column = getSquare().getColumn();

        int[][] moves = {
            {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
            {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        for (int[] move : moves) {

            Square target = board.getSquare(row + move[0], column + move[1]);

            if (target == null) {
                continue;
            }

            if (target.getPiece() == null || target.getPiece().getColor() != getColor()) {
                validMoves.add(target);
            }

        }
        return validMoves;
    }

    @Override
    public String getSrc() {
        return (getColor() == Color.WHITE) ? "/img/white-knight.png"
                : "/img/black-knight.png";
    }
}

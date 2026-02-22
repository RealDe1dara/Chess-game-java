package chess_game.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveType;
import chess_game.enums.PieceType;

public class Bishop extends Piece {


    public Bishop(Color color, Square square) {
        super(color, square, PieceType.BISHOP);
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
            {-1, -1} // up - left
        };

        for (int[] dir : directions) {
            for (int i = 1; ; i++) {

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
        return (getColor() == Color.WHITE) ? "/img/white-bishop.png"
                : "/img/black-bishop.png";
    }
}

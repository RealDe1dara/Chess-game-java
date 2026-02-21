package chess_game.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveType;
import chess_game.enums.PieceType;

public class Knight extends Piece {


    public Knight(Color color, Square square) {
        super(color, square,PieceType.KNIGHT);
    }

    @Override
    public List<Move> getValidMoves(Board board, Move lastMove) {
        List<Move> validMoves = new ArrayList<>();

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

            if (target.getPiece() == null) {
                validMoves.add(new Move(this, null, this.getSquare(), target, MoveType.NORMAL));
            } else {
                if (target.getPiece().getColor() != this.getColor()) {
                    validMoves.add(new Move(this, target.getPiece(), this.getSquare(), target, MoveType.CAPTURE));
                }
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

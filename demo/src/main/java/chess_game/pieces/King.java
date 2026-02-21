package chess_game.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveType;
import chess_game.enums.PieceType;

public class King extends Piece {

    private final int distance;
    private boolean isFirstMove = true;

    public King(Color color, Square square) {
        super(color, square, PieceType.KING);
        this.distance = 1;
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

        if (this.isFirstMove) {

            // King side
            Square rookSquare = board.getSquare(row, 7);
            if (rookSquare != null && rookSquare.getPiece() instanceof Rook rook
                    && rook.getIsFirstMove()) {

                Square fSquare = board.getSquare(row, column + 1);
                Square gSquare = board.getSquare(row, column + 2);

                if (fSquare.getPiece() == null && gSquare.getPiece() == null) {
                    validMoves.add(new Move(this, null, this.getSquare(), gSquare, MoveType.CASTLING));
                }
            }

            // Queen side
            rookSquare = board.getSquare(row, 0);
            if (rookSquare != null && rookSquare.getPiece() instanceof Rook rook
                    && rook.getIsFirstMove()) {

                Square dSquare = board.getSquare(row, column - 1);
                Square cSquare = board.getSquare(row, column - 2);
                Square bSquare = board.getSquare(row, column - 3);

                if (dSquare.getPiece() == null
                        && cSquare.getPiece() == null
                        && bSquare.getPiece() == null) {

                    validMoves.add(new Move(this, null, this.getSquare(), cSquare, MoveType.CASTLING));
                }
            }
        }

        return validMoves;
    }

    @Override
    public void onMove() {
        this.isFirstMove = false;
    }

    @Override
    public String getSrc() {
        return (getColor() == Color.WHITE) ? "/img/white-king.png"
                : "/img/black-king.png";
    }
}

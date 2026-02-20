package chess_game.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveType;

public class Pawn extends Piece {

    private boolean isFirstMove = true;
    private int distance;
    private List<MoveType> moveTypes;

    public Pawn(Color color, Square square) {
        super(color, square);
        this.moveTypes = List.of(MoveType.VERTICAL, MoveType.DIAGONAL);
        this.distance = 1;
    }

    @Override
    public List<Square> getValidMoves(Board board) {
        List<Square> validMoves = new ArrayList<>();

        int dir = getColor().getForwardDir();
        int row = getSquare().getRow();
        int column = getSquare().getColumn();

        Square forward = board.getSquare(row + dir, column);

        if (forward != null && forward.getPiece() == null) {
            validMoves.add(forward);

            if (isFirstMove) {
                Square twoSteps = board.getSquare(row + 2 * dir, column);
                if (twoSteps != null && twoSteps.getPiece() == null) {
                    validMoves.add(twoSteps);
                }
            }
        }
        Square forwardLeft = board.getSquare(row + dir, column - 1);
        Square forwardRight = board.getSquare(row + dir, column + 1);

        if (forwardLeft != null && forwardLeft.getPiece() != null
                && forwardLeft.getPiece().getColor() != this.getColor()) {
            validMoves.add(forwardLeft);
        }

        if (forwardRight != null && forwardRight.getPiece() != null
                && forwardRight.getPiece().getColor() != this.getColor()) {
            validMoves.add(forwardRight);
        }

        return validMoves;
    }

    @Override
    public String getSrc() {
        return (getColor() == Color.WHITE) ? "/img/white-pawn.png"
                : "/img/black-pawn.png";
    }

    public void setIsFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }
}

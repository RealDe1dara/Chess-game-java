package chess_game.pieces;

import java.util.ArrayList;
import java.util.List;

import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveType;
import chess_game.enums.PieceType;

public class Pawn extends Piece {

    private boolean isFirstMove = true;

    public Pawn(Color color, Square square) {
        super(color, square, PieceType.PAWN);
    }

    @Override
    public List<Move> getValidMoves(Board board, Move lastMove) {
        List<Move> validMoves = new ArrayList<>();

        int dir = getColor().getForwardDir();
        int row = getSquare().getRow();
        int column = getSquare().getColumn();

        Square forward = board.getSquare(row + dir, column);
        int promotionRow = (this.getColor() == Color.WHITE) ? 0 : 7;
        if (forward != null && forward.getPiece() == null) {
            if (promotionRow == forward.getRow()) {
                validMoves.add(new Move(this, null, this.getSquare(), forward, MoveType.PROMOTION));
            } else {
                validMoves.add(new Move(this, null, this.getSquare(), forward, MoveType.NORMAL));
            }
            if (isFirstMove) {
                Square twoSteps = board.getSquare(row + 2 * dir, column);
                if (twoSteps != null && twoSteps.getPiece() == null) {
                    validMoves.add(new Move(this, null, this.getSquare(), twoSteps, MoveType.PAWN_DOUBLE));
                }
            }
        }
        Square forwardLeft = board.getSquare(row + dir, column - 1);
        Square forwardRight = board.getSquare(row + dir, column + 1);

        if (forwardLeft != null && forwardLeft.getPiece() != null
                && forwardLeft.getPiece().getColor() != this.getColor()) {
            if (forwardLeft.getRow() == promotionRow) {
                validMoves.add(new Move(this, forwardLeft.getPiece(), this.getSquare(), forwardLeft, MoveType.CAPTURE, MoveType.PROMOTION));
            } else {
                validMoves.add(new Move(this, forwardLeft.getPiece(), this.getSquare(), forwardLeft, MoveType.CAPTURE));
            }
        }

        if (forwardRight != null && forwardRight.getPiece() != null
                && forwardRight.getPiece().getColor() != this.getColor()) {
            if (forwardRight.getRow() == promotionRow) {
                validMoves.add(new Move(this, forwardRight.getPiece(), this.getSquare(), forwardRight, MoveType.CAPTURE, MoveType.PROMOTION));
            } else {
                validMoves.add(new Move(this, forwardRight.getPiece(), this.getSquare(), forwardRight, MoveType.CAPTURE));
            }
        }

        if (lastMove != null
                && lastMove.hasType(MoveType.PAWN_DOUBLE)
                && lastMove.getMovedPiece().getColor() != this.getColor()
                && lastMove.getMovedPiece() instanceof Pawn) {

            Square enemyPawnSquare = lastMove.getNewSquare();

            if (this.getSquare().getRow() == enemyPawnSquare.getRow()
                    && Math.abs(this.getSquare().getColumn() - enemyPawnSquare.getColumn()) == 1) {

                Square enPassantTarget = board.getSquare(enemyPawnSquare.getRow() + dir, enemyPawnSquare.getColumn());

                if (enPassantTarget != null && enPassantTarget.getPiece() == null) {
                    validMoves.add(new Move(this, enemyPawnSquare.getPiece(), this.getSquare(), enPassantTarget, MoveType.EN_PASSANT, MoveType.CAPTURE
                    ));
                }
            }
        }

        return validMoves;
    }

    @Override
    public String getSrc() {
        return (getColor() == Color.WHITE) ? "/img/white-pawn.png"
                : "/img/black-pawn.png";
    }

    @Override
    public void onMove() {
        this.isFirstMove = false;
    }
    
    @Override
    public void undoOnMove() {
        this.isFirstMove = true;
    }
    
}

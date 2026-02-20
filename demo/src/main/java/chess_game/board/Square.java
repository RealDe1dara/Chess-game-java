package chess_game.board;

import chess_game.Visualizable;
import chess_game.enums.Color;
import chess_game.pieces.Piece;

public class Square implements Visualizable {

    private final int row;
    private final int column;
    private final Color color;
    private Piece piece;

    public Square(int row, int column) {
        this.row = row;
        this.column = column;
        this.piece = null;
        if ((row + column) % 2 == 0) {
            this.color = Color.WHITE;
        } else {
            this.color = Color.BLACK;
        }
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public String getSrc() {
        return (getColor() == Color.WHITE) ? "/img/white-square.png"
                : "/img/black-square.png";
    }
}

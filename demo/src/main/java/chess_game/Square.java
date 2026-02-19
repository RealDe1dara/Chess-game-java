package chess_game;

public class Square {

    private final int row;
    private final int column;
    private final Color color;

    public Square(int row, int column) {
        this.row = row;
        this.column = column;

        if ((row + column) % 2 == 0) {
            this.color = Color.WHITE;
        } else {
            this.color = Color.BLACK;
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Color getColor() {
        return color;
    }
}

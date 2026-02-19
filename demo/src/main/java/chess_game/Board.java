package chess_game;

public class Board {

    private final int rows = 8;
    private final int columns = 8;
    private final Square[][] board;

    public Board() {
        this.board = new Square[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }

    // public Square[][] getBoard() {
    //     return board;
    // }
    public Square getSquare(int row, int column) {
        return board[row][column];
    }
    
}

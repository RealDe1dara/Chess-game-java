package chess_game.board;

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
        if (row < 0 || row >= 8 || column < 0 || column >= 8) {
            return null;
        }
        return board[row][column];
    }

    // public Piece getSelectedPiece() {
    //     for (int i = 0; i < rows; i++) {
    //         for (int j = 0; j < columns; j++) {
    //             if (board[i][j].getPiece().getIsSelected()) {
    //                 return board[i][j].getPiece();
    //             }
    //         }
    //     }
    //     return null;
    // }
}

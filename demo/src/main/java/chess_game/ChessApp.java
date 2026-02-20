package chess_game;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.Color;
import chess_game.enums.MoveResult;
import chess_game.pieces.Pawn;
import chess_game.pieces.Piece;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ChessApp extends Application {

    private static final int SIZE = 72;

    @Override
    public void start(Stage primaryStage) {

        GridPane grid = new GridPane();
        GameController gameController = new GameController();
        Board board = gameController.getBoard();
        Player whitePlayer = gameController.getWhitePlayer();
        Player blackPlayer = gameController.getBlackPlayer();

        Map<Piece, ImageView> pieceViews = new HashMap<>();
        StackPane[][] cells = new StackPane[8][8];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                Square square = board.getSquare(row, col);

                StackPane cell = new StackPane();
                cells[row][col] = cell;

                Image squareImage = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream(square.getSrc())
                ));

                ImageView squareView = new ImageView(squareImage);
                squareView.setFitWidth(SIZE);
                squareView.setFitHeight(SIZE);

                cell.getChildren().add(squareView);

                if (square.getPiece() != null) {
                    Piece piece = square.getPiece();

                    Image pieceImage = new Image(Objects.requireNonNull(
                            getClass().getResourceAsStream(piece.getSrc())
                    ));

                    ImageView pieceView = new ImageView(pieceImage);
                    pieceView.setFitWidth(SIZE);
                    pieceView.setFitHeight(SIZE);

                    cell.getChildren().add(pieceView);
                    pieceViews.put(piece, pieceView);
                }

                final int r = row;
                final int c = col;

                cell.setOnMouseClicked(e -> {

                    Square clickedSquare = board.getSquare(r, c);
                    Piece clickedPiece = clickedSquare.getPiece();
                    Piece selected = gameController.getSelectedPiece();

                    if (selected == null) {
                        if (clickedPiece != null) {
                            gameController.selectPiece(clickedPiece);
                        }
                        return;
                    }

                    if (clickedPiece != null
                            && clickedPiece.getColor() == selected.getColor()) {

                        gameController.selectPiece(clickedPiece);
                        return;
                    }

                    if (clickedPiece != null) {
                        if (!selected.getValidMoves(board).contains(clickedSquare)) {
                            gameController.clearSelection();

                            return;
                        }
                    }
                    Square oldSquare = selected.getSquare();
                    int oldRow = oldSquare.getRow();
                    int oldCol = oldSquare.getColumn();

                    System.out.println("Attempting move from (" + oldRow + "," + oldCol
                            + ") to (" + r + "," + c + ")");

                    MoveResult moved = gameController.movePiece(selected, clickedSquare);

                    switch (moved) {
                        case INVALID -> {
                            System.out.println("Invalid");
                            gameController.clearSelection();
                        }
                        default -> {
                            System.out.println("Move successful: " + moved);

                            ImageView pieceView = pieceViews.get(selected);
                            cells[oldRow][oldCol].getChildren().remove(pieceView);
// Remove from whatever parent it currently has
                            if (pieceView.getParent() != null) {
                                ((StackPane) pieceView.getParent()).getChildren().remove(pieceView);
                                System.out.println("Removed piece from old parent");
                            }

// Add to new cell
                            cells[r][c].getChildren().add(pieceView);
                            System.out.println("Added piece to new cell (" + r + "," + c + ")");
                            if (selected instanceof Pawn movedPawn) {
                                movedPawn.setIsFirstMove(false);
                            }
                            if (moved == MoveResult.CAPTURED && clickedPiece != null) {
                                if (clickedPiece.getColor() == Color.WHITE) {
                                    whitePlayer.removePiece(clickedPiece);
                                } else {
                                    blackPlayer.removePiece(clickedPiece);
                                }
                                ImageView capturedView = pieceViews.get(clickedPiece);
                                if (capturedView != null && capturedView.getParent() != null) {
                                    ((StackPane) capturedView.getParent()).getChildren().remove(capturedView);
                                    System.out.println("Captured piece removed from board UI");
                                }

                                pieceViews.remove(clickedPiece);
                                System.out.println("Captured piece removed from player");
                            }
                            cells[r][c].getChildren().add(pieceView);

                            gameController.clearSelection();
                        }
                    }
                });

                grid.add(cell, col, row);
            }
        }

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package chess_game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import chess_game.actions.Move;
import chess_game.board.Board;
import chess_game.board.Square;
import chess_game.enums.MoveResult;
import chess_game.enums.MoveType;
import chess_game.enums.PieceType;
import chess_game.pieces.Bishop;
import chess_game.pieces.Knight;
import chess_game.pieces.Pawn;
import chess_game.pieces.Piece;
import chess_game.pieces.Queen;
import chess_game.pieces.Rook;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class GameUI {

    private final int squareSize = 72;
    private final GridPane grid;
    private StackPane root;

    private final StackPane[][] cells = new StackPane[8][8];
    private final Map<Piece, ImageView> pieceViews = new HashMap<>();
    private final GameController gameController;

    public GameUI(Board board, GameController gameController) {
        this.grid = new GridPane();
        this.gameController = gameController;

        createBoardView(board);
    }

    public void setRoot(StackPane root) {
        this.root = root;
    }

    private void createBoardView(Board board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                final int finalRow = row;
                final int finalCol = col;
                Square square = board.getSquare(row, col);
                StackPane cell = new StackPane();
                cells[row][col] = cell;
                Image squareImage = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream(square.getSrc())
                ));
                ImageView squareView = new ImageView(squareImage);
                squareView.setFitWidth(squareSize);
                squareView.setFitHeight(squareSize);

                cell.getChildren().add(squareView);

                if (square.getPiece() != null) {
                    Piece piece = square.getPiece();
                    Image pieceImage = new Image(Objects.requireNonNull(
                            getClass().getResourceAsStream(piece.getSrc())
                    ));
                    ImageView pieceView = new ImageView(pieceImage);
                    pieceView.setFitWidth(squareSize);
                    pieceView.setFitHeight(squareSize);
                    cell.getChildren().add(pieceView);
                    pieceViews.put(piece, pieceView);
                }
                cell.setOnMouseClicked(e -> {
                    Piece selected = gameController.getSelectedPiece();
                    Square clickedSquare = board.getSquare(finalRow, finalCol);

                    if (selected == null) {
                        if (clickedSquare.getPiece() != null
                                && clickedSquare.getPiece().getColor() == gameController.getCurrentPlayer().getColor()) {
                            gameController.selectPiece(clickedSquare.getPiece());
                        }
                        return;
                    }
                    if (clickedSquare.getPiece() != null && selected.getColor() == clickedSquare.getPiece().getColor()) {
                        gameController.selectPiece(clickedSquare.getPiece());
                        return;
                    }
                    Piece capturedPiece = clickedSquare.getPiece();

                    // en passant
                    if (selected instanceof Pawn && gameController.getLastMove() != null) {
                        Move lastMove = gameController.getLastMove();
                        if (lastMove.getType() == MoveType.PAWN_DOUBLE
                                && lastMove.getMovedPiece().getColor() != selected.getColor()) {
                            Square enemyPawnSquare = lastMove.getNewSquare();
                            int dir = selected.getColor().getForwardDir();
                            if (clickedSquare == board.getSquare(enemyPawnSquare.getRow() + dir, enemyPawnSquare.getColumn())) {
                                capturedPiece = enemyPawnSquare.getPiece();
                            }
                        }
                    }

                    Move move = gameController.findValidMove(selected, clickedSquare);

                    if (move == null) {
                        gameController.clearSelection();
                        return;
                    }

                    MoveResult result = gameController.movePiece(move);

                    switch (result) {
                        case MOVED, CAPTURED -> {
                            movePieceView(selected, capturedPiece);
                            if (move.getType() == MoveType.CASTLING) {
                                int kingRow = selected.getSquare().getRow();
                                Square rookSquare;
                                if (selected.getSquare().getColumn() == 2) {
                                    rookSquare = board.getSquare(kingRow, 3);
                                } else {
                                    rookSquare = board.getSquare(kingRow, 5);
                                }
                                movePieceView(rookSquare.getPiece(), null);
                            }
                        }
                        case PROMOTION_PENDING -> {
                            movePieceView(selected, null);
                            showPromotionDialog((Pawn) selected);
                        }
                        default -> {
                        }
                    }
                    gameController.clearSelection();

                });

                grid.add(cell, col, row);
            }
        }
    }

    public GridPane getGrid() {
        return grid;
    }

    public void movePieceView(Piece piece, Piece capturedPiece) {
        ImageView pieceView = pieceViews.get(piece);
        if (pieceView == null) {
            return;
        }

        if (pieceView.getParent() != null) {
            ((StackPane) pieceView.getParent()).getChildren().remove(pieceView);
        }
        if (capturedPiece != null) {
            ImageView capturedView = pieceViews.get(capturedPiece);
            if (capturedView != null && capturedView.getParent() != null) {
                ((StackPane) capturedView.getParent()).getChildren().remove(capturedView);
            }
            pieceViews.remove(capturedPiece);
        }

        Square square = piece.getSquare();
        cells[square.getRow()][square.getColumn()].getChildren().add(pieceView);
    }

    private void showPromotionDialog(Pawn pawn) {

        GridPane promotionGrid = new GridPane();
        promotionGrid.setStyle("-fx-background-color: rgba(0,0,0,0.9); -fx-padding: 10;");
        promotionGrid.setAlignment(Pos.CENTER);
        promotionGrid.setVgap(5);

        StackPane.setAlignment(promotionGrid, Pos.CENTER);

        List<Piece> promotionPieces = List.of(
                new Queen(pawn.getColor(), pawn.getSquare()),
                new Rook(pawn.getColor(), pawn.getSquare()),
                new Bishop(pawn.getColor(), pawn.getSquare()),
                new Knight(pawn.getColor(), pawn.getSquare())
        );

        for (int i = 0; i < promotionPieces.size(); i++) {

            Piece piece = promotionPieces.get(i);

            Image image = new Image(
                    Objects.requireNonNull(
                            getClass().getResourceAsStream(piece.getSrc())
                    )
            );

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(squareSize);
            imageView.setFitHeight(squareSize);

            imageView.setOnMouseClicked(e
                    -> finishPromotion(pawn, piece.getType(), promotionGrid)
            );

            promotionGrid.add(imageView, 0, i);
        }

        root.getChildren().add(promotionGrid);
    }

    private void finishPromotion(Pawn pawn, PieceType type, GridPane promotionGrid) {

        root.getChildren().remove(promotionGrid);

        Piece newPiece = gameController.promotePawn(pawn, type);

        ImageView pawnView = pieceViews.get(pawn);
        if (pawnView != null) {
            ((StackPane) pawnView.getParent()).getChildren().remove(pawnView);
            pieceViews.remove(pawn);
        }

        Image pieceImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(newPiece.getSrc())
                )
        );

        ImageView newView = new ImageView(pieceImage);
        newView.setFitWidth(squareSize);
        newView.setFitHeight(squareSize);

        Square square = newPiece.getSquare();
        cells[square.getRow()][square.getColumn()].getChildren().add(newView);

        pieceViews.put(newPiece, newView);

        gameController.clearSelection();
    }
}

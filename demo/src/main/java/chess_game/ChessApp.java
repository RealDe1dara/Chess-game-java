package chess_game;

import java.util.Objects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChessApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();

        Image whiteSquare = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/img/white-square.png"),
                "Missing resource: /img/white-square.png"));

        Image blackSquare = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/img/black-square.png"),
                "Missing resource: /img/black-square.png"));

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Image squareImage = ((row + col) % 2 == 0) ? whiteSquare : blackSquare;

                ImageView squareView = new ImageView(squareImage);
                squareView.setFitWidth(72);
                squareView.setFitHeight(72);

                grid.add(squareView, col, row);
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

package chess_game;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ChessApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        GameController gameController = new GameController();
        GameUI gameUI = new GameUI(gameController.getBoard(), gameController);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");
        root.setPadding(new Insets(70, 200, 70, 40));

        StackPane centerWrapper = new StackPane(gameUI.getGrid());
        gameUI.setRoot(centerWrapper);
        root.setCenter(centerWrapper);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

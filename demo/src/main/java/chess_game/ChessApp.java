package chess_game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChessApp extends Application {


    @Override
    public void start(Stage primaryStage) {

        GameController gameController = new GameController();
        GameUI gameUI = new GameUI(gameController.getBoard(), gameController);

        Scene scene = new Scene(gameUI.getGrid());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chess");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

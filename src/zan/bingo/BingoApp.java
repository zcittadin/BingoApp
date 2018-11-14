package zan.bingo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class BingoApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/zan/bingo/Bingo.fxml"));
		Parent root = (Parent) loader.load();
		final Scene scene = new Scene(root);

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		stage.setScene(scene);
		stage.setTitle("Bingo!");

		stage.setMinHeight(primaryScreenBounds.getHeight());
		stage.setMinWidth(primaryScreenBounds.getWidth());
		stage.setMaximized(true);

		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}

package zan.bingo;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.util.Duration;

public class BingoController implements Initializable {

	@FXML
	Pane bingoPane;

	private static Rectangle2D screenBounds;

	private double centerX;
	private double centerY;
	private boolean isCentered = false;

	Collection<Node> children;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		screenBounds = Screen.getPrimary().getVisualBounds();
		centerX = screenBounds.getWidth() / 2;
		centerY = screenBounds.getHeight() / 2;
		double r = 30.5;
		children = bingoPane.getChildren();
		children.forEach(ch -> {
			if (ch instanceof Button) {
				((Button) ch).setShape(new Circle(r));
				((Button) ch).setMinSize(2 * r, 2 * r);
				((Button) ch).setMaxSize(2 * r, 2 * r);
				((Button) ch).setStyle("-fx-background-color: lightgray; -fx-text-fill: navy;");
			}
		});
	}

	@FXML
	private void translateButton(ActionEvent e) {
		Button bt = (Button) e.getSource();
		children.forEach(ch -> {
			ch.setDisable(true);
			if (ch.getId().equals(bt.getId())) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						ch.setDisable(false);
						if (isCentered)
							toHome(bt);
						else
							toCenter(bt);
					}
				});
			}
		});
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (!isCentered) {
					children.forEach(ch -> {
						ch.setDisable(false);
					});
				}
			}
		});
	}

	private void toCenter(Button bt) {
		isCentered = true;
		bt.toFront();
		TranslateTransition trTrans = new TranslateTransition(Duration.seconds(1), bt);
		trTrans.setFromX(bt.getLayoutBounds().getWidth() - bt.getWidth());
		trTrans.setFromY(bt.getLayoutBounds().getHeight() - bt.getHeight());
		trTrans.setToX((centerX - bt.getWidth()) - bt.getLayoutX());
		trTrans.setToY((centerY - bt.getHeight()) - bt.getLayoutY());
		trTrans.setCycleCount(1);
		trTrans.setAutoReverse(false);
		ScaleTransition scTrans = new ScaleTransition(Duration.seconds(1), bt);
		scTrans.setFromX(1.0);
		scTrans.setToX(9.0);
		scTrans.setFromY(1.0);
		scTrans.setToY(9.0);
		scTrans.setCycleCount(1);
		scTrans.setAutoReverse(false);
		ParallelTransition parTransition = new ParallelTransition();
		parTransition.setNode(bt);
		parTransition.getChildren().addAll(trTrans, scTrans);
		parTransition.setCycleCount(1);
		parTransition.play();
	}

	private void toHome(Button bt) {
		isCentered = false;
		bt.toFront();
		TranslateTransition trTrans = new TranslateTransition(Duration.seconds(1), bt);
		trTrans.setFromX((centerX - bt.getWidth()) - bt.getLayoutX());
		trTrans.setFromY((centerY - bt.getHeight()) - bt.getLayoutY());
		trTrans.setToX(bt.getLayoutBounds().getWidth() - bt.getWidth());
		trTrans.setToY(bt.getLayoutBounds().getHeight() - bt.getHeight());
		trTrans.setCycleCount(1);
		trTrans.setAutoReverse(false);
		ScaleTransition scTrans = new ScaleTransition(Duration.seconds(1), bt);
		scTrans.setFromX(9.0);
		scTrans.setToX(1.0);
		scTrans.setFromY(9.0);
		scTrans.setToY(1.0);
		scTrans.setCycleCount(1);
		scTrans.setAutoReverse(false);
		ParallelTransition parTransition = new ParallelTransition();
		parTransition.setNode(bt);
		parTransition.getChildren().addAll(trTrans, scTrans);
		parTransition.setCycleCount(1);
		parTransition.play();
		bt.setOnAction(null);
		bt.setStyle("-fx-background-color: green; -fx-text-fill: white;");
	}

	@FXML
	private void resetBingo() {
		children.forEach(ch -> {
			if (ch instanceof Button) {
				((Button) ch).setStyle("-fx-background-color: lightgray; -fx-text-fill: navy;");
				((Button) ch).setOnAction(ev -> {
					translateButton(ev);
				});
			}
		});
	}
}

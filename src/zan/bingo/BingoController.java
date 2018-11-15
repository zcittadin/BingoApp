package zan.bingo;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BingoController implements Initializable {

	@FXML
	private Pane bingoPane;

	private static String backgroundColorClear;
	private static String textFillClear;
	private static String backgroundColorMarked;
	private static String textFillMarked;

	private static String BACKGROUND_CLEAR = "background-color-clear";
	private static String BACKGROUND_MARKED = "background-color-marked";
	private static String TEXT_CLEAR = "text-fill-clear";
	private static String TEXT_MARKED = "text-fill-marked";

	private double centerX;
	private double centerY;
	private boolean isCentered = false;

	private Collection<Node> children;
	private Map<Button, Boolean> statusMap = new HashMap<Button, Boolean>();

	private Preferences prefs;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPrefs();
		centerX = Screen.getPrimary().getVisualBounds().getWidth() / 2;
		centerY = Screen.getPrimary().getVisualBounds().getHeight() / 2;
		double r = 30.5;
		children = bingoPane.getChildren();
		children.forEach(ch -> {
			if (ch instanceof Button) {
				((Button) ch).setShape(new Circle(r));
				((Button) ch).setMinSize(2 * r, 2 * r);
				((Button) ch).setMaxSize(2 * r, 2 * r);
				((Button) ch).setStyle(
						"-fx-background-color: " + backgroundColorClear + "; -fx-text-fill: " + textFillClear + ";");
				statusMap.put((Button) ch, false);
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
		if (statusMap.get(bt) == true) {
			cancelMarked(bt);
			return;
		}
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
		bt.setStyle("-fx-background-color: " + backgroundColorMarked + "; -fx-text-fill: " + textFillMarked + ";");
		statusMap.put(bt, true);
	}

	private void cancelMarked(Button bt) {
		Optional<ButtonType> result = makeConfirm("Confirmar cancelamento",
				"Tem certeza que deseja cancelar a bolinha sorteada?");
		if (result.get() == ButtonType.OK) {
			statusMap.put(bt, false);
			bt.setStyle("-fx-background-color: " + backgroundColorClear + "; -fx-text-fill: " + textFillClear + ";");
		}
	}

	@FXML
	private void resetBingo() throws IOException {
		loadPrefs();
		children.forEach(ch -> {
			if (ch instanceof Button) {
				((Button) ch).setStyle(
						"-fx-background-color: " + backgroundColorClear + "; -fx-text-fill: " + textFillClear + ";");
				((Button) ch).setOnAction(ev -> {
					translateButton(ev);
				});
				statusMap.put((Button) ch, false);
			}
		});
	}

	@FXML
	private void openConfig() {
		try {
			Stage stage;
			Parent root;
			stage = new Stage();
			URL url = getClass().getResource("/zan/bingo/Config.fxml");
			FXMLLoader fxmlloader = new FXMLLoader();
			fxmlloader.setLocation(url);
			fxmlloader.setBuilderFactory(new JavaFXBuilderFactory());
			root = (Parent) fxmlloader.load(url.openStream());
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("/zan/bingo/icon.png"));
			((ConfigController) fxmlloader.getController()).setContext(backgroundColorClear, textFillClear,
					backgroundColorMarked, textFillMarked);
			stage.setTitle("Configurações");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(bingoPane.getScene().getWindow());
			stage.setResizable(Boolean.FALSE);
			stage.showAndWait();
			updateColors();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateColors() {
		loadPrefs();
		statusMap.keySet().forEach(bt -> {
			if (statusMap.get(bt) == true) {
				bt.setStyle(
						"-fx-background-color: " + backgroundColorMarked + "; -fx-text-fill: " + textFillMarked + ";");
			} else {
				bt.setStyle(
						"-fx-background-color: " + backgroundColorClear + "; -fx-text-fill: " + textFillClear + ";");
			}
		});
	}

	private void loadPrefs() {
		prefs = Preferences.userRoot().node("config");
		backgroundColorClear = prefs.get(BACKGROUND_CLEAR, "lightgray");
		backgroundColorMarked = prefs.get(BACKGROUND_MARKED, "green");
		textFillClear = prefs.get(TEXT_CLEAR, "navy");
		textFillMarked = prefs.get(TEXT_MARKED, "white");
	}

	private Optional<ButtonType> makeConfirm(String title, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(message);
		Optional<ButtonType> result = alert.showAndWait();
		return result;
	}
}

package zan.bingo;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ConfigController implements Initializable {

	@FXML
	private Button btSampleClear;
	@FXML
	private Button btSampleMarked;
	@FXML
	private ColorPicker clpBackgroundClear;
	@FXML
	private ColorPicker clpTextFillClear;
	@FXML
	private ColorPicker clpBackgroundMarked;
	@FXML
	private ColorPicker clpTextFillMarked;
	@FXML
	private ColorPicker clpBackScreen;
	@FXML
	private Rectangle rectColor;

	private static String BACKGROUND_CLEAR = "background-color-clear";
	private static String BACKGROUND_MARKED = "background-color-marked";
	private static String TEXT_CLEAR = "text-fill-clear";
	private static String TEXT_MARKED = "text-fill-marked";
	private static String BACKGROUND_SCREEN = "background-color-screen";

	private static double r = 60.0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		clpBackgroundClear.getStyleClass().add("split-button");
		clpTextFillClear.getStyleClass().add("split-button");
		clpBackgroundMarked.getStyleClass().add("split-button");
		clpTextFillMarked.getStyleClass().add("split-button");

		clpBackgroundClear.setOnAction(t -> setBackgroundButtonColor(btSampleClear, clpBackgroundClear.getValue()));
		clpTextFillClear.setOnAction(t -> setTextFillColor(btSampleClear, clpTextFillClear.getValue()));
		clpBackgroundMarked.setOnAction(t -> setBackgroundButtonColor(btSampleMarked, clpBackgroundMarked.getValue()));
		clpTextFillMarked.setOnAction(t -> setTextFillColor(btSampleMarked, clpTextFillMarked.getValue()));
		clpBackScreen.setOnAction(
				t -> rectColor.setFill(Color.web(clpBackScreen.getValue().toString().replace("x", "#").substring(1))));

	}

	private void setBackgroundButtonColor(Button bt, Color color) {
		String value = color.toString().replace("x", "#").substring(1);
		String style = bt.getStyle();
		String[] cssProps = style.split(";");
		bt.setStyle("-fx-background-color: " + value + "; " + cssProps[1] + ";"
				+ " -fx-font-size: 46; -fx-font-weight: bold;");
	}

	private void setTextFillColor(Button bt, Color color) {
		String value = color.toString().replace("x", "#").substring(1);
		String style = bt.getStyle();
		String[] cssProps = style.split(";");
		bt.setStyle(cssProps[0] + "; -fx-text-fill: " + value + "; -fx-font-size: 46; -fx-font-weight: bold;");
	}

	public void setContext(String backgroundColorClear, String textFillClear, String backgroundColorMarked,
			String textFillMarked, String backgroundColorScreen) {

		btSampleClear.setShape(new Circle(r));
		btSampleClear.setMinSize(2 * r, 2 * r);
		btSampleClear.setMaxSize(2 * r, 2 * r);
		btSampleMarked.setShape(new Circle(r));
		btSampleMarked.setMinSize(2 * r, 2 * r);
		btSampleMarked.setMaxSize(2 * r, 2 * r);

		btSampleClear.setStyle("-fx-background-color: " + backgroundColorClear + "; -fx-text-fill: " + textFillClear
				+ ";" + " -fx-font-size: 46; -fx-font-weight: bold;");
		btSampleMarked.setStyle("-fx-background-color: " + backgroundColorMarked + "; -fx-text-fill: " + textFillMarked
				+ ";" + " -fx-font-size: 46; -fx-font-weight: bold;");

		rectColor.setFill(Color.web(backgroundColorScreen));

		clpBackgroundClear.setValue(Color.web(backgroundColorClear));
		clpTextFillClear.setValue(Color.web(textFillClear));
		clpBackgroundMarked.setValue(Color.web(backgroundColorMarked));
		clpTextFillMarked.setValue(Color.web(textFillMarked));
		clpBackScreen.setValue(Color.web(backgroundColorScreen));
	}

	@FXML
	private void save() {
		String[] cssClearProps = btSampleClear.getStyle().split(";");
		String[] backgroundClearProp = cssClearProps[0].split(":");
		String[] textFillClearProp = cssClearProps[1].split(":");

		String[] cssMarkedProps = btSampleMarked.getStyle().split(";");
		String[] backgroundMarkedProp = cssMarkedProps[0].split(":");
		String[] textFillMarkedProp = cssMarkedProps[1].split(":");

		Preferences prefs = Preferences.userRoot().node("config_bingo");
		prefs.put(BACKGROUND_CLEAR, backgroundClearProp[1].trim());
		prefs.put(BACKGROUND_MARKED, backgroundMarkedProp[1].trim());
		prefs.put(TEXT_CLEAR, textFillClearProp[1].trim());
		prefs.put(TEXT_MARKED, textFillMarkedProp[1].trim());
		prefs.put(BACKGROUND_SCREEN, clpBackScreen.getValue().toString().replace("x", "#").substring(1));
		close();
	}

	@FXML
	private void close() {
		Stage stage = (Stage) btSampleClear.getScene().getWindow();
		stage.close();
	}

}

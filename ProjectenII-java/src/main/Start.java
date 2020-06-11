package main;

import domein.DomeinController;
import gui.HoofdSchermController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Start extends Application {

	public DomeinController dc;

	public static void main(String[] args) {
		Application.launch(Start.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		DomeinController dc = new DomeinController();

		Scene scene = new Scene(new HoofdSchermController(dc));
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());

		stage.setMaxWidth(primaryScreenBounds.getWidth());
		stage.setMinWidth(primaryScreenBounds.getWidth());

		stage.setMaxHeight(primaryScreenBounds.getHeight());
		stage.setMinHeight(primaryScreenBounds.getHeight());
		stage.setMaximized(true);

		// scene .getStylesheets()
		// .add("styles.css");
		stage.setScene(scene);

		stage.setTitle("ITLab");

//		stage.setOnShown((WindowEvent t) -> {
//			stage.setMinWidth(stage.getWidth());
//			stage.setMinHeight(stage.getHeight());
//		});

		stage.show();
	}

}
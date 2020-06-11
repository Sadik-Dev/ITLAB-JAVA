package main;

import domein.HoofdDomeinController;
import domein.InlogController;
import gui.InlogSchermController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {

	public HoofdDomeinController dc;

	public static void main(String[] args) {
		Application.launch(Start.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		InlogController ic = new InlogController();

		Scene scene = new Scene(new InlogSchermController(ic));

		// scene .getStylesheets()
		// .add("styles.css");
		stage.setScene(scene);

		stage.setTitle("Inloggen");

//		stage.setOnShown((WindowEvent t) -> {
//			stage.setMinWidth(stage.getWidth());
//			stage.setMinHeight(stage.getHeight());
//		});

		stage.show();
	}

}
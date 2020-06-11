package gui;

import java.io.IOException;

import domein.HoofdDomeinController;
import domein.InlogController;
import domein.VerantwoordelijkeDomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class InlogSchermController extends VBox {
	@FXML
	private TextField txtGebruikersnaam;
	@FXML
	private PasswordField pswdPasswoord;
	@FXML
	private Button btnLogin;
	@FXML
	private Label lblError;

	private InlogController ic;

	public InlogSchermController(InlogController inlogController) {
		super();
		this.ic = inlogController;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("InlogScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.lblError.setText("");

	}

	// Event Listener on Button[#btnLogin].onAction
	@FXML
	public void LoginGebruiker(ActionEvent event) {
		try {
			String gebruikersnaam = this.txtGebruikersnaam.getText();
			String passwoord = this.pswdPasswoord.getText();
			boolean success = this.ic.login(gebruikersnaam, passwoord);

			if (success) {
				Stage window = (Stage) ((Node) event.getSource())	.getScene()
																	.getWindow();

				window.setTitle("IT-Lab");

				if (this.ic	.getITlabGebruiker()
							.getGebruikersType()
							.name()
							.equals("HoofdVerantwoordelijke")) {
					window.setScene(new Scene(new HoofdSchermController(new HoofdDomeinController(this.ic))));
				} else if (this.ic	.getITlabGebruiker()
									.getGebruikersType()
									.name()
									.equals("Verantwoordelijke")) {
					window.setScene(new Scene(new HoofdSchermController(new VerantwoordelijkeDomeinController(this.ic))));
				} else {
					throw new Exception();
				}

				Rectangle2D primaryScreenBounds = Screen.getPrimary()
														.getVisualBounds();
				window.setX(primaryScreenBounds.getMinX());
				window.setY(primaryScreenBounds.getMinY());

				window.setMaxWidth(primaryScreenBounds.getWidth());
				window.setMinWidth(primaryScreenBounds.getWidth());

				window.setMaxHeight(primaryScreenBounds.getHeight());
				window.setMinHeight(primaryScreenBounds.getHeight());
				window.setMaximized(true);

				window.show();
			} else {
				this.lblError.setText("Inloggen mislukt");
			}

		} catch (Exception e) {
			this.lblError.setText(e.getMessage()); // als het inloggen mislukt is
		}
	}
}

package gui;

import java.io.IOException;

import domein.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class InlogSchermController {
	@FXML
	private TextField txtGebruikersnaam;
	@FXML
	private PasswordField pswdPasswoord;
	@FXML
	private Button btnLogin;
	@FXML
	private Label lblError;

	private LoginController ic;

	public InlogSchermController(LoginController inlogController) {
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
			this.ic.login(gebruikersnaam, passwoord);
		} catch (Exception e) {
			this.lblError.setText(e.getMessage()); // als het inloggen mislukt is
		}
	}
}

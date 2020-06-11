package gui;

import java.io.IOException;

import domein.HoofdDomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AankondigingController extends AnchorPane {
	@FXML
	private Label lblSessieTitel;
	@FXML
	private TextField TxtAankondigingTitel;
	@FXML
	private TextArea TxtInhoud;
	@FXML
	private Button BtnVerstuur;
	@FXML
	private Label lblError;

	private HoofdDomeinController dc;

	public AankondigingController(HoofdDomeinController dc) {
		this.dc = dc;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Aankondiging.fxml"));

		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		this.lblSessieTitel.setText(this.dc	.getGeselecteerdeSessie()
											.getTitel());

		lblError.setText("");

	}

	// Event Listener on Button[#BtnVerstuur].onAction
	@FXML
	public void verstuurAankondiging(ActionEvent event) {

		try {
			String titel = this.TxtAankondigingTitel.getText();
			String inhoud = this.TxtInhoud.getText();
			this.dc.verstuurAankondiging(titel, inhoud);

			Stage window = (Stage) ((Node) event.getSource())	.getScene()
																.getWindow();
			window.close();
		} catch (Exception e) {
			lblError.setText(e.getMessage());
		}

	}
}

package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Optional;

import domein.DomeinController;
import domein.Gebruiker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GebruikerDetailsController extends VBox implements PropertyChangeListener {
	@FXML
	private TextField txtNaam;
	@FXML
	private TextField txtGebruikersnaam;
	@FXML
	private TextField txtBarcode;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtGebruikerstype;
	@FXML
	private TextField txtStatus;
	@FXML
	private Button btnVerwijder;
	@FXML
	private Button btnWijzig;
	@FXML
	private Label lblError;

	private DomeinController dc;

	public GebruikerDetailsController(DomeinController dc) {
		super();
		this.dc = dc;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikerDetails.fxml"));

		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		this.lblError.setText("");

	}

	// Event Listener on Button[#btnVerwijder].onAction
	@FXML
	public void verwijderGebruiker(ActionEvent event) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);

			alert.setTitle("Gebruiker verwijderen");
			alert.setHeaderText("U staat op het punt om een gebruiker te verwijderen.");
			alert.setContentText("Deze actie kan niet ongedaan gemaakt worden.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				this.dc.verwijderGebruiker();
			} else {
				alert.close();
			}
		} catch (Exception e) {
			this.lblError.setText(e.getMessage());
		}

	}

	// Event Listener on Button[#btnWijzig].onAction
	@FXML
	public void wijzigGebruiker(ActionEvent event) {
		try {
			Stage wijzigGebruikerWindow = new Stage();

			wijzigGebruikerWindow.initModality(Modality.APPLICATION_MODAL); // block alle andere windows tot dit is opgelost

			wijzigGebruikerWindow.setTitle("Wijzig Gebruiker");

			wijzigGebruikerWindow.setScene(new Scene(new GebruikerFormulierController(dc, true)));

			wijzigGebruikerWindow.show();
		} catch (Exception e) {
			this.lblError.setText(e.getMessage());
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		Gebruiker gebruiker = (Gebruiker) evt.getNewValue();

		txtNaam.setText(gebruiker	.getNaamSimpleStringProperty()
									.get());

		txtBarcode.setText(gebruiker.getBarcodeSimpleStringProperty()
									.get());

		txtEmail.setText(gebruiker	.getEmailSimpleStringProperty()
									.get());

		txtGebruikersnaam.setText(gebruiker	.getGebruikersNaamProperty()
											.get());
		txtGebruikerstype.setText(gebruiker	.getTypeSimpleStringProperty()
											.get());
		txtStatus.setText(gebruiker	.getStatusSimpleStringProperty()
									.get());
	}
}

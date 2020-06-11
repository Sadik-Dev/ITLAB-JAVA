package gui;

import java.io.IOException;

import domein.Gebruiker;
import domein.GebruikerDTO;
import domein.GebruikerState;
import domein.GebruikerStateEnum;
import domein.GebruikersType;
import domein.HoofdDomeinController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GebruikerFormulierController extends VBox {
	@FXML
	private TextField txtNaam;
	@FXML
	private TextField txtGebruikersnaam;
	@FXML
	private TextField txtBarcode;
	@FXML
	private TextField txtEmail;
	@FXML
	private ComboBox<GebruikersType> cbType;
	@FXML
	private ComboBox<GebruikerStateEnum> cbStatus;
	@FXML
	private Button btnMaakGebruiker;
	@FXML
	private Label lblError;

	private HoofdDomeinController dc;

	private boolean isWijzigingsFormulier = false;

	public GebruikerFormulierController(HoofdDomeinController dc,
										boolean isWijzigingsFormulier) {
		super();
		this.dc = dc;
		this.isWijzigingsFormulier = isWijzigingsFormulier;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikerFormulier.fxml"));

		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		this.cbType.setItems(FXCollections.observableArrayList(GebruikersType.values()));
		this.cbStatus.setItems(FXCollections.observableArrayList(GebruikerStateEnum.values()));

		// default gebruiker
		this.cbType.setValue(GebruikersType.Gebruiker);
		// default actief
		this.cbStatus.setValue(GebruikerStateEnum.ActiefState);

		if (isWijzigingsFormulier) {
			Gebruiker g = this.dc.getGeselecteerdeGebruiker();

			this.btnMaakGebruiker.setText("Wijzig gebruiker");

			this.txtNaam.setText(g.getNaam());
			this.txtGebruikersnaam.setText(g.getGebruikersnaam());
			this.txtBarcode.setText(Long.toString(g.getBarcode()));
			this.txtEmail.setText(g.getEmail());
			this.cbType.setValue(g.getGebruikersType());
			this.cbStatus.setValue(g.getCurrentState()
									.getDiscriminator());

			this.txtGebruikersnaam.setDisable(true);

		}

		this.lblError.setText("");

	}

	// Event Listener on Button[#btnMaakGebruiker].onAction
	@FXML
	public void maakGebruiker(ActionEvent event) {
		try {
			if (this.txtBarcode	.getText()
								.equals("")
				|| !this.txtBarcode	.getText()
									.matches("[0-9]*")) {
				throw new IllegalArgumentException("barcode mag enkel cijfers zijn");
			}
			GebruikerDTO dto = new GebruikerDTO(this.txtGebruikersnaam.getText(),
												this.cbType.getValue(),
												null,
												Long.parseLong(this.txtBarcode.getText()),
												this.txtEmail.getText(),
												this.txtNaam.getText(),
												new GebruikerState(null, this.cbStatus.getValue()));
			if (isWijzigingsFormulier) {
				this.dc.wijzigGebruiker(dto);
			} else {
				this.dc.maakGebruikerAan(dto);
			}

			Stage window = (Stage) ((Node) event.getSource())	.getScene()
																.getWindow();
			window.close();

		} catch (Exception e) {
			this.lblError.setText(e.getMessage());
		}
	}
}

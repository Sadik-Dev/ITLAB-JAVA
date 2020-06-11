package gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import domein.DomeinController;
import domein.GebruikersType;
import domein.ITlabGebruiker;
import domein.ITlabLokaal;
import domein.ITlabSessie;
import domein.Lokaal;
import domein.Sessie;
import domein.SessieDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalTimePicker;

public class SessieFormulierController extends BorderPane {
	@FXML
	private TextField txtTitel;
	
	  @FXML
	    private Label lblError;

	@FXML
	private TextArea txtDescription;

	@FXML
	private TextField txtAantalPlaatsen;

	@FXML
	private Button bevestig;

	@FXML
	private ComboBox<String> cdLokaal;

	@FXML
	private TextField txtGastspreker;

	@FXML
	private TextField txtMedia;

    @FXML
    private DatePicker txtStartDatum;

    @FXML
    private DatePicker txtEindeDatum;

    @FXML
    private LocalTimePicker txtStartUur;

    @FXML
    private LocalTimePicker txtEindeUur;
	
	private boolean isWijzigingsFormulier = false;
	private DomeinController dc;

	public SessieFormulierController(	DomeinController dc,
										boolean isWijzigingsFormulier) {
		this.isWijzigingsFormulier = isWijzigingsFormulier;
		this.dc = dc;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieFormulier.fxml"));

		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		this.cdLokaal.setItems(dc.geefLokalen());

		if (isWijzigingsFormulier) {
			Sessie s = this.dc.getGeselecteerdeSessie();

			this.bevestig.setText("Wijzig Sessie");

//				this.txtNaam.setText(g.getNaam());
//				this.txtGebruikersnaam.setText(g.getGebruikersnaam());
//				this.txtBarcode.setText(Long.toString(g.getBarcode()));
//				this.txtEmail.setText(g.getEmail());
//				this.cbType.setValue(g.getGebruikersType());
//				this.cbStatus.setValue(g.getCurrentState()
//										.getDiscriminator());

		}
		lblError.setText("");
		
	}

	@FXML
	void maakSessieAan(ActionEvent event) {
		LocalDateTime start = LocalDateTime.of(txtStartDatum.getValue(), txtStartUur.getLocalTime());
		LocalDateTime einde =LocalDateTime.of(txtEindeDatum.getValue(), txtEindeUur.getLocalTime());
		
		String gastSpreker = txtGastspreker.getText();
		if(gastSpreker.isBlank() || gastSpreker == null) {
			gastSpreker = "GEEN";
			
		}
		try {
			SessieDTO sessie = new SessieDTO(
				txtTitel.getText(), 
				txtDescription.getText(), 
				Integer.parseInt(txtAantalPlaatsen.getText()),  										
				cdLokaal.getValue(),
				"123456hp",
				start,
				einde,
				gastSpreker,
				null // media nog even niet
				);


				dc.maakSessieAan(sessie);
			
				Stage window = (Stage) ((Node) event.getSource())	.getScene()
				.getWindow();
				window.close();

				} catch (Exception e) {
					this.lblError.setText(e.getMessage());
				}	
	}

}

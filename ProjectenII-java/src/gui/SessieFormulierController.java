package gui;

import java.io.IOException;
import java.time.LocalDateTime;

import domein.HoofdDomeinController;
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
	private DatePicker txtStartDatum;

	@FXML
	private DatePicker txtEindeDatum;

	@FXML
	private LocalTimePicker txtStartUur;

	@FXML
	private LocalTimePicker txtEindeUur;

	private boolean isWijzigingsFormulier = false;
	private HoofdDomeinController dc;

	public SessieFormulierController(	HoofdDomeinController dc,
										boolean isWijzigingsFormulier) {
		super();
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

			this.txtTitel.setText(s.getTitel());
			this.txtStartDatum.setValue(s	.getStart()
											.toLocalDate());
			this.txtStartUur.setLocalTime(s	.getStart()
											.toLocalTime());
			this.txtEindeDatum.setValue(s	.getEinde()
											.toLocalDate());
			this.txtEindeUur.setLocalTime(s	.getEinde()
											.toLocalTime());
			this.txtAantalPlaatsen.setText(Integer.toString(s.getAantalPlaatsen()));
			this.txtGastspreker.setText(s.getGastspreker());
			this.cdLokaal.setValue(s.getITlabLokaalString());
			// this.txtMedia
			this.txtDescription.setText(s.getDescription());
		}
		lblError.setText("");

	}

	@FXML
	void maakSessieAan(ActionEvent event) {
		LocalDateTime start = LocalDateTime.of(txtStartDatum.getValue(), txtStartUur.getLocalTime());
		LocalDateTime einde = LocalDateTime.of(txtEindeDatum.getValue(), txtEindeUur.getLocalTime());

		String gastSpreker = txtGastspreker.getText();

		if (gastSpreker == null || gastSpreker.isBlank()) {
			gastSpreker = "GEEN";

		}
		try {
			SessieDTO sessie = new SessieDTO(	txtTitel.getText(),
												txtDescription.getText(),
												Integer.parseInt(txtAantalPlaatsen.getText()),
												cdLokaal.getValue(),
												dc	.getIngelogdegebGebruiker()
													.getGebruikersnaam(),
												start,
												einde,
												gastSpreker,
												dc	.getGeselecteerdeSessie()
													.getCurrentStateString());

			if (isWijzigingsFormulier) {
				dc.wijzigSessie(sessie);
			} else {
				dc.maakSessieAan(sessie);
			}

			Stage window = (Stage) ((Node) event.getSource())	.getScene()
																.getWindow();
			window.close();

		} catch (Exception e) {
			this.lblError.setText(e.getMessage());
		}
	}

}

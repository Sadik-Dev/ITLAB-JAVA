package gui;

import java.io.IOException;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class HoofdSchermController extends BorderPane {

	@FXML
	private HBox overzicht;

	@FXML
	private Button sessies;

	@FXML
	private Label lblSchermtitel;

	@FXML
	private Button gebruikers;

	@FXML
	private Button statistieken;

	private DomeinController dc;
	private SessieDetailSchermController sessieDetail;
	private SessieOverzichtSchermController sessieOverzicht;
	private GebruikerOverzichtController gebruikersOverzicht;
	private GebruikerDetailsController gebruikersDetail;

	public HoofdSchermController(DomeinController dc) {
		this.dc = dc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HoofdScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		sessieOverzicht = new SessieOverzichtSchermController(dc);
		sessieDetail = new SessieDetailSchermController(dc);
//		gebruikersOverzicht = new GebruikerOverzichtController(dc);
//		gebruikerDetail = new GebruikerDetailsController(dc);
		dc.addPropertyChangeListener(sessieDetail);
		// dc.addPropertyChangeListener(gebruikerDetail);
		overzicht	.getChildren()
					.add(sessieOverzicht);
		overzicht	.getChildren()
					.add(sessieDetail);

		this.lblSchermtitel.setText("Sessies");

		//
		//
		//
		//
		this.statistieken.setDisable(true);
		//
		//
		///
		//

	}

	@FXML
	void naarGebruikers(ActionEvent event) {

		if (!lblSchermtitel	.getText()
							.equals("Gebruikers")) {
			gebruikersOverzicht = new GebruikerOverzichtController(dc);
			gebruikersDetail = new GebruikerDetailsController(dc);

			overzicht	.getChildren()
						.clear();
			overzicht	.getChildren()
						.addAll(gebruikersOverzicht, gebruikersDetail);
			dc.removePropertyChangeListener(sessieDetail);
			dc.addPropertyChangeListener(gebruikersDetail);
			this.lblSchermtitel.setText("Gebruikers");
		}

	}

	@FXML
	void naarSessies(ActionEvent event) {
		if (!this.lblSchermtitel.getText()
								.equals("Sessies")) {
			sessieOverzicht = new SessieOverzichtSchermController(dc);
			sessieDetail = new SessieDetailSchermController(dc);

			overzicht	.getChildren()
						.clear();
			overzicht	.getChildren()
						.addAll(sessieOverzicht, sessieDetail);
			dc.removePropertyChangeListener(gebruikersDetail);
			dc.addPropertyChangeListener(sessieDetail);
			this.lblSchermtitel.setText("Sessies");
		}
	}

	@FXML
	void naarStatistieken(ActionEvent event) {

		if (!this.lblSchermtitel.getText()
								.equals("Statistieken")) {
			this.lblSchermtitel.setText("Statistieken");
		}

	}
}

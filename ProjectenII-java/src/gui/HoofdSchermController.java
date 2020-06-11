package gui;

import java.io.IOException;

import domein.HoofdDomeinController;
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

	private HoofdDomeinController dc;
	private SessieDetailSchermController sessieDetail;
	private SessieOverzichtSchermController sessieOverzicht;
	private GebruikerOverzichtController gebruikersOverzicht;
	private GebruikerDetailsController gebruikersDetail;
	private HoofdStatistiekenController statistiekenScherm;

	public HoofdSchermController(HoofdDomeinController dc) {
		this.dc = dc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HoofdScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		gebruikersOverzicht = new GebruikerOverzichtController(dc);
		gebruikersDetail = new GebruikerDetailsController(dc);

		statistiekenScherm = new HoofdStatistiekenController(dc);

		sessieOverzicht = new SessieOverzichtSchermController(dc);
		sessieDetail = new SessieDetailSchermController(dc);

		dc.removePropertyChangeListener(sessieDetail);
		dc.removePropertyChangeListener(this.statistiekenScherm.getSessieListener());
		dc.removePropertyChangeListener(this.statistiekenScherm.getGebruikersListener());
		dc.removePropertyChangeListener(gebruikersDetail);

		dc.addPropertyChangeListener(sessieDetail);

		overzicht	.getChildren()
					.add(sessieOverzicht);
		overzicht	.getChildren()
					.add(sessieDetail);

		this.lblSchermtitel.setText("Sessies");

		//
		//
		//
		//
//		this.statistieken.setDisable(true);
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

			dc.removePropertyChangeListener(sessieDetail);
			dc.removePropertyChangeListener(this.statistiekenScherm.getSessieListener());
			dc.removePropertyChangeListener(this.statistiekenScherm.getGebruikersListener());
			dc.addPropertyChangeListener(gebruikersDetail);

			overzicht	.getChildren()
						.clear();
			overzicht	.getChildren()
						.addAll(gebruikersOverzicht, gebruikersDetail);

			this.lblSchermtitel.setText("Gebruikers");
		}

	}

	@FXML
	void naarSessies(ActionEvent event) {
		if (!this.lblSchermtitel.getText()
								.equals("Sessies")) {

			sessieOverzicht = new SessieOverzichtSchermController(dc);
			sessieDetail = new SessieDetailSchermController(dc);

			dc.removePropertyChangeListener(gebruikersDetail);
			dc.removePropertyChangeListener(this.statistiekenScherm.getSessieListener());
			dc.removePropertyChangeListener(this.statistiekenScherm.getGebruikersListener());
			dc.addPropertyChangeListener(sessieDetail);

			overzicht	.getChildren()
						.clear();
			overzicht	.getChildren()
						.addAll(sessieOverzicht, sessieDetail);
			this.lblSchermtitel.setText("Sessies");
		}
	}

	@FXML
	void naarStatistieken(ActionEvent event) {

		if (!this.lblSchermtitel.getText()
								.equals("Statistieken")) {

			dc.removePropertyChangeListener(gebruikersDetail);
			dc.removePropertyChangeListener(sessieDetail);
			dc.removePropertyChangeListener(this.statistiekenScherm.getSessieListener());
			dc.removePropertyChangeListener(this.statistiekenScherm.getGebruikersListener());

			statistiekenScherm = new HoofdStatistiekenController(dc);

			overzicht	.getChildren()
						.clear();
			overzicht	.getChildren()
						.add(statistiekenScherm);

			this.lblSchermtitel.setText("Statistieken");
		}

	}
}

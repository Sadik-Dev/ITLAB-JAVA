package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Optional;

import domein.HoofdDomeinController;
import domein.Sessie;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SessieDetailSchermController extends GridPane implements PropertyChangeListener {
	@FXML
	private TextField titelDetail;

	@FXML
	private TextField verantwoordelijkeDetail;

	@FXML
	private TextField startDetail;

	@FXML
	private TextField eindeDetail;

	@FXML
	private TextField aanwezigenDetail;

	@FXML
	private TextField aantalPlaatsenDetail;

	@FXML
	private Button wijzigSessie;

	@FXML
	private Button verwijderSessie;

	@FXML
	private Button aankondigingSessie;
    @FXML
    private Label lblError;


	private HoofdDomeinController dc;

	public SessieDetailSchermController(HoofdDomeinController dc) {
		this.dc = dc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieDetailScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Sessie sessie = (Sessie) evt.getNewValue();
		titelDetail.setText(sessie	.getTitelProperty()
									.get());
		verantwoordelijkeDetail.setText(sessie	.getVerantwoordelijkeProperty()
												.get());
		startDetail.setText(sessie	.getStartStringProperty()
									.get());
		
		eindeDetail.setText(sessie  .getEindeStringProperty()
									.get());
		aanwezigenDetail.setText(sessie.getAantalAanwezigenProperty()
									.get());
		aantalPlaatsenDetail.setText(sessie.getAantalPlaatsenProperty()
									.get());
	}

	@FXML
	void KondigSessieAan(ActionEvent event) {
		Stage aankondigingWindow = new Stage();

		aankondigingWindow.initModality(Modality.APPLICATION_MODAL); // block alle andere windows tot dit is opgelost

		aankondigingWindow.setTitle("aankondiging : " + dc	.getGeselecteerdeSessie()
															.getTitel());

		aankondigingWindow.setScene(new Scene(new AankondigingController(dc)));

		aankondigingWindow.show();
	}

	@FXML
	void VerwijderSessie(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);

		alert.setTitle("Sessie verwijderen");
		alert.setHeaderText("U staat op het punt om een sessie te verwijderen.");
		alert.setContentText("Deze actie kan niet ongedaan gemaakt worden.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			this.dc.verwijderSessie();
		} else {
			alert.close();
		}

	}

	@FXML
	void WijzigSessie(ActionEvent event) {
		try {
			Stage wijzigSessieWindow = new Stage();

			wijzigSessieWindow.initModality(Modality.APPLICATION_MODAL); 

			wijzigSessieWindow.setTitle("Wijzig Sessie");

			wijzigSessieWindow.setScene(new Scene(new SessieFormulierController(dc, true)));

			wijzigSessieWindow.show();
			
		} catch (Exception e) {
			this.lblError.setText(e.getMessage());
		}

	}
}

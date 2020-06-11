package gui;

import java.awt.Desktop;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import domein.HoofdDomeinController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HoofdStatistiekenController extends HBox {
	@FXML
	private HBox contentBox;
	@FXML
	private Button btnSessies;
	@FXML
	private Button btnGebruikers;
	@FXML
	private VBox lijstBox;
	@FXML
	private Label lblCategorie;
	@FXML
	private Hyperlink linkPowerBI;

	private HoofdDomeinController dc;
	private SessieStatistiekenController sessieStats;
	private GebruikerStatistiekenController gebruikerStats;

	private ListView<String> lvSessies;
	private ListView<String> lvGebruikers;

	public HoofdStatistiekenController(HoofdDomeinController dc) {
		this.dc = dc;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("HoofdStatistieken.fxml"));

		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		//// gebruikers
		this.dc.filtreerGebruikers("");
		lvGebruikers = new ListView<>();
		List<String> namen = this.dc.geefGebruikers()
									.stream()
									.map(s -> s.getNaam())
									.collect(Collectors.toList());

		lvGebruikers.setItems(FXCollections.observableArrayList(namen));

		lvGebruikers.getSelectionModel()
					.selectedItemProperty()
					.addListener((observableValue, oldValue, newValue) -> {
						// check of item geselecteerd is
						if (newValue != null) {
							dc.setGeselecteerdeGebruiker(dc.geefGebruikerOpNaam(newValue));

						}

					});

		this.gebruikerStats = new GebruikerStatistiekenController(dc);

		//// Sessies
		this.dc.filtreerSessies("");
		lvSessies = new ListView<>();
		List<String> titels = this.dc	.geefSessies()
										.stream()
										.map(s -> s.getTitel())
										.collect(Collectors.toList());

		lvSessies.setItems(FXCollections.observableArrayList(titels));

		lvSessies	.getSelectionModel()
					.selectedItemProperty()
					.addListener((observableValue, oldValue, newValue) -> {
						// check of item geselecteerd is
						if (newValue != null) {
							dc.setGeselecteerdeSessie(dc.geefSessie(newValue));

						}

					});

		lblCategorie.setText("Sessies");
		this.lijstBox	.getChildren()
						.add(lvSessies);

		this.btnSessies.setDisable(true);
		this.sessieStats = new SessieStatistiekenController(dc);
		this.contentBox	.getChildren()
						.add(sessieStats);

		this.dc.removePropertyChangeListener(gebruikerStats);
		this.dc.addPropertyChangeListener(sessieStats);
	}

	// Event Listener on Button[#btnSessies].onAction
	@FXML
	public void toonSessielijst(ActionEvent event) {
		if (!this.lblCategorie	.getText()
								.equals("Sessies")) {
			lblCategorie.setText("Sessies");

			this.lijstBox	.getChildren()
							.remove(lvGebruikers);
			this.lijstBox	.getChildren()
							.add(lvSessies);

			this.contentBox	.getChildren()
							.remove(gebruikerStats);
			this.contentBox	.getChildren()
							.add(sessieStats);

			this.dc.removePropertyChangeListener(gebruikerStats);
			this.dc.addPropertyChangeListener(sessieStats);

			this.btnSessies.setDisable(true);
			this.btnGebruikers.setDisable(false);
		}
	}

	// Event Listener on Button[#btnGebruikers].onAction
	@FXML
	public void toonGebruikerslijst(ActionEvent event) {
		if (!this.lblCategorie	.getText()
								.equals("Gebruikers")) {
			lblCategorie.setText("Gebruikers");

			this.lijstBox	.getChildren()
							.remove(lvSessies);
			this.lijstBox	.getChildren()
							.add(lvGebruikers);

			this.contentBox	.getChildren()
							.remove(sessieStats);
			this.contentBox	.getChildren()
							.add(gebruikerStats);

			this.dc.removePropertyChangeListener(sessieStats);
			this.dc.addPropertyChangeListener(gebruikerStats);

			this.btnSessies.setDisable(false);
			this.btnGebruikers.setDisable(true);
		}
	}

	@FXML
	void goToPowerBI(ActionEvent event) {

		if (Desktop.isDesktopSupported()) {
			try {
				Desktop	.getDesktop()
						.browse(new URI("https://powerbi.microsoft.com/nl-nl/"));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}

	public PropertyChangeListener getSessieListener() {
		return this.sessieStats;
	}

	public PropertyChangeListener getGebruikersListener() {
		return this.gebruikerStats;
	}

}

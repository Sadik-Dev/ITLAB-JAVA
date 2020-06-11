package gui;

import java.io.IOException;

import domein.Gebruiker;
import domein.HoofdDomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GebruikerOverzichtController extends VBox {
	@FXML
	private TableView<Gebruiker> tableGebruikers;
	@FXML
	private TableColumn<Gebruiker, String> columnGebruikersnaam;
	@FXML
	private TableColumn<Gebruiker, String> columnNaam;
	@FXML
	private TableColumn<Gebruiker, String> columnType;
	@FXML
	private Button btnNieuweGebruiker;
	
	@FXML
	private TextField gebruikersFilterenTextField;
	private HoofdDomeinController dc;

	public GebruikerOverzichtController(HoofdDomeinController dc) {
		super();
		this.dc = dc;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikerOverzicht.fxml"));

		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		this.columnGebruikersnaam.setCellValueFactory(data -> data	.getValue()
																	.getGebruikersNaamProperty());
		this.columnNaam.setCellValueFactory(data -> data.getValue()
														.getNaamSimpleStringProperty());
		this.columnType.setCellValueFactory(data -> data.getValue()
														.getTypeSimpleStringProperty());

	

		
			this.tableGebruikers.setItems(this.dc.geefGebruikers());
			tableGebruikers	.getSelectionModel()
							.selectedItemProperty()
							.addListener((observableValue, oldValue, newValue) -> {
								// check of item geselecteerd is
								if (newValue != null) {
									dc.setGeselecteerdeGebruiker(newValue);
									
								}
								
		
							});
	

	}

	// Event Listener on Button[#btnNieuweGebruiker].onAction
	@FXML
	public void maakNieuweGebruiker(ActionEvent event) {
	
			Stage nieuweGebruikerWindow = new Stage();

			nieuweGebruikerWindow.initModality(Modality.APPLICATION_MODAL); // block alle andere windows tot dit is opgelost

			nieuweGebruikerWindow.setTitle("Nieuwe Gebruiker");

			nieuweGebruikerWindow.setScene(new Scene(new GebruikerFormulierController(dc, false)));

			nieuweGebruikerWindow.show();}
		
	
	@FXML
	public void filtreerGebruikers(KeyEvent event) {
		dc.filtreerGebruikers(gebruikersFilterenTextField.getText());
	}
}

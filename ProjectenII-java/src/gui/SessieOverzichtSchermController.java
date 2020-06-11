package gui;

import java.io.IOException;

import domein.HoofdDomeinController;
import domein.Lokaal;
import domein.Sessie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SessieOverzichtSchermController extends BorderPane {
	@FXML
	private TableView<Sessie> sessieOverzicht;

	@FXML
	private TableColumn<Sessie, String> titelKolom;

	@FXML
	private TableColumn<Sessie, String> verantwoordelijkeKolom;

	@FXML
	private TableColumn<Sessie, String> startKolom;
	
	@FXML
	private TableColumn<Sessie, String> lokaalKolom;

	@FXML
	private Button nieuweSessie;
	@FXML
	private TextField filtreerTextField;
	
	private HoofdDomeinController dc;

	public SessieOverzichtSchermController(HoofdDomeinController dc) {
		this.dc = dc;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieOverzichtScherm.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		titelKolom.setCellValueFactory(cellData -> cellData	.getValue()
															.getTitelProperty());
		
		verantwoordelijkeKolom.setCellValueFactory(cellData -> cellData	.getValue()
																		.getVerantwoordelijkeProperty());
		startKolom.setCellValueFactory(cellData -> cellData	.getValue()
															.getStartStringProperty());
		lokaalKolom.setCellValueFactory(cellData -> cellData	.getValue()
				.getLokaalCodeProperty());
		sessieOverzicht.setItems(dc.geefSessies());
		
		sessieOverzicht	.getSelectionModel()
						.selectedItemProperty()
						.addListener((observableValue, oldValue, newValue) -> {
							// check of item geselecteerd is
							if (newValue != null) {
								 dc.setGeselecteerdeSessie(newValue);
								;
							}
						});

	}
	@FXML
	void filtreerSessies(KeyEvent event) {
		//System.out.println(filtreerTextField.getText());
		dc.filtreerSessies(filtreerTextField.getText());
	}
	
    @FXML
    void maakNieuweSessieAan(ActionEvent event) {
		Stage nieuweSessieWindow = new Stage();

		nieuweSessieWindow.initModality(Modality.APPLICATION_MODAL); // block alle andere windows tot dit is opgelost

		nieuweSessieWindow.setTitle("Nieuwe Sessie");

		nieuweSessieWindow.setScene(new Scene(new SessieFormulierController(dc, false)));

		nieuweSessieWindow.show();
    }
}
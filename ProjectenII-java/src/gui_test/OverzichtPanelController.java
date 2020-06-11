package gui_test;

import java.io.IOException;

import domein.DomeinController;
import domein.Sessie;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class OverzichtPanelController extends BorderPane {

	@FXML
	TableView<Sessie> table;
	@FXML
	TableColumn<Sessie, String> plaat;
	@FXML
	TableColumn<Sessie, String> merk;
	@FXML
	TableColumn<Sessie, String> model;
	@FXML
	private TextField txtVanMerk;

	@FXML
	private TextField txtTotMerk;

	private DomeinController domeinController;

	public OverzichtPanelController(DomeinController domeinController) {
		this.domeinController = domeinController;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("OverzichtPanel.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		table.setItems(domeinController.geefSessies());

		plaat.setCellValueFactory(data -> data	.getValue()
												.getTitelProperty());
		merk.setCellValueFactory(data -> data	.getValue()
												.getStartStringProperty());
		model.setCellValueFactory(data -> data	.getValue()
												.getStartStringProperty());

		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Object object = table	.getSelectionModel()
										.getSelectedItem();
				System.out.println(object);
			}
		});

	}

	private void filter(KeyEvent event) {

	}

}

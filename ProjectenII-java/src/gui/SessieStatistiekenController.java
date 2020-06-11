package gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Formatter;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SessieStatistiekenController {
	@FXML
	private Label lblSessietitel;
	@FXML
	private Label lblVerantwoordelijke;
	@FXML
	private Label lblStart;
	@FXML
	private Label lblEinde;
	@FXML
	private Label lblInschrijvingen;
	@FXML
	private Label lblAanwezigheden;
	@FXML
	private Label lblPercentueel;
	@FXML
	private Button btnDownload;

	private DomeinController dc;

	public SessieStatistiekenController(DomeinController dc) {
		super();
		this.dc = dc;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("SessieStatistieken.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Event Listener on Button[#btnDownload].onAction
	@FXML
	public void downloadStatistieken(ActionEvent event) {

		File directory = new File("Statistieken/Sessies");
		directory.mkdirs();

		LocalDate now = LocalDate.now();
		String dateStr = String.format("%d-%d-%d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());

		try (Formatter output = new Formatter(Files.newOutputStream(Paths.get(String.format("Statistieken/Sessies/%s-%s.txt",
			this.lblSessietitel	.getText()
								.replaceAll(" ", ""),
			dateStr))))) {

			output.format(String.format("Statistieken voor sessie %s: %n%n", this.lblSessietitel.getText()));
			output.format("%-30s%s%n", "Sessietitel:", this.lblSessietitel.getText());
			output.format("%-30s%s%n", "Verantwoordelijke:", this.lblVerantwoordelijke.getText());
			output.format("%-30s%s%n", "Start:", this.lblStart.getText());
			output.format("%-30s%s%n", "Einde:", this.lblEinde.getText());
			output.format("%-30s%s%n", "Inschrijvingen:", this.lblInschrijvingen.getText());
			output.format("%-30s%s%n", "Aanwezigheden:", this.lblAanwezigheden.getText());
			output.format("%-30s%s%n", "Percentueel:", this.lblPercentueel.getText());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

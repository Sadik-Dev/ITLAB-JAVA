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

public class GebruikerStatistiekenController {
	@FXML
	private Label lblNaamGebruiker;
	@FXML
	private Label lblGebruikersnaam;
	@FXML
	private Label lblEmail;
	@FXML
	private Label lblInschrijvingen;
	@FXML
	private Label lbAanwezigheden;
	@FXML
	private Label lblPercentueel;
	@FXML
	private Button btnDownload;

	private DomeinController dc;

	public GebruikerStatistiekenController(DomeinController dc) {
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

		// TODO: statistieken opvullen

	}

	// Event Listener on Button[#btnDownload].onAction
	@FXML
	public void downloadStatistieken(ActionEvent event) {
		File directory = new File("Statistieken/Gebruikers");
		directory.mkdirs();

		LocalDate now = LocalDate.now();
		String dateStr = String.format("%d-%d-%d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());

		try (Formatter output = new Formatter(Files.newOutputStream(Paths.get(String.format("Statistieken/Gebruikers/%s-%s.txt",
			this.lblGebruikersnaam	.getText()
									.replaceAll(" ", ""),
			dateStr))))) {

			output.format(String.format("Statistieken voor gebruiker %s: %n%n", this.lblGebruikersnaam.getText()));
			output.format("%-30s%s%n", "Naam:", this.lblNaamGebruiker.getText());
			output.format("%-30s%s%n", "Gebruikersnaam:", this.lblGebruikersnaam.getText());
			output.format("%-30s%s%n", "Email:", this.lblEmail.getText());
			output.format("%-30s%s%n", "Inschrijvingen:", this.lblInschrijvingen.getText());
			output.format("%-30s%s%n", "Aanwezigheden:", this.lbAanwezigheden.getText());
			output.format("%-30s%s%n", "Percentueel:", this.lblPercentueel.getText());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

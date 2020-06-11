package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Formatter;

import domein.Gebruiker;
import domein.HoofdDomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GebruikerStatistiekenController extends AnchorPane implements PropertyChangeListener {
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

	private HoofdDomeinController dc;

	public GebruikerStatistiekenController(HoofdDomeinController dc) {
		super();
		this.dc = dc;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("GebruikerStatistieken.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	// Event Listener on Button[#btnDownload].onActio
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
			output.format("%-30s%s%n", "Afwezigheden:", this.lbAanwezigheden.getText());
			output.format("%-30s%s%n", "Percentueel:", this.lblPercentueel.getText());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Gebruiker g = (Gebruiker) evt.getNewValue();

		this.lblNaamGebruiker.setText(g.getNaam());

		this.lblGebruikersnaam.setText(g.getGebruikersnaam());

		this.lblEmail.setText(g.getEmail());

		this.lbAanwezigheden.setText(String.valueOf(g.getAantalAfwezigheden()));

		this.lblInschrijvingen.setText(String.valueOf(g.getAantalInschrijvingen()));

		int aantalAanwezigheden = g.getAantalAfwezigheden();
		int aantalInschrijvingen = g.getAantalInschrijvingen();

		String info;

		if (aantalInschrijvingen == 0) {
			info = "niet ingeschreven voor sessies";
		} else {
			double percent = 100 - aantalAanwezigheden / aantalInschrijvingen * 100;
			info = String.format("%.2f percent afwezig", percent);
		}

		this.lblPercentueel.setText(info);

	}

}

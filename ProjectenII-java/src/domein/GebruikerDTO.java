package domein;

import java.util.HashSet;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;

public class GebruikerDTO implements Gebruiker {

	private String gebruikersnaam;
	private String naam;
	private long barcode;
	private String afbeelding;
	private String email;
	private int aantalAfwezigheden;
	private GebruikersType gebruikersType;
	private Set<ITlabSessie> ingeschrevenSessies;
	private Set<ITlabSessie> aanwezigeGebruikers;
	private Set<ITlabSessie> georganiseerdeSessies;
	private GebruikerState currentState;

	public GebruikerDTO(String gebruikersnaam,
						GebruikersType gebruikersType,
						String afbeelding,
						long barcode,
						String email,
						String naam,
						GebruikerState gebruikerState) {
		this.setGebruikersnaam(gebruikersnaam);
		this.setGebruikersType(gebruikersType);
		this.setAantalAfwezigheden(0);
		this.setAfbeelding(afbeelding);
		this.setBarcode(barcode);
		this.setEmail(email);
		this.setNaam(naam);
		this.ingeschrevenSessies = new HashSet<>();
		this.aanwezigeGebruikers = new HashSet<>();
		this.setCurrentState(gebruikerState);
	}

	public GebruikerDTO(ITlabGebruiker gebruiker) {
		this.setGebruikersnaam(gebruiker.getGebruikersnaam());
		this.setGebruikersType(gebruiker.getGebruikersType());
		this.setAantalAfwezigheden(gebruiker.getAantalAfwezigheden());
		this.setAfbeelding(gebruiker.getAfbeelding());
		this.setBarcode(gebruiker.getBarcode());
		this.setEmail(gebruiker.getEmail());
		this.setNaam(gebruiker.getNaam());
		this.ingeschrevenSessies = new HashSet<>();
		this.aanwezigeGebruikers = new HashSet<>();
		this.setCurrentState(gebruiker.getCurrentState());
	}

	@Override
	public int getAantalAfwezigheden() {
		return this.aantalAfwezigheden;
	}

	public void setAantalAfwezigheden(int value) {
		this.aantalAfwezigheden = value;
	}

	@Override
	public String getAfbeelding() {
		return this.afbeelding;
	}

	public void setAfbeelding(String value) {
		this.afbeelding = value;
	}

	@Override
	public long getBarcode() {
		return this.barcode;
	}

	public void setBarcode(long value) {
		this.barcode = value;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String value) {
		this.email = value;
	}

	@Override
	public String getGebruikersnaam() {
		return this.gebruikersnaam;
	}

	public void setGebruikersnaam(String value) {
		this.gebruikersnaam = value;
	}

	@Override
	public String getNaam() {
		return this.naam;
	}

	public void setNaam(String value) {
		this.naam = value;
	}

	@Override
	public GebruikerState getCurrentState() {
		return this.currentState;
	}

	public void setCurrentState(GebruikerState value) {
		this.currentState = value;
	}

	@Override
	public GebruikersType getGebruikersType() {
		return gebruikersType;
	}

	public void setGebruikersType(GebruikersType gebruikersType) {
		this.gebruikersType = gebruikersType;
	}

	@Override
	public SimpleStringProperty getAfbeeldingProperty() {
		return new SimpleStringProperty(afbeelding);
	}

	@Override
	public SimpleStringProperty getNaamSimpleStringProperty() {
		return new SimpleStringProperty(naam);
	}

	@Override
	public SimpleStringProperty getGebruikersNaamProperty() {
		return new SimpleStringProperty(gebruikersnaam);

	}

	@Override
	public SimpleStringProperty getTypeSimpleStringProperty() {
		return new SimpleStringProperty(gebruikersType.name());

	}

	@Override
	public SimpleStringProperty getStatusSimpleStringProperty() {
		return new SimpleStringProperty(currentState.getDiscriminator()
													.name());
	}

	@Override
	public SimpleStringProperty getEmailSimpleStringProperty() {
		return new SimpleStringProperty(email);
	}

	@Override
	public SimpleStringProperty getBarcodeSimpleStringProperty() {
		return new SimpleStringProperty(String.valueOf(barcode));
	}
	@Override
	public boolean equals(Object obj) {
		GebruikerDTO g2 = (GebruikerDTO) obj;
		if(this.getGebruikersnaam().equals(g2.getGebruikersnaam()))
			return true;
		else return false;
	}

}

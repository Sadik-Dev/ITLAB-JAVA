package domein;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "Gebruiker")
@NamedQueries({ @NamedQuery(
	name = "ITlabGebruiker.getByGebruikersnaam",
	query = "SELECT g FROM ITlabGebruiker g WHERE g.gebruikersnaam = :gebruikersnaam") })
public class ITlabGebruiker implements Serializable, Gebruiker {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Gebruikersnaam", nullable = false)
	private String gebruikersnaam;

	@Column(name = "Naam", nullable = false)
	private String naam;

	@Column(name = "Barcode", nullable = false)
	private long barcode;

	@Column(name = "Afbeelding", nullable = true)
	private String afbeelding;

	@Column(name = "Email", nullable = true)
	private String email;

	@Column(name = "AantalAfwezigheden", nullable = false)
	private int aantalAfwezigheden;

	@Enumerated(EnumType.STRING)
	@Column(name = "Discriminator", nullable = false)
	private GebruikersType gebruikersType;

	@ManyToMany(mappedBy = "ingeschrevenGebruikers", fetch = FetchType.EAGER)
	private Set<ITlabSessie> ingeschrevenSessies;

	@ManyToMany(mappedBy = "aanwezigeGebruikers", fetch = FetchType.EAGER)
	private Set<ITlabSessie> aanwezigeGebruikers;

	@OneToMany(mappedBy = "verantwoordelijke", fetch = FetchType.EAGER)
	private Set<ITlabSessie> georganiseerdeSessies;

	@OneToOne(mappedBy = "context", cascade = CascadeType.PERSIST)
	private GebruikerState currentState;

	public ITlabGebruiker(	String gebruikersnaam,
							GebruikersType gebruikersType,
							String afbeelding,
							long barcode,
							String email,
							String naam) {
		super();
		this.setGebruikersnaam(gebruikersnaam);
		this.setGebruikersType(gebruikersType);
		this.setAantalAfwezigheden(0);
		this.setAfbeelding(afbeelding);
		this.setBarcode(barcode);
		this.setEmail(email);
		this.setNaam(naam);
		this.ingeschrevenSessies = new HashSet<>();
		this.aanwezigeGebruikers = new HashSet<>();
		this.setCurrentState(new GebruikerState(this, GebruikerStateEnum.ActiefState));
	}

	public ITlabGebruiker(GebruikerDTO dto) {
		super();
		this.setGebruikersnaam(dto.getGebruikersnaam());
		this.setGebruikersType(dto.getGebruikersType());
		this.setAantalAfwezigheden(0);
		this.setAfbeelding(dto.getAfbeelding());
		this.setBarcode(dto.getBarcode());
		this.setEmail(dto.getEmail());
		this.setNaam(dto.getNaam());
		this.ingeschrevenSessies = new HashSet<>();
		this.aanwezigeGebruikers = new HashSet<>();
		this.setCurrentState(new GebruikerState(this, GebruikerStateEnum.ActiefState));
	}

	public void wijzigGebruiker(GebruikerDTO dto) {
		this.setGebruikersnaam(dto.getGebruikersnaam());
		this.setGebruikersType(dto.getGebruikersType());
		this.setAfbeelding(dto.getAfbeelding());
		this.setBarcode(dto.getBarcode());
		this.setEmail(dto.getEmail());
		this.setNaam(dto.getNaam());
		this.setCurrentState(new GebruikerState(this,
												dto	.getCurrentState()
													.getDiscriminator()));

	}

	@Override
	public int getAantalAfwezigheden() {
		return this.aantalAfwezigheden;
	}

	private void setAantalAfwezigheden(int value) {
		this.aantalAfwezigheden = value;
	}

	@Override
	public String getAfbeelding() {
		return this.afbeelding;
	}

	private void setAfbeelding(String value) {
		this.afbeelding = value;
	}

	@Override
	public long getBarcode() {
		return this.barcode;
	}

	private void setBarcode(long value) {

		if (value < 100000000000L)
			throw new IllegalArgumentException("Barcode is te kort !");
		this.barcode = value;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	private void setEmail(String value) {
		if (!value.matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$"))
			throw new IllegalArgumentException("Email is incorrect !");
		this.email = value;
	}

	@Override
	public String getGebruikersnaam() {
		return this.gebruikersnaam;
	}

	private void setGebruikersnaam(String value) {
		if (value.length() < 3)
			throw new IllegalArgumentException("Gebruikersnaam is te kort !");
		this.gebruikersnaam = value;
	}

	@Override
	public String getNaam() {
		return this.naam;
	}

	private void setNaam(String value) {
		if (value.length() < 3)
			throw new IllegalArgumentException("Naam is te kort !");

		this.naam = value;
	}

	@Override
	public GebruikerState getCurrentState() {
		return this.currentState;
	}

	private void setCurrentState(GebruikerState value) {
		this.currentState = value;
	}

	@Override
	public GebruikersType getGebruikersType() {
		return gebruikersType;
	}

	private void setGebruikersType(GebruikersType gebruikersType) {
		if (gebruikersType == null)
			throw new IllegalArgumentException("GebruikersType mag niet leeg zijn !");
		this.gebruikersType = gebruikersType;
	}

	protected ITlabGebruiker() {
		super();
		// VOOR JPA
	}

//	@Override
//	public SimpleStringProperty getNaamSimpleStringProp() {
//		return new SimpleStringProperty(naam);
//	}
//
//	@Override
//	public SimpleStringProperty getGebruikersNaamChamilo() {
//		return new SimpleStringProperty(gebruikersnaam);
//	}
//
//	@Override
//	public SimpleStringProperty getTypeSimpleStringProp() {
//		return new SimpleStringProperty(gebruikersType.name());
//	}
//
//	@Override
//	public SimpleStringProperty getStatusSimpleStringProp() {
//		return new SimpleStringProperty(currentState.getDiscriminator().name());
//	}
	@Override
	public String toString() {
		return "Gebruiker [gebruikersnaam=" + gebruikersnaam + ", naam=" + naam + ", barcode=" + barcode + ", afbeelding="
			+ afbeelding + ", email=" + email + ", aantalAfwezigheden=" + aantalAfwezigheden + ", gebruikersType="
			+ gebruikersType + ", currentState=" + currentState + "]";
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gebruikersnaam == null) ? 0 : gebruikersnaam.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ITlabGebruiker other = (ITlabGebruiker) obj;
		if (gebruikersnaam == null) {
			if (other.gebruikersnaam != null)
				return false;
		} else if (!gebruikersnaam.equals(other.gebruikersnaam))
			return false;
		return true;
	}

	@Override
	public int getAantalInschrijvingen() {
		return this.ingeschrevenSessies.size();
	}

}

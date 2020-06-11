package domein;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import javafx.beans.property.SimpleStringProperty;

@Entity
@NamedQueries({
	@NamedQuery(name = "ITlabSessie.findByTitel", query = "SELECT s FROM ITlabSessie s WHERE s.titel = :sessieTitel") })
@Table(name = "Sessie")
public class ITlabSessie implements Serializable, Sessie {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", nullable = false)
	private int id;

	@Column(name = "AantalPlaatsen", nullable = false)
	private int aantalPlaatsen;

	@Column(name = "Description", nullable = false)
	private String description;

	@ManyToOne
	@JoinColumn(name = "VerantwoordelijkeGebruikersnaam", referencedColumnName = "Gebruikersnaam", nullable = true)
	private ITlabGebruiker verantwoordelijke;

	@Column(name = "IsGeopendGeweest", nullable = false)
	private boolean isGeopendGeweest;

	@Column(name = "Titel", nullable = false)
	private String titel;

	@Column(name = "Gastspreker", nullable = false)
	private String gastspreker;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LokaalId", referencedColumnName = "Id", nullable = true)
	private ITlabLokaal ITlabLokaal;

	@Column(name = "Start", nullable = false)
	private LocalDateTime start;

	@Column(name = "Einde", nullable = false)
	private LocalDateTime einde;

	@OneToOne(mappedBy = "context", cascade = CascadeType.PERSIST)
	private SessieState currentState;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "IngeschrevenGebruikers",
		joinColumns = @JoinColumn(name = "SessieId"),
		inverseJoinColumns = @JoinColumn(name = "GebruikerId"))
	private Set<ITlabGebruiker> ingeschrevenGebruikers;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "AanwezigeGebruikers",
		joinColumns = @JoinColumn(name = "SessieId"),
		inverseJoinColumns = @JoinColumn(name = "GebruikerId"))
	private Set<ITlabGebruiker> aanwezigeGebruikers;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "SessieId")
	private List<Media> media;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "SessieId")
	private List<FeedbackEntry> feedbackEntries;

	// constructor
	public ITlabSessie(	String titel,
						String description,
						int aantalPlaatsen,
						ITlabLokaal ITlabLokaal,
						ITlabGebruiker verantwoordelijke,
						LocalDateTime start,
						LocalDateTime einde,
						String gastspreker,
						List<Media> media)
		throws IllegalArgumentException {
		super();

		this.setTitel(titel);
		this.setDescription(description);
		this.setITlabLokaal(ITlabLokaal);
		this.setAantalPlaatsen(aantalPlaatsen);
		this.setGastspreker(gastspreker);
		this.setVerantwoordelijke(verantwoordelijke);
		this.setStart(start);
		this.setEinde(einde);
		this.setMedia(media);
		this.setCurrentState(new SessieState(this, SessieStateEnum.RegistratieOpenAanmeldenGeslotenState));
		this.ingeschrevenGebruikers = new HashSet<>();
		this.aanwezigeGebruikers = new HashSet<>();
	}

	public void wijzigSessie(ITlabSessie sessieGewijzigd) {

		this.setTitel(sessieGewijzigd.getTitel());
		this.setDescription(sessieGewijzigd.getDescription());
		this.setITlabLokaal(sessieGewijzigd.getITlabLokaal());
		this.setAantalPlaatsen(sessieGewijzigd.getAantalPlaatsen());
		this.setGastspreker(sessieGewijzigd.getGastspreker());
		this.setVerantwoordelijke(sessieGewijzigd.getVerantwoordelijke());
		this.setStart(sessieGewijzigd.getStart());
		this.setEinde(sessieGewijzigd.getEinde());
		this.setIngeschrevenGebruikers(sessieGewijzigd.getIngeschrevenGebruikers());
		this.setAanwezigeGebruikers(sessieGewijzigd.getAanwezigeGebruikers());
		this.setFeedbackEntries(sessieGewijzigd.getFeedbackEntries());
		this.setIsGeopendGeweest(sessieGewijzigd.isGeopendGeweest());
		this.setMedia(sessieGewijzigd.getMedia());
	}

	// Test Constructor voor CUI
	public ITlabSessie(	int id,
						String titel,
						LocalDateTime start) {
		this.id = id;
		this.titel = titel;
		this.start = start;
	}

	// getters en setters

	public int getId() {
		return this.id;
	}

	public ITlabGebruiker getVerantwoordelijke() {
		return verantwoordelijke;
	}

	public void setVerantwoordelijke(ITlabGebruiker verantwoordelijke) {
		this.verantwoordelijke = verantwoordelijke;
	}

	@Override
	public String getTitel() {
		return this.titel;
	}

	private void setTitel(String value) {
		this.titel = value;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	private void setDescription(String value) {
		this.description = value;
	}

	@Override
	public int getAantalPlaatsen() {

		return this.aantalPlaatsen;
	}

	public void setAantalPlaatsen(int aantal) {
		if (aantal > ITlabLokaal.getAantalPlaatsen()) {
			throw new IllegalArgumentException(String.format("Er kunnen slechts %d personen in dit lokaal. U koos er %d",
				ITlabLokaal.getAantalPlaatsen(),
				aantalPlaatsen));
		}
		this.aantalPlaatsen = aantal;
	}

	@Override
	public LocalDateTime getStart() {
		return this.start;
	}

	private void setStart(LocalDateTime value) {
		if (value.isBefore(LocalDateTime.now()
										.plusDays(1)
										.minusMinutes(1))) {
			throw new IllegalArgumentException("De sessie moet minstens 1 dag in de toekomst liggen");
		}
		this.start = value;
	}

	@Override
	public LocalDateTime getEinde() {
		return this.einde;
	}

	private void setEinde(LocalDateTime value) {
		if (value.isBefore(start)) {
			throw new IllegalArgumentException("Het einde van de sessie mag niet voor het begin van de sessie zijn.");
		} else if (value.isBefore(start.plusMinutes(30))) {
			throw new IllegalArgumentException("De minimale duur van een sessie is 30 minuten");
		} else if (value.isAfter(start.plusHours(4))) {

			throw new IllegalArgumentException("De maximale duur van een sessie is 4 uur");
		}
		this.einde = value;
	}

	@Override
	public String getGastspreker() {
		return this.gastspreker;
	}

	private void setGastspreker(String value) {
		this.gastspreker = value;
	}

	@Override
	public boolean isGeopendGeweest() {
		// TODO Auto-generated method stub
		return this.isGeopendGeweest;
	}

	private void setIsGeopendGeweest(boolean value) {
		this.isGeopendGeweest = value;
	}

	public SessieState getCurrentState() {
		return this.currentState;
	}

	private void setCurrentState(SessieState value) {
		this.currentState = value;
	}

	public ITlabLokaal getITlabLokaal() {
		return ITlabLokaal;
	}

	private void setITlabLokaal(ITlabLokaal ITlabLokaal) {
		this.ITlabLokaal = ITlabLokaal;
	}

	@Override
	public List<Media> getMedia() {
		return media;
	}

	private void setMedia(List<Media> media) {
		this.media = media;
	}

	@Override
	public String toString() {
		return "Sessie [id=" + id + ", aantalPlaatsen=" + getAantalPlaatsen() + ", description=" + description
			+ ", verantwoordelijke=" + verantwoordelijke + ", isGeopendGeweest=" + isGeopendGeweest + ", titel=" + titel
			+ ", gastspreker=" + gastspreker + ", lokaal=" + ITlabLokaal + ", start=" + start + ", einde=" + einde
			+ ", currentState=" + currentState + ", ingeschrevenGebruikers=" + ingeschrevenGebruikers + ", aanwezigeGebruikers="
			+ aanwezigeGebruikers + ", media=" + media + ", feedbackEntries=" + feedbackEntries + "]";
	}

	public void toState(SessieState newState) {
		this.currentState = newState;

	}

	public void setIngeschrevenGebruikers(Set<ITlabGebruiker> ingeschrevenGebruikers) {
		this.ingeschrevenGebruikers = ingeschrevenGebruikers;
	}

	public void setAanwezigeGebruikers(Set<ITlabGebruiker> aanwezigeGebruikers) {
		this.aanwezigeGebruikers = aanwezigeGebruikers;
	}

	public void setFeedbackEntries(List<FeedbackEntry> feedbackEntries) {
		this.feedbackEntries = feedbackEntries;
	}

	public Set<ITlabGebruiker> getIngeschrevenGebruikers() {
		return ingeschrevenGebruikers;
	}

	public Set<ITlabGebruiker> getAanwezigeGebruikers() {
		return aanwezigeGebruikers;
	}

	public List<FeedbackEntry> getFeedbackEntries() {
		return feedbackEntries;
	}

	protected ITlabSessie() {
		super();
		// VOOR JPA
	}

	@Override
	public SimpleStringProperty getAantalAanwezigenProperty() {
		return new SimpleStringProperty(String.valueOf(aanwezigeGebruikers.size()));
	}

	@Override
	public SimpleStringProperty getStartStringProperty() {
		return new SimpleStringProperty(start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
	}

	@Override
	public SimpleStringProperty getEindeStringProperty() {
		return new SimpleStringProperty(einde.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
	}

	@Override
	public SimpleStringProperty getLokaalCodeProperty() {
		return new SimpleStringProperty(ITlabLokaal.getLokaalcode());
	}

	@Override
	public SimpleStringProperty getAantalPlaatsenProperty() {
		return new SimpleStringProperty(String.valueOf(aantalPlaatsen));
	}

	@Override
	public SimpleStringProperty getTitelProperty() {
		return new SimpleStringProperty(titel);
	}

	@Override
	public SimpleStringProperty getDescriptionProperty() {
		return new SimpleStringProperty(description);
	}

	@Override
	public SimpleStringProperty getVerantwoordelijkeProperty() {
		return new SimpleStringProperty(verantwoordelijke.getNaam());
	}

	@Override
	public SimpleStringProperty getGastSprekerProperty() {
		return new SimpleStringProperty(gastspreker);
	}

}

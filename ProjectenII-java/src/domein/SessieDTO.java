package domein;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringPropertyBase;

public class SessieDTO implements Sessie {
	private int id;
	private String titel;
	private String description;
	private int aantalPlaatsen;
	private String ITlabLokaal;
	private String verantwoordelijke;
	private LocalDateTime start;
	private LocalDateTime einde;
	private String gastSpreker;
	private List<Media> media;
	private boolean isGeopendGeweest;
	private String currentState;

	public SessieDTO(	String titel,
						String description,
						int aantalPlaatsen,
						String ITlabLokaal,
						String verantwoordelijke,
						LocalDateTime start,
						LocalDateTime einde,
						String gastSpreker,
						String state) {
		super();
		setTitel(titel);
		setDescription(description);
		setAantalPlaatsen(aantalPlaatsen);
		setITlabLokaal(ITlabLokaal);
		setVerantwoordelijke(verantwoordelijke);
		setStart(start);
		setEinde(einde);
		setGastSpreker(gastSpreker);
		setCurrentState(state);
	}

	public SessieDTO(ITlabSessie s) {
		setId(s.getId());
		setTitel(s.getTitel());
		setDescription(s.getDescription());
		setAantalPlaatsen(s.getAantalPlaatsen());
		setITlabLokaal(s.getITlabLokaal()
						.getLokaalcode());
		setVerantwoordelijke(s	.getVerantwoordelijke()
								.getGebruikersnaam());
		setStart(s.getStart());
		setEinde(s.getEinde());
		setGastSpreker(s.getGastspreker());
		setMedia(s.getMedia());
		setGeopendGeweest(s.isGeopendGeweest());
		this.setCurrentState(s	.getCurrentState()
								.toString());
	}

	@Override
	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int getAantalPlaatsen() {
		return aantalPlaatsen;
	}

	public void setAantalPlaatsen(int aantalPlaatsen) {
		this.aantalPlaatsen = aantalPlaatsen;
	}

	@Override
	public String getITlabLokaalString() {
		return ITlabLokaal;
	}

	public void setITlabLokaal(String ITlabLokaal) {
		this.ITlabLokaal = ITlabLokaal;
	}

	public String getVerantwoordelijkeString() {
		return verantwoordelijke;
	}

	public void setVerantwoordelijke(String verantwoordelijke2) {
		this.verantwoordelijke = verantwoordelijke2;
	}

	@Override
	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	@Override
	public LocalDateTime getEinde() {
		return einde;
	}

	public void setEinde(LocalDateTime einde) {
		this.einde = einde;
	}

	public String getGastSpreker() {
		return gastSpreker;
	}

	public void setGastSpreker(String gastSpreker) {
		this.gastSpreker = gastSpreker;
	}

	@Override
	public List<Media> getMedia() {
		return media;
	}

	public void setMedia(List<Media> media) {
		this.media = media;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setGeopendGeweest(boolean geopendGeweest) {
		isGeopendGeweest = geopendGeweest;
	}

	public String getCurrentStateString() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((titel == null) ? 0 : titel.hashCode());
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
		SessieDTO other = (SessieDTO) obj;
		if (id != other.id)
			return false;
		if (titel == null) {
			if (other.titel != null)
				return false;
		} else if (!titel.equals(other.titel))
			return false;
		return true;
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
		return null;
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
	public SimpleStringProperty getGastSprekerProperty() {
		return new SimpleStringProperty(gastSpreker);
	}

	@Override
	public String getGastspreker() {
		// TODO Auto-generated method stub
		return this.gastSpreker;
	}

	@Override
	public boolean isGeopendGeweest() {
		// TODO Auto-generated method stub
		return this.isGeopendGeweest;
	}

	@Override
	public SimpleStringProperty getVerantwoordelijkeProperty() {
		return new SimpleStringProperty(this.getVerantwoordelijkeString());
	}

	@Override
	public SimpleStringProperty getAantalAanwezigenProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringPropertyBase getAantalInschrijvingenProperty() {
		// TODO Auto-generated method stub
		return null;
	}

}

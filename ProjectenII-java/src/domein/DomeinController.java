package domein;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repositories.GebruikerDaoJpa;
import repositories.LokaalDaoJpa;
import repositories.SessieDaoJpa;

public class DomeinController {

	private PropertyChangeSupport subject;
	private ITlabSessie geselecteerdeSessie;
	private ITlabGebruiker geselecteerdeGebruiker;

	private ITLAB itlab;

	public DomeinController(InlogController lg) {
		this.itlab = new ITLAB(new SessieDaoJpa(), new GebruikerDaoJpa(), new LokaalDaoJpa(),lg);
		subject = new PropertyChangeSupport(this);
	}
	public DomeinController() {
		this.itlab = new ITLAB(new SessieDaoJpa(), new GebruikerDaoJpa(), new LokaalDaoJpa());
		subject = new PropertyChangeSupport(this);
	}
	public void MaakNieuweSessieAan(SessieDTO sessieDTO) throws IllegalArgumentException {
		itlab.maakNieuweSessieAan(sessieDTO);

	}

	public FilteredList<Sessie> geefSessies() {
		return itlab.geefSessies();

	}

	public FilteredList<Gebruiker> geefGebruikers() {
		return itlab.geefGebruikers();

	}

	public void verwijderSessie() {
		itlab.verwijderSessie(this.geselecteerdeSessie);

	}

	public void maakSessieAan(SessieDTO dto) {
		this.itlab.maakNieuweSessieAan(dto);

	}

	public void wijzigSessie(SessieDTO gewijzigdeSessie) {
		itlab.wijzigSessie(this.geselecteerdeSessie, gewijzigdeSessie);

	}

	public Sessie geefSessie(int id) {
		return itlab.getSessie(id);
	}

	public Gebruiker geefGebruiker(String gebruikersnaam) {
		return itlab.getGebruiker(gebruikersnaam);
	}

	public void wijzigGebruiker(GebruikerDTO gewijzigdeGebruiker) throws Exception {
		itlab.wijzigGebruiker(this.geselecteerdeGebruiker, gewijzigdeGebruiker);

	}

	public void filtreerSessies(String predicate) {
		itlab.filtreerSessies(predicate);
	}
	public void filtreerGebruikers(String predicate) {
		itlab.filtreerGebruikers(predicate);
	}

	public void maakGebruikerAan(GebruikerDTO gebruikerdto) throws Exception {
		itlab.maakGebruikerAan(gebruikerdto);
	}

	// getter en setter voor geselecteerde sessie
	public SessieDTO getGeselecteerdeSessie() {
		return new SessieDTO(this.geselecteerdeSessie);
	}

	public void setGeselecteerdeSessie(Sessie newValue) {
		subject.firePropertyChange("geselecteerdeSessie", this.geselecteerdeSessie, newValue);
		this.geselecteerdeSessie = (ITlabSessie) newValue;
	}

	public Gebruiker getGeselecteerdeGebruiker() {
		return geselecteerdeGebruiker;
	}

	public void setGeselecteerdeGebruiker(Gebruiker geselecteerdeGebruiker) {
		subject.firePropertyChange("geselecteerdeGebruiker", this.geselecteerdeGebruiker, geselecteerdeGebruiker);
		this.geselecteerdeGebruiker = (ITlabGebruiker) geselecteerdeGebruiker;
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		subject.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		subject.removePropertyChangeListener(pcl);
	}

	public void verstuurAankondiging(String mailtitel, String inhoud) throws Exception {
		this.itlab.verstuurAankondiging(this.geselecteerdeSessie.getTitel(), mailtitel, inhoud);
//		this.itlab.verstuurAankondiging("testsessie", mailtitel, inhoud);

	}

	public void verwijderGebruiker() {
		this.itlab.verwijderGebruiker(this.geselecteerdeGebruiker);

	}
	
	public ITlabLokaal geefLokaal(String lokaalCode) {
		return (ITlabLokaal) this.itlab.geefLokalen().stream().filter(l -> l.getLokaalcode() == lokaalCode).findFirst().get();
	}

	public ObservableList<String> geefLokalen() {
		return FXCollections.observableArrayList(itlab.geefLokalen().stream().map(Lokaal::getLokaalcode).collect(Collectors.toList()));
	}


}

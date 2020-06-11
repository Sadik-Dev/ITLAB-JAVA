package domein;

import java.awt.Desktop;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repositories.GebruikerDaoJpa;
import repositories.GenericDaoJpa;
import repositories.LokaalDaoJpa;
import repositories.SessieDaoJpa;

public class ITLAB {

	private SessieDaoJpa sessieDao;
	private GebruikerDaoJpa gebruikersDao;
	private LokaalDaoJpa lokaalDao;

	private ObservableList<ITlabGebruiker> gebruikers;
	private FilteredList<Gebruiker> filteredGebruikers;

	private ObservableList<ITlabSessie> sessies;
	private FilteredList<Sessie> filteredSessies;

	private ObservableList<ITlabLokaal> lokalen;
	private FilteredList<Lokaal> filteredLokalen;

	private ITlabGebruiker aangemeldeGebruiker;
	private InlogController loginController;

	public ITLAB(	SessieDaoJpa sessieDaoJpa,
					GebruikerDaoJpa gebruikersDaoJpa,
					LokaalDaoJpa lokaalDaoJpa) {
		this.sessieDao = sessieDaoJpa;
		this.gebruikersDao = gebruikersDaoJpa;
		this.lokaalDao = lokaalDaoJpa;
		this.gebruikers = FXCollections.observableArrayList(gebruikersDao.findAll());

		// Sessies Voor Huidige maand

		Month currentMonth = LocalDateTime	.now()
											.getMonth();
		this.sessies = FXCollections.observableArrayList(this.sessieDao.findAll());

		this.lokalen = FXCollections.observableArrayList(this.lokaalDao.findAll());

		this.filteredSessies = new FilteredList<>((ObservableList<Sessie>) (Object) sessies, p -> true); // enkel deze maand en
																											// nog niet geopend

		this.filteredGebruikers = new FilteredList<>((ObservableList<Gebruiker>) (Object) gebruikers, p -> true);

		this.filteredLokalen = new FilteredList<>((ObservableList<Lokaal>) (Object) lokalen, p -> true);

	}

	public ITLAB(	SessieDaoJpa sessieDaoJpa,
					 GebruikerDaoJpa gebruikersDaoJpa,
					 LokaalDaoJpa lokaalDaoJpa, InlogController lg) {
		this.sessieDao = sessieDaoJpa;
		this.gebruikersDao = gebruikersDaoJpa;
		this.lokaalDao = lokaalDaoJpa;
		this.gebruikers = FXCollections.observableArrayList(gebruikersDao.findAll());

		// Sessies Voor Huidige maand

		Month currentMonth = LocalDateTime	.now()
				.getMonth();
		this.sessies = FXCollections.observableArrayList(this.sessieDao.findAll());

		this.lokalen = FXCollections.observableArrayList(this.lokaalDao.findAll());

		this.filteredSessies = new FilteredList<>((ObservableList<Sessie>) (Object) sessies, p -> true); // enkel deze maand en
		// nog niet geopend

		this.filteredGebruikers = new FilteredList<>((ObservableList<Gebruiker>) (Object) gebruikers, p -> true);

		this.filteredLokalen = new FilteredList<>((ObservableList<Lokaal>) (Object) lokalen, p -> true);
		this.loginController = lg;
		aangemeldeGebruiker = lg.getITlabGebruiker();
	}
	public FilteredList<Sessie> geefSessies() {

		return this.filteredSessies;
	}

	public void maakNieuweSessieAan(SessieDTO dto) throws IllegalArgumentException {

		// VOOLEDIGE CHECK OP GELDIGHEID
		if (dto	.getTitel()
				.length() < 5)
			throw new IllegalArgumentException("Titel van Sessie is te kort");
		if (dto.getITlabLokaalString() == null)
			throw new IllegalArgumentException("Er moet een Lokaal gekozen worden");
		if (dto.getStart() == null || dto.getEinde() == null)
			throw new IllegalArgumentException("Datums mogen niet leeg zijn");
		if (dto.getAantalPlaatsen() <= 0)
			throw new IllegalArgumentException("Aantal plaatsen moet groter dan 0 zijn");

		Duration verschil = Duration.between(dto.getStart(), dto.getEinde());

		if (verschil.toMinutes() < 30)
			throw new IllegalArgumentException("Startdatum moet minstens 30 minuten zijn voor Einddatum");

		if (dto.isGeopendGeweest() == true)
			throw new IllegalArgumentException("Sessie mag niet meer gewijzigd worden");

		if (sessies.size() != 0) {

			if (this.sessies.stream()
							.filter(s -> dto.getStart()
											.isAfter(s.getStart()))
							.filter(s -> dto.getStart()
											.isBefore(s.getEinde()))
							.filter(s -> dto.getITlabLokaalString()
											.equals(s.getITlabLokaal().getLokaalcode()))
							.findAny()
							.orElse((ITlabSessie) null) != null) {

				throw new IllegalArgumentException("Het lokaal is reeds bezet op dit moment");
			}
		}

		// Alles klopt als we tot hier geraakt zijn.
		ITlabLokaal lokaal = this.lokalen.stream().filter(l->l.getLokaalcode()==dto.getITlabLokaalString()).findFirst().get();
		ITlabGebruiker verantwoordelijke = this.getGebruiker(dto.getVerantwoordelijkeString());
		ITlabSessie nieuweSessie = new ITlabSessie(
				dto.getTitel(),
				dto.getDescription(), 
				dto.getAantalPlaatsen(), 
				lokaal, 
				verantwoordelijke,
				dto.getStart(), 
				dto.getEinde(), 
				dto.getGastspreker(),
				null);
		// opslaan in db
		this.sessieDao.startTransaction();
		this.sessieDao.insert(nieuweSessie);
		this.sessieDao.commitTransaction();
		// lokaal bijhouden
		this.sessies.add(nieuweSessie);

	}

	public ITlabSessie getSessie(int id) {
		ITlabSessie sessie = this.sessies	.stream()
											.filter(p -> p.getId() == id)
											.findFirst()
											.get();

		return sessie;

	}

	public ITlabGebruiker getGebruiker(String gebruikersnaam) {
		ITlabGebruiker gebruiker = this.gebruikers	.stream()
													.filter(g -> g	.getGebruikersnaam()
																	.equals(gebruikersnaam))
													.findFirst()
													.get();

		return gebruiker;

	}

	public void wijzigGebruiker(ITlabGebruiker geselecteerdeGebruiker, GebruikerDTO dto) throws Exception {
		if (dto	.getGebruikersnaam()
				.length() < 3)
			throw new IllegalArgumentException("Gebruikersnaam is te kort !");

		if (dto.getBarcode() < 100000000000L)
			throw new IllegalArgumentException("Barcode is te kort !");
		if (!dto.getEmail()
				.matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$"))
			throw new IllegalArgumentException("Email is incorrect !");
		if (dto	.getNaam()
				.length() < 3)
			throw new IllegalArgumentException("Naam is te kort !");
		if (dto.getGebruikersType() == null)
			throw new IllegalArgumentException("GebruikersType mag niet leeg zijn !");

		gebruikers.remove(geselecteerdeGebruiker);

		GebruikerState teVerwijderenState = geselecteerdeGebruiker.getCurrentState();

		checkGebruikersnaam(dto.getGebruikersnaam()); // zal een exception throwen als het niet mag

		geselecteerdeGebruiker.wijzigGebruiker(dto);

		// DB
		GenericDaoJpa<GebruikerState> stateDao = new GenericDaoJpa<>(GebruikerState.class);
		stateDao.startTransaction();
		stateDao.delete(teVerwijderenState);
		stateDao.commitTransaction();

		gebruikersDao.startTransaction();
		gebruikersDao.update(geselecteerdeGebruiker);
		gebruikersDao.commitTransaction();
		// Local
		gebruikers.add(geselecteerdeGebruiker);
	}

	private void checkGebruikersnaam(String gebruikersnaam) throws Exception {
		gebruikers	.stream()
					.forEach(g -> {
						if (g	.getGebruikersnaam()
								.toString()
								.equals(gebruikersnaam))
							throw new IllegalArgumentException("Deze Gebruikersnaam bestaat al !");
					});
	}

	public void wijzigSessie(ITlabSessie geselecteerdeSessie, SessieDTO dto) {
		// VOLLEDIGE CHECK OP GELDIGHEID
		if (dto	.getTitel()
				.length() < 3)
			throw new IllegalArgumentException("Titel van Sessie is te kort");
		if (dto.getITlabLokaalString() == null)
			throw new IllegalArgumentException("Er moet een Lokaal gekozen worden");
		if (dto.getStart() == null || dto.getEinde() == null)
			throw new IllegalArgumentException("Datums mogen niet leeg zijn");
		if (dto.getAantalPlaatsen() <= 0)
			throw new IllegalArgumentException("Aantal plaatsen moet groter dan 0 zijn");

		Duration verschill = Duration.between(dto.getStart(), dto.getEinde());

		if (verschill.toMinutes() < 30)
			throw new IllegalArgumentException("Startdatum moet minstens 30 minuten zijn voor Einddatum");

		if (dto.isGeopendGeweest() == true)
			throw new IllegalArgumentException("Sessie mag niet meer gewijzigd worden");

		if (dto .getCurrentStateString()
				.equals(SessieStateEnum.AanmeldenEnRegistratieGeslotenState)
			|| dto	.getCurrentStateString()
					.equals(SessieStateEnum.RegistratieGeslotenAanmeldenOpenState)) {
			throw new IllegalArgumentException("Sessie mag niet meer gewijzigd worden");
		}

		// is dit nodig?
//		dto	.getFeedbackEntries()
//			.stream()
//			.forEach(f -> {
//				if (f	.getInhoud()
//						.length() < 1)
//					throw new IllegalArgumentException("Feedback Inhoud mag niet leeg zijn");
//			});

		if (geselecteerdeSessie == null) {
			throw new IllegalArgumentException("Deze sessie bestaat niet");
		}

		ITlabLokaal lokaal = this.lokalen.stream().filter(l->l.getLokaalcode()==dto.getITlabLokaalString()).findFirst().get();
		ITlabGebruiker verandwoordelijke = this.getGebruiker(dto.getVerantwoordelijkeString());
		ITlabSessie sessieGewijzigd = new ITlabSessie(
													dto.getTitel(),
													dto.getDescription(), 
													dto.getAantalPlaatsen(), 
													lokaal, 
													verandwoordelijke,
													dto.getStart(), 
													dto.getEinde(), 
													dto.getGastspreker(),
													null);
		geselecteerdeSessie.wijzigSessie(sessieGewijzigd);
		// DB
		sessieDao.startTransaction();
		sessieDao.update(geselecteerdeSessie);
		sessieDao.commitTransaction();
		// Local
		sessies.add(geselecteerdeSessie);

	}

	public void verwijderSessie(ITlabSessie sessie) {
		SessieStateEnum state = sessie	.getCurrentState()
										.getDiscriminator();

		if (state.equals(SessieStateEnum.RegistratieGeslotenAanmeldenOpenState)) {
			throw new IllegalStateException("De sessie mag nog niet geopend zijn.");
		}
		if (state.equals(SessieStateEnum.AanmeldenEnRegistratieGeslotenState)) {
			throw new IllegalStateException("De sessie mag nog niet afgelopen zijn.");
		}

		// DB
		sessieDao.startTransaction();
		this.sessieDao.delete(sessie);
		sessieDao.commitTransaction();
		// LOCAL
		sessies.remove(sessie);
	}

	public FilteredList<Gebruiker> geefGebruikers() {
		if (this.filteredGebruikers == null || this.filteredGebruikers.size() == 0) {
			this.filteredGebruikers = new FilteredList<>((ObservableList<Gebruiker>) (Object) gebruikers, p -> true);
		}

		return this.filteredGebruikers;
	}

	public void verwijderGebruiker(ITlabGebruiker teVerwijderenGebruiker) {

		// DB
		this.gebruikersDao.startTransaction();
		this.gebruikersDao.delete(teVerwijderenGebruiker);
		this.gebruikersDao.commitTransaction();
		// Local
		this.gebruikers.remove(teVerwijderenGebruiker);

	}

	public void filtreerSessies(String value) {

	
		this.filteredSessies.setPredicate(s -> {
			if (value.equals("") || value.isBlank()) {
				return true;
			}

			return s.getTitel()
					.toLowerCase()
					.contains(value.toLowerCase());
		});
	}

	public void filtreerGebruikers(String value) {
		
		//DOES NOT WORK WELL
	/*	if (this.filteredGebruikers.size() == 0) { // eerst opvullen
			filteredGebruikers = new FilteredList<>(FXCollections.observableArrayList(gebruikers.stream()
																								.sorted(Comparator.comparing(
																									Gebruiker::getGebruikersnaam))
																								.collect(Collectors.toList())),
													p -> true);
		}
		this.filteredGebruikers.setPredicate(s -> {
			if (value == null || value.isBlank()) {
				return true;
			}
*/
		this.filteredGebruikers.setPredicate(s -> {
			if (value.equals("") || value.isBlank()) {
				return true;
			}
			return s.getNaam()
					.toLowerCase()
					.contains(value.toLowerCase());
		});

	}

	public void maakGebruikerAan(GebruikerDTO dto) throws Exception {

		if (dto.getGebruikersnaam() == null || dto	.getGebruikersnaam()
													.isBlank()
			|| dto	.getGebruikersnaam()
					.length() == 0)
			throw new IllegalArgumentException("Gebruikersnaam mag niet leeg zijn of is incorrect");
		if (dto.getNaam() == null || dto.getNaam()
										.isBlank()
			|| dto	.getNaam()
					.length() == 0)
			throw new IllegalArgumentException("Naam mag niet leeg zijn");
		if (String	.valueOf(dto.getBarcode())
					.length() != 13)
			throw new IllegalArgumentException("Barcode is incorrect");
		if (!dto.getEmail()
				.matches("^(.+)@(.+)$"))
			throw new IllegalArgumentException("Email is incorrect");
		if (dto.getGebruikersType() == null)
			throw new IllegalArgumentException("GebruikersType mag niet leeg zijn");

		checkGebruikersnaam(dto.getGebruikersnaam()); // zal exception throwen als het niet mag

		ITlabGebruiker gebruiker = new ITlabGebruiker(dto);

		// DB
		this.gebruikersDao.startTransaction();
		this.gebruikersDao.insert(gebruiker);
		this.gebruikersDao.commitTransaction();

		if(loginController != null){
			if(gebruiker.getGebruikersType().equals(GebruikersType.HoofdVerantwoordelijke) ||
					gebruiker.getGebruikersType().equals(GebruikersType.Verantwoordelijke))
				loginController.addVerantwoordelijke(gebruiker);
		}

		// Local
		gebruikers.add(gebruiker);
	}

	public void verstuurAankondiging(String sessieTitel, String mailtitel, String inhoud) throws Exception {
		Desktop desktop;
		if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
			try {
				String mailTo = geefEersteOntvanger(); // deze moet ingevuld zijn
//				String mailTo = "Simon.dewilde@gmail.com";
				String cc = geefAlleOntvangerZonderEerste(); // hier moeten ;s tussen
//				String cc = ""; // hier moeten ;s tussen
				String subject = changeToURICode(String.format("AANKONDIGING %s - %s", sessieTitel, mailtitel));
				String body = changeToURICode(inhoud);

				final String mailURIStr = String.format("mailto:%s?subject=%s&bcc=%s&body=%s", mailTo, subject, cc, body);
				final URI mailURI = new URI(mailURIStr);

				desktop.mail(mailURI);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			throw new RuntimeException("desktop doesn't support mailto...)");
		}

	}

	private String geefAlleOntvangerZonderEerste() {
		StringBuilder emails = new StringBuilder();
		gebruikers	.subList(1, gebruikers.size())
					.stream()
					.forEach(g -> {
						emails.append(g.getEmail());
						emails.append(";");
					});

		return emails.toString();
	}

	private String geefEersteOntvanger() {
		return gebruikers	.get(0)
							.getEmail();
	}

	private static String changeToURICode(String subject) {
		return subject	.replaceAll(" ", "%20")
						.replaceAll("\n", "%0D"); // in de URI moeten de spaties in %20 veranderen en \n in %0D
	}

	public FilteredList<Lokaal> geefLokalen() {
		return filteredLokalen;
	}
	
	public boolean login(String gebruikersnaam, String password){
		boolean succes = loginController.login(gebruikersnaam,password);
		if(succes)
			aangemeldeGebruiker = gebruikersDao.getGebruikerByGebruikersnaam(gebruikersnaam);

		return succes;

	}
}

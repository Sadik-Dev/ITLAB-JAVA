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

	private InlogController loginController;

	public ITLAB(	SessieDaoJpa sessieDaoJpa,
					GebruikerDaoJpa gebruikersDaoJpa,
					LokaalDaoJpa lokaalDaoJpa,
					InlogController lg) {
		this.sessieDao = sessieDaoJpa;
		this.gebruikersDao = gebruikersDaoJpa;
		this.lokaalDao = lokaalDaoJpa;
		this.gebruikers = FXCollections.observableArrayList(gebruikersDao.findAll());

		// Sessies Voor Huidige maand

		Month currentMonth = LocalDateTime	.now()
											.getMonth();
		this.sessies = FXCollections.observableArrayList(this.sessieDao.findAll());

		this.lokalen = FXCollections.observableArrayList(this.lokaalDao.findAll());

		this.filteredSessies = new FilteredList<>(	(ObservableList<Sessie>) (Object) sessies,
													p -> p	.getStart()
															.getMonth()
															.equals(currentMonth)); // enkel deze maand
		if (this.filteredSessies.size() == 0) {
			this.filteredSessies.setPredicate(p -> true);
		}

		this.filteredGebruikers = new FilteredList<>((ObservableList<Gebruiker>) (Object) gebruikers, p -> true);

		this.filteredLokalen = new FilteredList<>((ObservableList<Lokaal>) (Object) lokalen, p -> true);

		this.loginController = lg;
	}

	public FilteredList<Sessie> geefSessies() {

		return this.filteredSessies;
	}

	public void maakNieuweSessieAan(SessieDTO dto) throws IllegalArgumentException {

		if (dto.getITlabLokaalString() == null)
			throw new IllegalArgumentException("Er moet een Lokaal gekozen worden");
		if (dto.getStart() == null || dto.getEinde() == null)
			throw new IllegalArgumentException("Datums mogen niet leeg zijn");
		if (dto.getAantalPlaatsen() <= 0)
			throw new IllegalArgumentException("Aantal plaatsen moet groter dan 0 zijn");

		Duration verschil = Duration.between(dto.getStart(), dto.getEinde());

		if (verschil.toMinutes() < 30)
			throw new IllegalArgumentException("Startdatum moet minstens 30 minuten zijn voor Einddatum");

		if (sessies.size() != 0) {

			if (this.sessies.stream()
							.filter(s -> dto.getStart()
											.isAfter(s.getStart()))
							.filter(s -> dto.getStart()
											.isBefore(s.getEinde()))
							.filter(s -> dto.getITlabLokaalString()
											.equals(s	.getITlabLokaal()
														.getLokaalcode()))
							.findAny()
							.orElse((ITlabSessie) null) != null) {

				throw new IllegalArgumentException("Het lokaal is reeds bezet op dit moment");
			}
		}

		// Alles klopt als we tot hier geraakt zijn.
		ITlabLokaal lokaal = this.lokalen	.stream()
											.filter(l -> l	.getLokaalcode()
															.equals(dto.getITlabLokaalString()))
											.findFirst()
											.get();
		ITlabGebruiker verantwoordelijke = this.getGebruiker(dto.getVerantwoordelijkeString());

		// opslaan in db
		this.sessieDao.startTransaction();
		ITlabSessie nieuweSessie = new ITlabSessie(	dto.getTitel(),
													dto.getDescription(),
													dto.getAantalPlaatsen(),
													lokaal,
													verantwoordelijke,
													dto.getStart(),
													dto.getEinde(),
													dto.getGastspreker(),
													null);
		this.sessies.add(nieuweSessie);

		this.sessieDao.insert(nieuweSessie);

		this.sessieDao.commitTransaction();

	}

	public ITlabSessie getSessie(String titel) {
		ITlabSessie sessie = this.sessies	.stream()
											.filter(p -> p	.getTitel()
															.equals(titel))
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

	public ITlabGebruiker getGebruikerbyNaam(String naam) {
		ITlabGebruiker gebruiker = this.gebruikers	.stream()
													.filter(g -> g	.getNaam()
																	.equals(naam))
													.findFirst()
													.get();

		return gebruiker;

	}

	public void wijzigGebruiker(ITlabGebruiker geselecteerdeGebruiker, GebruikerDTO dto) throws Exception {

		GebruikerState teVerwijderenState = geselecteerdeGebruiker.getCurrentState();

		// DB
		GenericDaoJpa<GebruikerState> stateDao = new GenericDaoJpa<>(GebruikerState.class);
		stateDao.startTransaction();
		stateDao.delete(teVerwijderenState);
		stateDao.commitTransaction();

		gebruikersDao.startTransaction();
		geselecteerdeGebruiker.wijzigGebruiker(dto);
		gebruikersDao.commitTransaction();

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

	public void wijzigSessie(ITlabSessie geselecteerdeSessie, SessieDTO dto) throws Exception {

		if (dto.getITlabLokaalString() == null)
			throw new IllegalArgumentException("Er moet een Lokaal gekozen worden");
		if (dto.getStart() == null || dto.getEinde() == null)
			throw new IllegalArgumentException("Datums mogen niet leeg zijn");
		if (dto.getAantalPlaatsen() <= 0)
			throw new IllegalArgumentException("Aantal plaatsen moet groter dan 0 zijn");

		Duration verschill = Duration.between(dto.getStart(), dto.getEinde());

		if (verschill.toMinutes() < 30)
			throw new IllegalArgumentException("Startdatum moet minstens 30 minuten zijn voor Einddatum");

		if (dto	.getCurrentStateString()
				.equals(SessieStateEnum.AanmeldenEnRegistratieGeslotenState.toString())
			|| dto	.getCurrentStateString()
					.equals(SessieStateEnum.RegistratieGeslotenAanmeldenOpenState.toString())) {
			throw new IllegalArgumentException("Sessie mag niet meer gewijzigd worden");
		}

		if (geselecteerdeSessie == null) {
			throw new IllegalArgumentException("Deze sessie bestaat niet");
		}

		ITlabLokaal lokaal = this.lokalen	.stream()
											.filter(l -> l.getLokaalcode() == dto.getITlabLokaalString())
											.findFirst()
											.get();
		ITlabGebruiker verandwoordelijke = this.getGebruiker(dto.getVerantwoordelijkeString());
		ITlabSessie sessieGewijzigd = new ITlabSessie(	dto.getTitel(),
														dto.getDescription(),
														dto.getAantalPlaatsen(),
														lokaal,
														verandwoordelijke,
														dto.getStart(),
														dto.getEinde(),
														dto.getGastspreker(),
														null);

		// DB
		sessieDao.startTransaction();

		geselecteerdeSessie.wijzigSessie(sessieGewijzigd);

		sessieDao.commitTransaction();

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

		sessies.remove(sessie);
		sessieDao.delete(sessie);

		sessieDao.commitTransaction();
		// LOCAL
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
		this.gebruikers.remove(teVerwijderenGebruiker);
		this.gebruikersDao.delete(teVerwijderenGebruiker);
		this.gebruikersDao.commitTransaction();

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

		// DOES NOT WORK WELL
		/*
		 * if (this.filteredGebruikers.size() == 0) { // eerst opvullen
		 * filteredGebruikers = new
		 * FilteredList<>(FXCollections.observableArrayList(gebruikers.stream()
		 * .sorted(Comparator.comparing( Gebruiker::getGebruikersnaam))
		 * .collect(Collectors.toList())), p -> true); }
		 * this.filteredGebruikers.setPredicate(s -> { if (value == null ||
		 * value.isBlank()) { return true; }
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

		checkGebruikersnaam(dto.getGebruikersnaam()); // zal exception throwen als het niet mag

		// DB
		this.gebruikersDao.startTransaction();

		ITlabGebruiker gebruiker = new ITlabGebruiker(dto);
		gebruikers.add(gebruiker);

		this.gebruikersDao.insert(gebruiker);
		this.gebruikersDao.commitTransaction();

		if (loginController != null) {
			if (gebruiker	.getGebruikersType()
							.equals(GebruikersType.HoofdVerantwoordelijke)
				|| gebruiker.getGebruikersType()
							.equals(GebruikersType.Verantwoordelijke))
				loginController.addVerantwoordelijke(gebruiker);
		}

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
						String email = g.getEmail();
						if (email != null) {
							emails.append(email);
							emails.append(";");
						}
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
}

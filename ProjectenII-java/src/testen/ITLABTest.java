package testen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.GebruikersType;
import domein.ITLAB;
import domein.ITlabGebruiker;
import domein.ITlabLokaal;
import domein.ITlabSessie;
import domein.LokaalType;
import domein.SessieDTO;
import domein.SessieState;
import domein.SessieStateEnum;
import repositories.GebruikerDaoJpa;
import repositories.LokaalDaoJpa;
import repositories.SessieDaoJpa;

@ExtendWith(MockitoExtension.class)
class ITLABTest {

	private ITlabSessie testSessie;
	private ITlabLokaal testITlabLokaal;
	private static ITlabGebruiker testVerantwoordelijke;

	@Mock
	private SessieDaoJpa mockSessieDao;
	@Mock
	private GebruikerDaoJpa mockGebruikersDao;
	@Mock
	private LokaalDaoJpa mockLokaalDao;

	private ITLAB itlab;
	private List<ITlabGebruiker> gebruikerslijst;
	private List<ITlabLokaal> lokaallijst;
	private List<ITlabSessie> sessielijst;

	@BeforeEach
	public void before() {

		this.itlab = new ITLAB(mockSessieDao, mockGebruikersDao, mockLokaalDao);
		this.testITlabLokaal = new ITlabLokaal(LokaalType.Leslokaal, 60, "B1.023");
		this.testVerantwoordelijke = new ITlabGebruiker("123456tv",
														GebruikersType.Verantwoordelijke,
														"afbeelding",
														Long.parseLong("1234567891234"),
														"testverantwoordelijke@hogent.be",
														"testverantwoordelijke");
		this.testSessie = new ITlabSessie(	"testSessie",
											"Dit is een testsessie",
											50,
											testITlabLokaal,
											testVerantwoordelijke,
											LocalDateTime	.now()
															.plusDays(4),
											LocalDateTime	.now()
															.plusDays(4)
															.plusHours(2),
											"Geen",
											null);

		this.gebruikerslijst = new ArrayList<>();
		this.gebruikerslijst.add(testVerantwoordelijke);
		this.gebruikerslijst.add(new ITlabGebruiker("azerazer",
													GebruikersType.Gebruiker,
													null,
													Long.parseLong("3216549873215"),
													"zerazerazer",
													"azerqsfsdfv"));

		this.lokaallijst = new ArrayList<>();
		this.lokaallijst.add(testITlabLokaal);
		this.lokaallijst.add(new ITlabLokaal(LokaalType.Auditorium, 100, "dqsdfqsdf"));

		this.sessielijst = new ArrayList<>();
		this.sessielijst.add(testSessie);
		this.sessielijst.add(new ITlabSessie(	"azer",
												"zer",
												20,
												new ITlabLokaal(LokaalType.Auditorium, 30, "qsdfqsd"),
												testVerantwoordelijke,
												LocalDateTime	.now()
																.plusDays(20),
												LocalDateTime	.now()
																.plusDays(20)
																.plusMinutes(60),
												"azerazer",
												null));

		Mockito	.when(this.mockGebruikersDao.findAll())
				.thenReturn(gebruikerslijst);

		Mockito	.when(this.mockLokaalDao.findAll())
				.thenReturn(lokaallijst);

		Mockito	.when(this.mockSessieDao.findAll())
				.thenReturn(sessielijst);

	}

	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	// sessie kunnen aanmaken
	private static Stream<Arguments> opsommingGeldigeWaarden() {
		return Stream.of(
			Arguments.of(new ITlabLokaal(LokaalType.Auditorium, 80, "BCON"),
				testVerantwoordelijke,
				"titel1",
				"dit is een hele mooie sessie",
				LocalDateTime	.now()
								.plusDays(1),
				LocalDateTime	.now()
								.plusDays(1)
								.plusMinutes(120),
				"gastspreker1"),
			Arguments.of(new ITlabLokaal(LokaalType.Auditorium, 80, "BCON"),
				testVerantwoordelijke,
				"titel2",
				"dit is nog een hele mooie sessie",
				LocalDateTime	.now()
								.plusMonths(5),
				LocalDateTime	.now()
								.plusMonths(5)
								.plusMinutes(60),
				"gastspreker2"),
			Arguments.of(new ITlabLokaal(LokaalType.Auditorium, 80, "BCON"),
				testVerantwoordelijke,
				"titel3",
				"dit is nog een hele mooie sessie",
				LocalDateTime	.now()
								.plusMonths(2),
				LocalDateTime	.now()
								.plusMonths(2)
								.plusMinutes(90),
				"gastspreker3"));
	}

	@ParameterizedTest
	@MethodSource("opsommingGeldigeWaarden")
	public void geldigeWaarden_makenSessieAan(	ITlabLokaal ITlabLokaal,
												ITlabGebruiker verandw,
												String titel,
												String description,
												LocalDateTime start,
												LocalDateTime eind,
												String gast) {
		this.itlab.maakNieuweSessieAan(new SessieDTO(new ITlabSessie(	titel,
																		description,
																		ITlabLokaal.getAantalPlaatsen(),
																		ITlabLokaal,
																		verandw,
																		start,
																		eind,
																		gast,
																		null)));

	}

	// minder dan 24 uur?
//	@Test
//	public void datum_MinderDan1DagInToekomstThrowsIllegalArgumentException() {
//		Assertions.assertThrows(IllegalArgumentException.class,
//			() -> this.itlab.maakNieuweSessieAan(new SessieDTO(new ITlabSessie(	"titel1",
//																				"dit is een hele mooie sessie",
//																				50,
//																				testLokaal,
//																				testVerantwoordelijke,
//																				LocalDateTime	.now()
//																								.plusHours(23),
//																				LocalDateTime	.now()
//																								.plusHours(25),
//																				"gastspreker1",
//																				null))));
//		Assertions.assertThrows(IllegalArgumentException.class,
//			() -> this.itlab.maakNieuweSessieAan(new SessieDTO(new ITlabSessie(	"titel1",
//																				"dit is een hele mooie sessie",
//																				50,
//																				testLokaal,
//																				testVerantwoordelijke,
//																				LocalDateTime	.now()
//																								.minusMonths(1)
//																								.minusMinutes(1),
//																				LocalDateTime	.now()
//																								.minusMonths(1)
//																								.plusMinutes(120),
//																				"gastspreker1",
//																				null))));
//	}

//	@ParameterizedTest
//	@ValueSource(longs = { 1, 15, 29, 30, 90, 120 })
//	public void beginDatum_NaEindatum_ThrowsIllegalArgumentException(Long minutenVerschil) {
//		Assertions.assertThrows(IllegalArgumentException.class,
//			() -> this.itlab.maakNieuweSessieAan(new SessieDTO(new ITlabSessie(	"titel1",
//																				"dit is een hele mooie sessie",
//																				60,
//																				testLokaal,
//																				testVerantwoordelijke,
//																				LocalDateTime	.now()
//																								.plusMonths(1)
//																								.plusMinutes(minutenVerschil),
//																				LocalDateTime	.now()
//																								.plusMonths(1),
//																				"gastspreker1",
//																				null))));
//	}
//
//	@ParameterizedTest
//	@ValueSource(longs = { 0, 1, 15, 29 })
//	public void beginDatum_minderDan30minVoorEindatum_ThrowsIllegalArgumentException(Long duurInMinuten) {
//		Assertions.assertThrows(IllegalArgumentException.class,
//			() -> this.itlab.maakNieuweSessieAan(new SessieDTO(new ITlabSessie(	"titel1",
//																				"dit is een hele mooie sessie",
//																				30,
//																				testLokaal,
//																				testVerantwoordelijke,
//																				LocalDateTime	.now()
//																								.plusMonths(1),
//																				LocalDateTime	.now()
//																								.plusMonths(1)
//																								.plusMinutes(duurInMinuten),
//																				"gastspreker1",
//																				null))));
//	}
//
//	@Test
//	public void datum_deelsInVerleden_deelsInToekomstThrowsIllegalArgumentException() {
//		Assertions.assertThrows(IllegalArgumentException.class,
//			() -> this.itlab.maakNieuweSessieAan(new SessieDTO(new ITlabSessie(	"titel1",
//																				"dit is een hele mooie sessie",
//																				70,
//																				testLokaal,
//																				testVerantwoordelijke,
//																				LocalDateTime	.now()
//																								.minusHours(1),
//																				LocalDateTime	.now()
//																								.plusMinutes(1),
//																				"gastspreker1",
//																				null))));
//		Assertions.assertThrows(IllegalArgumentException.class,
//			() -> this.itlab.maakNieuweSessieAan(new SessieDTO(new ITlabSessie(	"titel1",
//																				"dit is een hele mooie sessie",
//																				80,
//																				testLokaal,
//																				testVerantwoordelijke,
//																				LocalDateTime	.now()
//																								.minusMinutes(1),
//																				LocalDateTime	.now()
//																								.plusMinutes(120),
//																				"gastspreker1",
//																				null))));
//	}
//
//	@Test
//	public void lokaalAlBezet_ThrowsIllegalArgumentException() {
//		this.itlab.maakNieuweSessieAan(new SessieDTO(new ITlabSessie(	"titel1",
//											"dit is een hele mooie sessie",
//											50,
//											testLokaal,
//											testVerantwoordelijke,
//											LocalDateTime	.now()
//															.plusDays(23),
//											LocalDateTime	.now()
//															.plusDays(23)
//															.plusHours(2),
//											"gastspreker1",
//											null)));
//		Assertions.assertThrows(IllegalArgumentException.class,
//			() -> this.itlab.maakNieuweSessieAan(new SessieDTO(new ITlabSessie(	"titel2",
//									"dit is een overlappende sessie",
//									50,
//									testLokaal,
//									testVerantwoordelijke,
//									LocalDateTime	.now()
//													.plusDays(23),
//									LocalDateTime	.now()
//													.plusDays(23)
//													.plusHours(1),
//									"gastspreker1",
//									null))));
//	}

	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	// Sessie kunnen verwijderen

	@Test
	public void verwijderSessie_sessieKanVerwijderdWorden_Succesvol() {
		this.itlab.maakNieuweSessieAan(new SessieDTO(testSessie));
		this.itlab.verwijderSessie(testSessie);
	}

	@Test
	public void verwijderSessie_registratieGesloten_throwsException() {
		this.itlab.maakNieuweSessieAan(new SessieDTO(testSessie));

		testSessie.toState(new SessieState(testSessie, SessieStateEnum.RegistratieGeslotenAanmeldenOpenState));

		Assertions.assertThrows(IllegalStateException.class, () -> this.itlab.verwijderSessie(testSessie));
	}

	@Test
	public void verwijderSessie_aanmeldenGesloten_throwsException() {
		this.itlab.maakNieuweSessieAan(new SessieDTO(testSessie));
		testSessie.toState(new SessieState(testSessie, SessieStateEnum.AanmeldenEnRegistratieGeslotenState));
		Assertions.assertThrows(IllegalStateException.class, () -> this.itlab.verwijderSessie(testSessie));
	}
	//
	// aanwezigheidstest onnodig --> men kan enkel aanmelden als de sessie al open
	// is, en dat wordt al getest hierboven
	//

	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	// Sessie kunnen wijzigen
	@Test
	public void wijzigSessie_sessieKanGewijzigdWorden_Succesvol() {
		SessieDTO dto = new SessieDTO(testSessie);

		this.itlab.maakNieuweSessieAan(dto);

		SessieDTO gewijzigdeSessieDTO = dto;
		gewijzigdeSessieDTO.setAantalPlaatsen(20);

		this.itlab.wijzigSessie(testSessie, gewijzigdeSessieDTO);
		Assertions.assertEquals(gewijzigdeSessieDTO, this.itlab.getSessie(gewijzigdeSessieDTO.getId()));
		Assertions.assertEquals(gewijzigdeSessieDTO.getAantalPlaatsen(),
			this.itlab	.getSessie(gewijzigdeSessieDTO.getId())
						.getAantalPlaatsen());
	}
/*
	@Test
	public void wijzigSessie_registratieGesloten_throwsException() {
		SessieDTO dto = new SessieDTO(testSessie);

		this.itlab.maakNieuweSessieAan(dto);

		dto.setCurrentState(SessieStateEnum.RegistratieGeslotenAanmeldenOpenState.name());

		assertThrows(IllegalArgumentException.class, () -> this.itlab.wijzigSessie(testSessie, dto));
	}

	@Test
	public void wijzigSessie_RegistratieEnAanmeldenGesloten_throwsException() {
		SessieDTO dto = new SessieDTO(testSessie);

		this.itlab.maakNieuweSessieAan(dto);

		dto.setCurrentState(SessieStateEnum.AanmeldenEnRegistratieGeslotenState.name());

		assertThrows(IllegalArgumentException.class, () -> this.itlab.wijzigSessie(testSessie, dto));
	}

//	@Test
//	public void wijzigSessie_ongeldigeGegevens_throwsException() {
//		SessieDTO dto = new SessieDTO(testSessie);
//
//		this.itlab.maakNieuweSessieAan(dto);
//
//		SessieDTO gewijzigdeSessie = dto;
//		gewijzigdeSessie.setAantalPlaatsen(-50);
//
//		assertThrows(IllegalArgumentException.class, () -> this.itlab.wijzigSessie(gewijzigdeSessie));
//	}

	// sessieoverzicht kunnen filteren
	@Test
	public void filterSessie_geeftGefilterdeLijstTerug() {
		this.itlab.maakNieuweSessieAan(new SessieDTO(testSessie));
		SessieDTO andereDto = new SessieDTO(testSessie);
		ITlabSessie andereSessie = new ITlabSessie(	"azer",
													"zer",
													20,
													new ITlabLokaal(LokaalType.Auditorium, 30, "qsdfqsd"),
													testVerantwoordelijke,
													LocalDateTime	.now()
																	.plusDays(20),
													LocalDateTime	.now()
																	.plusDays(20)
																	.plusMinutes(60),
													"azerazer",
													null);

		this.itlab.maakNieuweSessieAan(new SessieDTO(andereSessie));

		// filteren
		itlab.filtreerSessies("ab");

		// enkel de laatste sessie mag getoont worden
		assertEquals(1,
			this.itlab	.geefSessies()
						.size());

	}
*/
}

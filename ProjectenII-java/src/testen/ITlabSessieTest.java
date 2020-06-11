package testen;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import domein.GebruikersType;
import domein.ITlabGebruiker;
import domein.ITlabSessie;
import domein.ITlabLokaal;
import domein.LokaalType;
import domein.SessieStateEnum;
import repositories.GebruikerDaoJpa;
import repositories.SessieDaoJpa;

class ITlabSessieTest {
	

	private static ITlabSessie testSessie;
	private static ITlabLokaal testITlabLokaal;
	private static ITlabGebruiker testVerantwoordelijke;
	private static ITlabGebruiker testGebruiker;

	@Mock
	private SessieDaoJpa mockSessieDao;
	@Mock
	private GebruikerDaoJpa mockGebruikersDao;
	
	@BeforeEach
	public void before() {
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
		this.testGebruiker = new ITlabGebruiker("123456tg",
												GebruikersType.Gebruiker,
												"afbeelding",
												Long.parseLong("9876543219876"),
												"testgebruiker@hogent.be",
												"testgebruiker");
	}

	
	
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

	
	//sessie aanmaken
	@ParameterizedTest
	@MethodSource("opsommingGeldigeWaarden")
	public void geldigeWaarden_makenSessieAan(	ITlabLokaal ITlabLokaal,
												ITlabGebruiker verandw,
												String titel,
												String description,
												LocalDateTime start,
												LocalDateTime eind,
												String gast) {
		ITlabSessie s1 = new ITlabSessie (	titel,
							description,
							ITlabLokaal.getAantalPlaatsen(),
				ITlabLokaal,
							verandw,
							start,
							eind,
							gast,
							null);
		
		Assertions.assertEquals(ITlabLokaal.getAantalPlaatsen(), s1.getAantalPlaatsen());
		Assertions.assertEquals(verandw, s1.getVerantwoordelijke());
		Assertions.assertTrue(s1 instanceof ITlabSessie);
		Assertions.assertEquals(SessieStateEnum.RegistratieOpenAanmeldenGeslotenState,
			s1	.getCurrentState()
				.getDiscriminator());
		Assertions.assertTrue(s1.isGeopendGeweest() == false);
	}

	// minder dan 24 uur?
	@Test
	public void datum_MinderDan1DagInToekomstThrowsIllegalArgumentException() {
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new ITlabSessie(	"titel1",
									"dit is een hele mooie sessie",
									50,
					testITlabLokaal,
									testVerantwoordelijke,
									LocalDateTime	.now()
													.plusHours(23),
									LocalDateTime	.now()
													.plusHours(25),
									"gastspreker1",
									null));
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new ITlabSessie(	"titel1",
									"dit is een hele mooie sessie",
									50,
					testITlabLokaal,
									testVerantwoordelijke,
									LocalDateTime	.now()
													.minusMonths(1)
													.minusMinutes(1),
									LocalDateTime	.now()
													.minusMonths(1)
													.plusMinutes(120),
									"gastspreker1",
									null));
	}

	@Test
	public void datum_deelsInVerleden_deelsInToekomstThrowsIllegalArgumentException() {
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new ITlabSessie(	"titel1",
									"dit is een hele mooie sessie",
									70,
					testITlabLokaal,
									testVerantwoordelijke,
									LocalDateTime	.now()
													.minusHours(1),
									LocalDateTime	.now()
													.plusMinutes(1),
									"gastspreker1",
									null));
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> new ITlabSessie(	"titel1",
									"dit is een hele mooie sessie",
									80,
					testITlabLokaal,
									testVerantwoordelijke,
									LocalDateTime	.now()
													.minusMinutes(1),
									LocalDateTime	.now()
													.plusMinutes(120),
									"gastspreker1",
									null));
	}
	
	//sessie wijzigen
	
	@Test
	public void wijzigSessie_ongeldigeGegevens_throwsException() {
//		SessieDTO dto = new SessieDTO(testSessie);
//
//		this.itlab.maakNieuweSessieAan(dto);
//
//		SessieDTO gewijzigdeSessie = dto;
//		gewijzigdeSessie.setAantalPlaatsen(-50);
//
//		assertThrows(IllegalArgumentException.class, () -> this.itlab.wijzigSessie(gewijzigdeSessie));
	}

}

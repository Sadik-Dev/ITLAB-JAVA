package testen;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domein.GebruikerDTO;
import domein.GebruikersType;
import domein.ITLAB;
import domein.ITlabGebruiker;
import repositories.GebruikerDaoJpa;
import repositories.LokaalDaoJpa;
import repositories.SessieDaoJpa;

@ExtendWith(MockitoExtension.class)
class ITlabGebruikerTest {

	private ITlabGebruiker testGebruiker;

	@Mock
	private SessieDaoJpa mockSessieDao;
	@Mock
	private GebruikerDaoJpa mockGebruikersDao;
	@Mock
	private LokaalDaoJpa mockLokaalDao;
	private ITLAB itlab;

	@BeforeEach
	public void before() {

		this.itlab = new ITLAB(mockSessieDao, mockGebruikersDao, mockLokaalDao);

		this.testGebruiker = new ITlabGebruiker("123456tg",
												GebruikersType.Gebruiker,
												"afbeelding",
												Long.parseLong("9876543219876"),
												"testgebruiker@hogent.be",
												"testgebruiker");
	}

	//
	//
	//
	//
	//
	//
	//
	// gebruiker kunnen verwijderen
	@Test
	public void verwijderGebruiker_verwijderdGebruiker() {
		try {
			this.itlab.maakGebruikerAan(new GebruikerDTO(testGebruiker));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Assertions.assertEquals(0,
			this.itlab	.geefGebruikers()
						.indexOf(testGebruiker));

		this.itlab.verwijderGebruiker(testGebruiker);

		Assertions.assertEquals(-1,
			this.itlab	.geefGebruikers()
						.indexOf(testGebruiker));
	}

	//
	//
	//
	//
	//
	//
	//
	// gebruiker aanmaken

	// sessie aanmaken
	@Test
	public void maakNieuweGebruikerAan_juisteInfo() {
		try {
			this.itlab.maakGebruikerAan(new GebruikerDTO(testGebruiker));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Assertions.assertEquals(0,
			this.itlab	.geefGebruikers()
						.indexOf(testGebruiker));
	}

	@ParameterizedTest
	@MethodSource("foutieveGebruikers")
	public void maakNieweGebruikerAan_fouteInfo(GebruikerDTO dto) {
		assertThrows(IllegalArgumentException.class, () -> this.itlab.maakGebruikerAan(dto));
	}

	// gebuiker wijzigen

	////////////////////////////////////////////////////////////////////
	private static Stream<GebruikerDTO> foutieveGebruikers() {
		return Stream.of(new GebruikerDTO(	null, // foute gebruikersnaam
											GebruikersType.Gebruiker,
											"afbeelding",
											Long.parseLong("3216549873214"),
											"email@google.com",
											"valseNaam",
											null),
			new GebruikerDTO(	"  ", // foute gebruikersnaam
								GebruikersType.Gebruiker,
								"afbeelding",
								Long.parseLong("3216549873214"),
								"email@google.com",
								"valseNaam",
								null),
			new GebruikerDTO(	"", // foute gebruikersnaam
								GebruikersType.Gebruiker,
								"afbeelding",
								Long.parseLong("3216549873214"),
								"email@google.com",
								"valseNaam",
								null),
			new GebruikerDTO(	"321654sd",
								GebruikersType.Gebruiker,
								"afbeelding",
								Long.parseLong("3216549873214"),
								"email@google.com",
								"  ", // foute naam
								null),
			new GebruikerDTO(	"321654sd",
								GebruikersType.Gebruiker,
								"afbeelding",
								Long.parseLong("3216549873214"),
								"email@google.com",
								"", // foute naam
								null),
			new GebruikerDTO(	"321654sd",
								GebruikersType.Gebruiker,
								"afbeelding",
								Long.parseLong("3216549873214"),
								"email@google.com",
								null, // foute naam
								null),
			new GebruikerDTO(	"321654sd",
								GebruikersType.Gebruiker,
								"afbeelding",
								Long.parseLong("32165498"), // te korte barcode
								"email@google.com",
								"testnaam",
								null),
			new GebruikerDTO(	"321654sd",
								GebruikersType.Gebruiker,
								"afbeelding",
								Long.parseLong("3216549873214987987"), // te lange barcode
								"email@google.com",
								"testnaam",
								null),
			new GebruikerDTO(	"321654sd",
								GebruikersType.Gebruiker,
								"afbeelding",
								Long.parseLong("3216549873214"),
								"emailgoogle.com", // foute email
								"testnaam",
								null),
			new GebruikerDTO(	"321654sd",
								null, // geen type
								"afbeelding",
								Long.parseLong("3216549873214"),
								"email@google.com",
								"testnaam",
								null));

	}
}

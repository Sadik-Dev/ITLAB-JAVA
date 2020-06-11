package domein;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FeedbackEntry")
public class FeedbackEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", nullable = false)
	private int id;

	@ManyToOne
	@JoinColumn(name = "AuteurGebruikersnaam", referencedColumnName = "Gebruikersnaam", nullable = true)
	private ITlabGebruiker auteur;

	@Column(name = "Score", nullable = false)
	private int score;

	@Column(name = "Datum", nullable = false)
	private LocalDateTime datum;

	@Column(name = "Inhoud", nullable = false)
	private String inhoud;

	public int getId() {
		return this.id;
	}

	public LocalDateTime getDatum() {
		return this.datum;
	}

	public void setDatum(LocalDateTime value) {
		this.datum = value;
	}

	public String getInhoud() {
		return this.inhoud;
	}

	public void setInhoud(String value) {
		this.inhoud = value;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int value) {
		this.score = value;
	}

	protected FeedbackEntry() {
		super();
		// VOOR JPA
	}

}

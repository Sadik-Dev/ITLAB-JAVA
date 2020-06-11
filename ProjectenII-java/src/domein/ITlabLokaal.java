package domein;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "Lokalen")
@NamedQueries({ @NamedQuery(
		name = "Lokaal.getByLokaalcode",
		query = "SELECT g FROM ITlabLokaal g WHERE g.lokaalcode = :lokaalcode") })
public class ITlabLokaal implements Serializable, Lokaal {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", nullable = false)
	private int id;

	@Column(name = "Lokaalcode", nullable = false)
	private String lokaalcode;

	@Column(name = "AantalPlaatsen", nullable = false)
	private int aantalPlaatsen;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "lokaalType", nullable = false)
	private LokaalType lokaalType;

	// constructor (gegenereerd om testen te kunnen schrijven
	public ITlabLokaal(LokaalType lokaalType,
					   int aantalPlaatsen,
					   String lokaalcode) {
		super();
		this.lokaalType = lokaalType;
		this.aantalPlaatsen = aantalPlaatsen;
		this.lokaalcode = lokaalcode;
	}

	// getters en setters
	public int getAantalPlaatsen() {
		return this.aantalPlaatsen;
	}

	public void setAantalPlaatsen(int value) {
		this.aantalPlaatsen = value;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int value) {
		this.id = value;
	}

	public String getLokaalcode() {
		return this.lokaalcode;
	}

	public void setLokaalcode(String value) {
		this.lokaalcode = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lokaalcode == null) ? 0 : lokaalcode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		ITlabLokaal other = (ITlabLokaal) obj;
		if (lokaalcode == null) {
			if (other.lokaalcode != null)
				return false;
		} else if (!lokaalcode.equals(other.lokaalcode))
			return false;
		return true;
	}

	protected ITlabLokaal() {
		super();
		// VOOR JPA
	}

	@Override
	public String toString() {
		return this.lokaalcode;
	}

}

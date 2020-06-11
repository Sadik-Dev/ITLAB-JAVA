package domein;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GebruikerState")
public class GebruikerState {

	@Id
	@Column(name = "Id", nullable = false)
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(name = "Discriminator", nullable = false)
	private GebruikerStateEnum discriminator;

	@OneToOne
	@JoinColumn(name = "Id")
	@MapsId
	private ITlabGebruiker context;

	public GebruikerState(	ITlabGebruiker context,
							GebruikerStateEnum disc) {
		super();
		this.context = context;
		this.discriminator = disc;
	}

	protected GebruikerState() {
		super();
		// VOOR JPA
	}

	public GebruikerStateEnum getDiscriminator() {
		return discriminator;
	}

	public ITlabGebruiker getContext() {
		return context;
	}

	@Override
	public String toString() {
		return this.discriminator.name();
	}

}
package domein;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SessieState")
public class SessieState implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", nullable = false)
	private int id;

	@Enumerated(EnumType.STRING)
	@Column(name = "Discriminator", nullable = false)
	private SessieStateEnum discriminator;

	@OneToOne
	@JoinColumn(name = "Id")
	@MapsId
	private ITlabSessie context;

	public SessieState(	ITlabSessie context,
						SessieStateEnum disc) {
		this.context = context;
		this.discriminator = disc;
	}

	protected SessieState() {
		super();
		// VOOR JPA
	}

	public SessieStateEnum getDiscriminator() {
		return this.discriminator;
	}

	public ITlabSessie getContext() {
		return context;
	}

	@Override
	public String toString() {
		return this.discriminator.name();
	}

}

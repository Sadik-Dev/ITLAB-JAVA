package domein;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Media")
public class Media implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MediaID", nullable = false)
	private int mediaId;

	@Column(name = "Path", nullable = true)
	private String path;

	@Column(name = "Description", nullable = true)
	private String description;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "Type", nullable = false)
	private MediaType mediaType;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String value) {
		this.description = value;
	}

	public int getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(int value) {
		this.mediaId = value;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String value) {
		this.path = value;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	protected Media() {
		super();
		// VOOR JPA
	}

	@Override
	public String toString() {
		return "Media [path=" + path + ", description=" + description + ", mediaType=" + mediaType + "]";
	}

}

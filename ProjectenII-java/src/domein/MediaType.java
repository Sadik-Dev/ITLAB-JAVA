package domein;

public enum MediaType {
	MediaImage(0), MediaFile(1), MediaUrl(2);

	private int value;

	private MediaType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}

package domein;

public enum LokaalType {
	Vergaderzaal(0), Leslokaal(1), PCklas(2), Auditorium(3);

	private int value;

	private LokaalType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}

package domein;

import javafx.beans.property.SimpleStringProperty;

public interface Gebruiker {

	public SimpleStringProperty getAfbeeldingProperty();

	public SimpleStringProperty getNaamSimpleStringProperty();

	public SimpleStringProperty getGebruikersNaamProperty();

	public SimpleStringProperty getTypeSimpleStringProperty();

	public SimpleStringProperty getStatusSimpleStringProperty();

	public SimpleStringProperty getEmailSimpleStringProperty();

	public SimpleStringProperty getBarcodeSimpleStringProperty();

	public int getAantalAfwezigheden();

	public String getAfbeelding();

	public long getBarcode();

	public String getEmail();

	public String getGebruikersnaam();

	public String getNaam();

	public GebruikerState getCurrentState();

	public GebruikersType getGebruikersType();

	public int getAantalInschrijvingen();

}

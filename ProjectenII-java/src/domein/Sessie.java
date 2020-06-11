package domein;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public interface Sessie {

	public SimpleStringProperty getAantalAanwezigenProperty();

	public SimpleStringProperty getVerantwoordelijkeProperty();

	public SimpleStringProperty getStartStringProperty();

	public SimpleStringProperty getEindeStringProperty();

	public SimpleStringProperty getLokaalCodeProperty();

	public SimpleStringProperty getAantalPlaatsenProperty();

	public SimpleStringProperty getTitelProperty();

	public SimpleStringProperty getDescriptionProperty();

	public SimpleStringProperty getGastSprekerProperty();

	public LocalDateTime getStart();

	public LocalDateTime getEinde();

	public String getTitel();

	public String getDescription();

	public int getAantalPlaatsen();

	public String getGastspreker();

	public boolean isGeopendGeweest();


	public List<Media> getMedia();


}

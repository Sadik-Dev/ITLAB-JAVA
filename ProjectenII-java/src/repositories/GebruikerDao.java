package repositories;

import javax.persistence.EntityNotFoundException;

import domein.ITlabGebruiker;

public interface GebruikerDao extends GenericDao<ITlabGebruiker> {
	public ITlabGebruiker getGebruikerByGebruikersnaam(String gebruikersnaam) throws EntityNotFoundException;
}

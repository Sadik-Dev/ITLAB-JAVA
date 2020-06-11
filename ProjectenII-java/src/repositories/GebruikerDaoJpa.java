package repositories;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domein.ITlabGebruiker;

public class GebruikerDaoJpa extends GenericDaoJpa<ITlabGebruiker> implements GebruikerDao {

	public GebruikerDaoJpa() {
		super(ITlabGebruiker.class);
	}

	@Override
	public ITlabGebruiker getGebruikerByGebruikersnaam(String gebruikersnaam) throws EntityNotFoundException {
		try {
			return em	.createNamedQuery("ITlabGebruiker.getByGebruikersnaam", ITlabGebruiker.class)
						.setParameter("gebruikersnaam", gebruikersnaam)
						.getSingleResult();
		} catch (NoResultException e) {
			throw new EntityNotFoundException();
		}
	}

}

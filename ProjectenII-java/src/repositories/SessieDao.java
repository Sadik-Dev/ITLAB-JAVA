package repositories;

import javax.persistence.EntityNotFoundException;

import domein.ITlabSessie;

public interface SessieDao extends GenericDao<ITlabSessie> {
	public ITlabSessie getSessieByTitel(String titel) throws EntityNotFoundException;
}

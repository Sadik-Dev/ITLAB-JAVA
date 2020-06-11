package repositories;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import domein.ITlabSessie;

public class SessieDaoJpa extends GenericDaoJpa<ITlabSessie> implements SessieDao {

	public SessieDaoJpa() {
		super(ITlabSessie.class);
	}

	@Override
	public ITlabSessie getSessieByTitel(String titel) throws EntityNotFoundException {
		try {
			return em	.createNamedQuery("Sessie.findByTitel", ITlabSessie.class)
						.setParameter("sessieTitel", titel)
						.getSingleResult();
		} catch (NoResultException e) {
			throw new EntityNotFoundException();
		}
	}

}

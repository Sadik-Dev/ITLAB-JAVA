package repositories;

import domein.ITlabLokaal;
import domein.Lokaal;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

public class LokaalDaoJpa extends GenericDaoJpa<ITlabLokaal> implements LokaalDao {
    public LokaalDaoJpa() {
        super(ITlabLokaal.class);
    }

    @Override
    public ITlabLokaal getLokaalByLokaalcode(String lokaalcode) throws EntityNotFoundException {
        try{
        return em	.createNamedQuery("Lokaal.getByLokaalcode", ITlabLokaal.class)
                .setParameter("lokaalcode", lokaalcode)
                .getSingleResult();

    } catch (
    NoResultException e) {
        throw new EntityNotFoundException();
    }
    }
}

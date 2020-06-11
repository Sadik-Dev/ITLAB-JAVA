package repositories;

import domein.ITlabGebruiker;
import domein.ITlabLokaal;

import javax.persistence.EntityNotFoundException;

public interface LokaalDao  extends GenericDao<ITlabLokaal>{
    public ITlabLokaal getLokaalByLokaalcode(String lokaalcode) throws EntityNotFoundException;

}

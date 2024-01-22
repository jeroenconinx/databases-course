package be.kuleuven.dbproject.repository;

import be.kuleuven.dbproject.model.Bezoek;
import be.kuleuven.dbproject.model.Bezoeker;
import be.kuleuven.dbproject.model.Medewerker;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class BezoekRepository extends EntityRepository<Bezoek> {
    public BezoekRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Bezoek> getEntityClass() {
        return Bezoek.class;
    }


}

package be.kuleuven.dbproject.repository;

import be.kuleuven.dbproject.model.Bijdrage;

import javax.persistence.EntityManager;

public class BijdrageRepository extends EntityRepository<Bijdrage>{

    public BijdrageRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Bijdrage> getEntityClass() {
        return Bijdrage.class;
    }
}

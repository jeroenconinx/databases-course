package be.kuleuven.dbproject.repository;

import be.kuleuven.dbproject.model.Medewerker;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class MedewerkerRepository extends EntityRepository<Medewerker>{

    public MedewerkerRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Medewerker findByEmailAdres(String emailAdres){
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Medewerker> query = criteriaBuilder.createQuery(getEntityClass());
            Root<Medewerker> root = query.from(getEntityClass());
            query.select(root).where(criteriaBuilder.equal(root.get("emailAdres"), emailAdres));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    protected Class<Medewerker> getEntityClass() {
        return Medewerker.class;
    }
}

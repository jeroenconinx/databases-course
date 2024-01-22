package be.kuleuven.dbproject.repository;

import be.kuleuven.dbproject.model.Bezoeker;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class BezoekerRepository extends EntityRepository<Bezoeker>{

    public BezoekerRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Bezoeker> getEntityClass() {
        return Bezoeker.class;
    }

    public Bezoeker findByEmailAdres(String emailAdres){
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Bezoeker> query = criteriaBuilder.createQuery(getEntityClass());
            Root<Bezoeker> root = query.from(getEntityClass());
            query.select(root).where(criteriaBuilder.equal(root.get("emailAdres"), emailAdres));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

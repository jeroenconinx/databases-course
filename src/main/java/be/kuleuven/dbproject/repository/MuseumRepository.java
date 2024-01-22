package be.kuleuven.dbproject.repository;

import be.kuleuven.dbproject.model.Medewerker;
import be.kuleuven.dbproject.model.Museum;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class MuseumRepository extends EntityRepository<Museum> {

    public MuseumRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Museum> getEntityClass() {
        return Museum.class;
    }

    public List<Museum> findByMedewerker(Medewerker medewerker){
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Museum> query = criteriaBuilder.createQuery(getEntityClass());
            Root<Museum> root = query.from(getEntityClass());
            Join<Museum, Medewerker> medewerkersJoin = root.join("medewerkers");
            query.select(root).where(criteriaBuilder.equal(medewerkersJoin, medewerker));
            return entityManager.createQuery(query).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}

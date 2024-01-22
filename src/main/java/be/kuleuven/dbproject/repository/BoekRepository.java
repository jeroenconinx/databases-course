package be.kuleuven.dbproject.repository;

import be.kuleuven.dbproject.model.Boek;
import be.kuleuven.dbproject.model.Museum;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class BoekRepository extends EntityRepository<Boek>{
    public BoekRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Boek> getEntityClass() {
        return Boek.class;
    }

    public List<Boek> findByPartialName(String partialName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Boek> query = criteriaBuilder.createQuery(Boek.class);
        Root<Boek> root = query.from(Boek.class);

        // Gebruik de like-expressie om te zoeken op een deel van de naam
        query.select(root).where(criteriaBuilder.like(root.get("naam"), "%" + partialName + "%"));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Boek> findByPartialNameInMuseum(String partialName, Museum museum) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Boek> query = criteriaBuilder.createQuery(Boek.class);
        Root<Boek> root = query.from(Boek.class);

        // Voer een INNER JOIN uit met de relatie tussen Museum en Boek
        Join<Boek, Museum> joinMuseum = root.join("museum");

        // Voeg voorwaarden toe aan de query: deel van de naam en specifiek museum
        query.select(root).where(
                criteriaBuilder.like(root.get("naam"), "%" + partialName + "%"),
                criteriaBuilder.equal(joinMuseum.get("museumID"), museum.getMuseumID())
        );

        return entityManager.createQuery(query).getResultList();
    }

}

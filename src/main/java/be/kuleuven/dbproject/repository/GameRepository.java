package be.kuleuven.dbproject.repository;

import be.kuleuven.dbproject.model.Game;
import be.kuleuven.dbproject.model.Museum;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class GameRepository extends EntityRepository<Game> {
    public GameRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Game> getEntityClass() {
        return Game.class;
    }

    public List<Game> findByPartialName(String partialName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Game> query = criteriaBuilder.createQuery(Game.class);
        Root<Game> root = query.from(Game.class);

        // Gebruik de like-expressie om te zoeken op een deel van de naam
        query.select(root).where(criteriaBuilder.like(root.get("naam"), "%" + partialName + "%"));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Game> findByPartialNameInMuseum(String partialName, Museum museum) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Game> query = criteriaBuilder.createQuery(Game.class);
        Root<Game> root = query.from(Game.class);

        // Voer een INNER JOIN uit met de relatie tussen Museum en Game
        Join<Game, Museum> joinMuseum = root.join("museum");

        // Voeg voorwaarden toe aan de query: deel van de naam en specifiek museum
        query.select(root).where(
                criteriaBuilder.like(root.get("naam"), "%" + partialName + "%"),
                criteriaBuilder.equal(joinMuseum.get("museumID"), museum.getMuseumID())
        );

        return entityManager.createQuery(query).getResultList();
    }
}

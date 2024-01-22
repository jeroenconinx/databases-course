package be.kuleuven.dbproject.repository;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

public abstract class EntityRepository<T> {

    protected final EntityManager entityManager;

    public EntityRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<T> findAll(){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(getEntityClass());
        Root<T> root = query.from(getEntityClass());

        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    public T findById(Integer id){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(getEntityClass());
        Root<T> root = query.from(getEntityClass());
        query.select(root).where(criteriaBuilder.equal(root.get(getIdFieldName(getEntityClass())), id));
        return entityManager.createQuery(query).getSingleResult();
    }


    public void save(T entity){
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }
    public void update(T entity){
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    public void delete(T entity){
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }

    public void deleteByID(Integer id){
        T entity = findById(id);
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }

    private String getIdFieldName(Class<T> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        throw new IllegalArgumentException("No field annotated with @Id found in the entity class.");
    }
    protected abstract Class<T> getEntityClass();

}

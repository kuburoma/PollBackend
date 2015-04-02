package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.exception.DaoException;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;


/**
 * Created by Nell on 25.2.2015.
 */
public class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

    public GenericDaoImpl(Class entityClass) {
        this.entityClass = entityClass;
        emf = Persistence.createEntityManagerFactory("persist-unit");
    }

    protected Class entityClass;
    protected EntityManagerFactory emf;
    protected EntityManager em;
    protected EntityTransaction tx;

    @Override
    public T find(PK id)  throws DaoException {
        em = emf.createEntityManager();
        try {
            T object = (T) em.find(entityClass, id);
            return object;
        } catch (Exception e) {
            throw new DaoException("Chyba při hledání entity", e);
        }finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll()  throws DaoException{
        try{
        em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(entityClass);
        Root<T> c = q.from(entityClass);
        q.select(c);
        return em.createQuery(q).getResultList();
    } catch (Exception e) {
        throw new DaoException("Chyba při hledání entit", e);
    }finally {
        em.close();
    }
    }


    public List<T> findBy(String columnName, String name)  throws DaoException {
        try{
        em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(entityClass);
        Root<T> c = q.from(entityClass);
        q.select(c);
        q.where(cb.equal(c.get(columnName),name));
        return em.createQuery(q).getResultList();
    } catch (Exception e) {
        throw new DaoException("Chyba při hledání entit", e);
    }finally {
        em.close();
    }
    }

    @Override
    public T create(T object)  throws DaoException {
        em = emf.createEntityManager();
        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(object);
            tx.commit();
            return object;
        } catch (PersistenceException e) {
            tx.rollback();
            throw new DaoException("Chyba při vytvoření entity", e);
        }finally {
            em.close();
        }
    }

    @Override
    public void update(T object) throws DaoException{
        em = emf.createEntityManager();
        try {
            tx = em.getTransaction();
            tx.begin();
            em.merge(object);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new DaoException("Chyba při ukládání entity", e);
        }finally {
            em.close();
        }
    }

    @Override
    public void delete(PK id)  throws DaoException {
        em = emf.createEntityManager();
        try {
            T object = (T) em.find(entityClass,id);
            tx = em.getTransaction();
            tx.begin();
            em.remove(object);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw new DaoException("Chyba při mazaní entit", e);
        }finally {
            em.close();
        }
    }

}


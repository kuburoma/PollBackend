package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.EntitiesList;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;

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
        //emf = Persistence.createEntityManagerFactory("persist-unit");
        emf = EntityManagerFactoryDao.getEntityManagerFactory();
    }

    protected Class entityClass;
    protected EntityManagerFactory emf;
    protected EntityManager em;
    protected EntityTransaction tx;

    protected CriteriaBuilder cb;
    protected CriteriaQuery<T> cq;
    protected Root<T> root;
    protected Query query;
    /**
     * Nalezne objekty dle zadaného id
     *
     * @param id použité pro vyhledávání entity
     * @return Vyhledanou entitu
     * @throws DaoException
     */
    @Override
    public T find(PK id) throws DaoException {
        em = emf.createEntityManager();
        try {
            T object = (T) em.find(entityClass, id);
            return object;
        } catch (PersistenceException e) {
            throw new DaoException("Chyba při hledání entity", e);
        } finally {
            em.close();
        }
    }

    @Override
    public EntitiesList<T> findAll(Integer offset, Integer base, String order) throws DaoException, InputException {
        try {
            initializationSearch();

            return makeEntitiesList(offset, base, order);
        } catch (PersistenceException e) {
            throw new DaoException("Chyba při hledání entit", e);
        } finally {
            em.close();
        }
    }

    /**
     * Vytvoření entity
     *
     * @param object
     * @return Uloženou entitu
     * @throws DaoException
     */
    @Override
    public T create(T object) throws DaoException {
        em = emf.createEntityManager();
        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(object);
            tx.commit();
            return object;
        } catch (PersistenceException e) {
            e.printStackTrace();
            tx.rollback();
            throw new DaoException("Error when saving entity", e);
        } finally {
            em.close();
        }
    }

    /**
     * Upravení existující entity
     *
     * @param object
     * @throws DaoException
     */
    @Override
    public void update(T object) throws DaoException {
        em = emf.createEntityManager();
        try {
            tx = em.getTransaction();
            tx.begin();
            em.merge(object);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw new DaoException("Error when updating entity", e);
        } finally {
            em.close();
        }
    }

    /**
     * Smazání existující entity
     * @param id
     * @throws DaoException
     */
    @Override
    public void delete(PK id) throws DaoException {
        em = emf.createEntityManager();
        try {
            T object = (T) em.find(entityClass, id);
            tx = em.getTransaction();
            tx.begin();
            em.remove(object);
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            tx.rollback();
            throw new DaoException("Error when deleting entity", e);
        } finally {
            em.close();
        }
    }

    /**
     * Do CriteriaQuery přidá podmínku pro řazení entit
     *
     * @param order
     * @throws InputException Pokud order neobsahuje na prvním míste +- nebo neexistuje daný sloupeček vrátí chybu s popisem
     */
    private void orderBy(String order) throws InputException {
        if (order == null || order.length() < 2) {
            return;
        }

        char orderHow = order.charAt(0);
        String orderBy = order.substring(1);

        if(orderHow != '+' && orderHow != '-'){
            throw new InputException("Unknown order symbol ["+orderHow+"] please use + or -");
        }
        try {
            if (orderHow == '+') {
                cq.orderBy(cb.asc(root.get(orderBy)));
            }
            if (orderHow == '-') {
                cq.orderBy(cb.desc(root.get(orderBy)));
            }
        }catch(IllegalArgumentException e){
            throw new InputException("Cannot order by column name: "+orderBy);
        }
    }

    /**
     *  Přidá query base a offset pro stránkování.
     *
     * @param base maximální počet vrácených entit
     * @param offset Od které entity mám seznam vracet
     */
    private void addBaseAndOffset(Integer base, Integer offset){
        if (offset != null) {
            query.setFirstResult(offset);
        }
        if (base != null) {
            query.setMaxResults(base);
        }
    }

    /**
     * Přidá řezní entit, nastavení base a offset a vytvoří nový seznam.
     *
     * @return výsledný enttities list
     */
    protected EntitiesList<T> makeEntitiesList(Integer offset, Integer base, String order) throws InputException {
        orderBy(order);

        query = em.createQuery(cq);
        EntitiesList<T> entitiesList = new EntitiesList<T>();
        entitiesList.setTotalSize(query.getResultList().size());
        addBaseAndOffset(base,offset);
        entitiesList.setEntities(query.getResultList());

        return entitiesList;
    }

    protected void initializationSearch(){
        em = emf.createEntityManager();
        cb = em.getCriteriaBuilder();
        cq = cb.createQuery(entityClass);
        root = cq.from(entityClass);
        cq.select(root);
    }

}


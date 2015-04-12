package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.entities.EntitiesList;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;
import org.hibernate.Hibernate;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.List;

public class VoterDao extends GenericDaoImpl<Voter, Long> {

    public VoterDao() {
        super(Voter.class);
    }

    /**
     * Vrací uživatele se schodným emailem
     *
     * @param email
     * @return
     * @throws DaoException
     */
    public Voter findVoterByEmail(String email) throws DaoException {
        try {
            initializationSearch();

            cq.where(cb.equal(root.get("email"), email));


            try {
                return em.createQuery(cq).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }


        } catch (PersistenceException e) {
            throw new DaoException("Error findVoterByEmail", e);
        } finally {
            em.close();
        }
    }

    /**
     * Upravý jmeno, email a heslo osoby.
     *
     * @param object Voter
     * @throws DaoException
     */
    @Override
    public void update(Voter object) throws DaoException {
        em = emf.createEntityManager();
        try {
            tx = em.getTransaction();
            tx.begin();
            Voter entity = em.find(Voter.class, object.getId());
            entity.setFirstName(object.getFirstName());
            entity.setLastName(object.getLastName());
            entity.setEmail(object.getEmail());
            entity.setPassword(object.getPassword());
            em.merge(entity);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw new DaoException("Error update", e);
        } finally {
            em.close();
        }
    }

    /**
     * Vr
     *
     * @param id
     * @param offset
     * @param base
     * @param order
     * @return
     * @throws DaoException
     * @throws InputException
     */
    public EntitiesList<Voter> findVotersOfVoterGroup(Long id, Integer offset, Integer base, String order) throws DaoException, InputException {
        try {
            initializationSearch();

            Expression<Long> voter = root.join("voterGroups").get("id");

            cq.where(cb.equal(voter, id));

            return makeEntitiesList(offset, base, order);

        } catch (PersistenceException e) {
            throw new DaoException("Error findUserPolls", e);
        } finally {
            em.close();
        }
    }

}

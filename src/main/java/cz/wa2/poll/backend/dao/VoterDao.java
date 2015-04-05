package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.dto.VoterFullDTO;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;
import cz.wa2.poll.backend.exception.DaoException;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class VoterDao extends GenericDaoImpl<Voter, Long> {

    public VoterDao() {
        super(Voter.class);
    }

    public Voter getVoterByEmail(String email) throws DaoException {
        try {
            em = emf.createEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Voter> q = cb.createQuery(entityClass);
            Root<Voter> c = q.from(entityClass);
            q.select(c);
            q.where(cb.equal(c.get("email"), email));
            List<Voter> voters = em.createQuery(q).getResultList();
            if (voters.size() == 1) {
                return voters.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            tx.rollback();
            throw new DaoException("Chyba při ukládání entity", e);
        } finally {
            em.close();
        }
    }

    public Voter findWithVoterGroups(Long id) throws DaoException {
        em = emf.createEntityManager();
        try {
            Voter object = (Voter) em.find(entityClass, id);
            Hibernate.initialize(object.getVoterGroups());
            return object;
        } catch (Exception e) {
            throw new DaoException("Chyba při VoterDao.findWithVoterGroup("+id+")", e);
        } finally {
            em.close();
        }
    }

    public void update(VoterDTO object) {
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
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Voter> ahoj() {
        em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Voter> q = cb.createQuery(Voter.class);
        Root<Voter> schoolRoot = q.from(Voter.class);
        Join<Voter, VoterGroup> join = schoolRoot.join("voterGroups");
        q.where(cb.notEqual(join.get("id"), 2));
        List<Voter> voters = em.createQuery(q).getResultList();
        return voters;
    }
}

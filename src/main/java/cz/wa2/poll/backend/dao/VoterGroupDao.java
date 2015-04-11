package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.*;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;
import org.hibernate.Hibernate;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Iterator;
import java.util.List;

public class VoterGroupDao extends GenericDaoImpl<VoterGroup, Long> {

    public VoterGroupDao() {
        super(VoterGroup.class);
    }

    /**
     * Pro zadaneho uzivatele vytvori novou skupinu a auzivatele nastavi jako supervisor.
     *
     * @param supervisorId
     * @param voterGroup
     * @return
     * @throws DaoException
     */
    public VoterGroup create(Long supervisorId, VoterGroup voterGroup) throws DaoException {
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            Voter supervisor = em.find(Voter.class, supervisorId);
            VoterGroup voterGroupSave = new VoterGroup();
            voterGroupSave.setSupervisor(supervisor);
            voterGroupSave.setName(voterGroup.getName());
            voterGroupSave.setDescription(voterGroup.getDescription());
            em.persist(voterGroupSave);
            tx.commit();
            return voterGroupSave;
        } catch (PersistenceException e) {
            tx.rollback();
            throw new DaoException("Chyba při ukládání entity", e);
        } finally {
            em.close();
        }
    }

    /**
     * Odstraní z dané skupiny votera.
     *
     * @param votergroupId
     * @param voterId
     * @throws DaoException
     */
    public void removeVoter(Long votergroupId, Long voterId) throws DaoException {
        em = emf.createEntityManager();
        try {
            tx = em.getTransaction();
            tx.begin();
            VoterGroup voterGroup = (VoterGroup) em.find(entityClass, votergroupId);
            Iterator<Voter> it = voterGroup.getVoters().iterator();
            while (it.hasNext()) {
                Voter v = it.next();
                if (v.getId() == voterId) {
                    it.remove();
                    break;
                }
            }
            em.merge(voterGroup);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw new DaoException("Error removeVoter", e);
        } finally {
            em.close();
        }
    }

    /**
     * Ke skupine prida noveho votera.
     *
     * @param votergroupId
     * @param voterId
     * @throws DaoException
     */
    public void putVoter(Long votergroupId, Long voterId) throws DaoException {
        em = emf.createEntityManager();
        try {
            VoterGroup voterGroup = (VoterGroup) em.find(entityClass, votergroupId);
            Iterator<Voter> it = voterGroup.getVoters().iterator();
            while (it.hasNext()) {
                if (it.next().getId() == voterId) {
                    throw new DaoException("Uživatel již ve skupině je");
                }
            }
            Voter voter = em.find(Voter.class, voterId);
            voterGroup.addVoter(voter);
            tx = em.getTransaction();
            tx.begin();
            em.merge(voterGroup);
            tx.commit();
        } catch (PersistenceException e) {
            throw new DaoException("Error putVoter", e);
        } finally {
            em.close();
        }
    }

    /**
     * @param voterId
     * @param findWho
     * @param offset
     * @param base
     * @param order
     * @return
     * @throws DaoException
     * @throws InputException
     */
    public EntitiesList<VoterGroup> findVoterGroups(Long voterId, Integer findWho, Integer offset, Integer base, String order) throws DaoException, InputException {
        try {
            initializationSearch();
            if (!(0 <= findWho && findWho <= 2)) {
                throw new InputException("Neplatná hodnota pro findWho param. \n0 - není členem ani supervisorem skupiny\n1 - je členem skupiny\n2 - je supervisorem skupiny");
            }
            if (findWho == 0) {
                Expression<List<Voter>> voters = root.get("voters");
                Expression<Voter> supervisor = root.get("supervisor");
                Voter voter = em.find(Voter.class, voterId);
                cq.where(cb.and(cb.not(cb.isMember(voter, voters)), cb.not(cb.equal(supervisor, voter))));
            }
            if (findWho == 1) {
                Expression<Long> voter = root.join("voters").get("id");
                cq.where(cb.equal(voter, voterId));
            }
            if (findWho == 2) {
                Expression<Long> supervisor = root.get("supervisor").get("id");
                cq.where(cb.equal(supervisor, voterId));
            }

            return makeEntitiesList(offset, base, order);

        } catch (PersistenceException e) {
            throw new DaoException("Error findUserPolls", e);
        } finally {
            em.close();
        }
    }

    /**
     * vytvoří nové hlasování pro zvolenou skupinu a pro každého uživatele skupiny vytvoří nový hlasovací lístek.
     *
     * @param poll
     * @param groupId
     * @return
     * @throws DaoException
     */
    public Poll createPoll(Poll poll, Long groupId) throws DaoException {
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            VoterGroup voterGroup = em.find(VoterGroup.class, groupId);
            poll.setVoterGroup(voterGroup);
            em.persist(poll);
            Iterator<Voter> it = voterGroup.getVoters().iterator();
            while (it.hasNext()) {
                Voter voter = it.next();
                Ballot ballot = new Ballot();
                ballot.setAnswer(null);
                ballot.setPoll(poll);
                ballot.setVoter(voter);
                em.persist(ballot);
            }
            tx.commit();
            return poll;
        } catch (PersistenceException e) {
            tx.rollback();
            throw new DaoException("Error createPoll", e);
        } finally {
            em.close();
        }
    }

}

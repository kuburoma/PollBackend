package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.dto.VoterGroupDTO;
import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;
import cz.wa2.poll.backend.exception.DaoException;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VoterGroupDao extends GenericDaoImpl<VoterGroup, Long> {

    public VoterGroupDao() {
        super(VoterGroup.class);
    }

    public VoterGroup create(VoterGroup voterGroup) throws DaoException {
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            Voter supervisor = em.find(Voter.class, voterGroup.getSupervisor().getId());
            VoterGroup voterGroupSave = new VoterGroup();
            voterGroupSave.setSupervisor(supervisor);
            voterGroupSave.setName(voterGroup.getName());
            voterGroupSave.setDescription(voterGroup.getDescription());
            em.persist(voterGroupSave);
            tx.commit();
            return voterGroupSave;
        } catch (Exception e) {
            tx.rollback();
            throw new DaoException("Chyba při ukládání entity", e);
        } finally {
            em.close();
        }
    }

    public void removeVoter(Long votergroupId, Long voterId) throws DaoException {
        em = emf.createEntityManager();
        try {
            tx = em.getTransaction();
            tx.begin();
            VoterGroup voterGroup = (VoterGroup) em.find(entityClass, votergroupId);
            Iterator<Voter> it = voterGroup.getVoters().iterator();
            while(it.hasNext()){
                Voter v = it.next();
                if(v.getId() == voterId){
                    it.remove();
                    break;
                }
            }
            em.merge(voterGroup);
            tx.commit();
        } catch (Exception e) {
            throw new DaoException("Chyba při VoterGroupDao.removeVoter("+votergroupId+" ,"+voterId+")", e);
        } finally {
            em.close();
        }
    }

    public void putVoter(Long votergroupId, Long voterId) throws DaoException {
        em = emf.createEntityManager();
        try {
            tx = em.getTransaction();
            tx.begin();
            VoterGroup voterGroup = (VoterGroup) em.find(entityClass, votergroupId);
            Voter voter = em.find(Voter.class, voterId);
            voterGroup.addVoter(voter);
            em.merge(voterGroup);
            tx.commit();
        } catch (Exception e) {
            throw new DaoException("Chyba při VoterGroupDao.putVoter("+votergroupId+" ,"+voterId+")", e);
        } finally {
            em.close();
        }
    }

    public List<Voter> findVoters(Long id) throws DaoException {
        em = emf.createEntityManager();
        try {
            VoterGroup object = (VoterGroup) em.find(entityClass, id);
            Hibernate.initialize(object.getVoters());
            return object.getVoters();
        } catch (Exception e) {
            throw new DaoException("Chyba při VoterDao.findWithVoterGroup("+id+")", e);
        } finally {
            em.close();
        }
    }

    public List<VoterGroup> getVoterGroupsWithVoter(Long id) throws DaoException {
            em = emf.createEntityManager();
            CriteriaBuilder cb=em.getCriteriaBuilder();
            CriteriaQuery<VoterGroup> q = cb.createQuery(VoterGroup.class);
            Root<VoterGroup> b=q.from(VoterGroup.class);
            Expression<List<Voter>> voters=b.get("voters");
            Voter voter = em.find(Voter.class, id);
            q.select(b).where(cb.isMember(voter, voters)).orderBy(cb.desc(b.get("id")));
            List<VoterGroup> voterGroups = em.createQuery(q).getResultList();
            return voterGroups;
    }

    public List<VoterGroup> getVoterGroupsWithoutVoter(Long id) throws DaoException {
        em = emf.createEntityManager();
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<VoterGroup> q = cb.createQuery(VoterGroup.class);
        Root<VoterGroup> b=q.from(VoterGroup.class);
        Expression<List<Voter>> voters=b.get("voters");
        Expression<Voter> supervisor = b.get("supervisor");
        Voter voter = em.find(Voter.class, id);
        q.select(b).where(cb.and(cb.not(cb.isMember(voter, voters)), cb.not(cb.equal(supervisor,voter)))).orderBy(cb.desc(b.get("id")));
        List<VoterGroup> voterGroups = em.createQuery(q).getResultList();
        return voterGroups;
    }

    public List<VoterGroup> getSupervisedGroups(Long id) throws DaoException {
        em = emf.createEntityManager();
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<VoterGroup> q = cb.createQuery(VoterGroup.class);
        Root<VoterGroup> b=q.from(VoterGroup.class);
        Expression<Voter> supervisor = b.get("supervisor");
        Voter voter = em.find(Voter.class, id);
        q.select(b).where(cb.equal(supervisor,voter)).orderBy(cb.desc(b.get("id")));
        List<VoterGroup> voterGroups = em.createQuery(q).getResultList();
        return voterGroups;
    }



    public Poll createPoll(Poll poll, Long groupId) throws DaoException {
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            VoterGroup voterGroup = em.find(VoterGroup.class, groupId);
            poll.setVoterGroup(voterGroup);
            em.persist(poll);
            Iterator<Voter> it = voterGroup.getVoters().iterator();
            while (it.hasNext()){
                Voter voter = it.next();
                Ballot ballot = new Ballot();
                ballot.setAnswer(0L);
                ballot.setPoll(poll);
                ballot.setVoter(voter);
                em.persist(ballot);
            }
            tx.commit();
            return poll;
        } catch (Exception e) {
            tx.rollback();
            throw new DaoException("Chyba při ukládání entity", e);
        } finally {
            em.close();
        }
    }

}

package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;
import cz.wa2.poll.backend.exception.DaoException;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PollDao extends GenericDaoImpl<Poll, Long> {

    public PollDao() {
        super(Poll.class);
    }


    @Override
    public Poll create(Poll object) throws DaoException {
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
        } finally {
            em.close();
        }
    }

    public List<Ballot> getPollBallots(Long id) throws DaoException {
        em = emf.createEntityManager();
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Ballot> q = cb.createQuery(Ballot.class);
        Root<Ballot> b=q.from(Ballot.class);
        Expression<Poll> pollBallot = b.get("poll");

        Poll poll = em.find(Poll.class, id);

        q.select(b).where(cb.equal(pollBallot, poll)).orderBy(cb.desc(b.get("id")));
        return em.createQuery(q).getResultList();
    }


    public List<Poll> getNonvotedPolls(Long id) throws DaoException {
        em = emf.createEntityManager();
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Ballot> q = cb.createQuery(Ballot.class);
        Root<Ballot> b=q.from(Ballot.class);
        Expression<Voter> voterBallot = b.get("voter");
        Expression<Long> answer = b.get("answer");
        Voter voter = em.find(Voter.class, id);
        q.select(b).where(cb.and(cb.equal(voterBallot, voter), cb.equal(answer, 0L))).orderBy(cb.desc(b.get("id")));
        List<Ballot> ballots = em.createQuery(q).getResultList();
        List<Poll> polls = new ArrayList<Poll>();
        Iterator<Ballot> it = ballots.iterator();
        while(it.hasNext()){
            polls.add(it.next().getPoll());
        }
        return polls;
    }

    public List<Poll> getVotedPolls(Long id) throws DaoException {
        em = emf.createEntityManager();
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Ballot> q = cb.createQuery(Ballot.class);
        Root<Ballot> b=q.from(Ballot.class);
        Expression<Voter> voterBallot = b.get("voter");
        Expression<Long> answer = b.get("answer");
        Voter voter = em.find(Voter.class, id);
        q.select(b).where(cb.and(cb.equal(voterBallot, voter), cb.not(cb.equal(answer, 0L)))).orderBy(cb.desc(b.get("id")));
        List<Ballot> ballots = em.createQuery(q).getResultList();
        List<Poll> polls = new ArrayList<Poll>();
        Iterator<Ballot> it = ballots.iterator();
        while(it.hasNext()){
            polls.add(it.next().getPoll());
        }
        return polls;
    }
}

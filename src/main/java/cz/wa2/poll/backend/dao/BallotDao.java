package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.exception.DaoException;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BallotDao extends GenericDaoImpl<Ballot, Long> {

    public BallotDao() {
        super(Ballot.class);
    }

    @Override
    public void update(Ballot ballot) throws DaoException {
        em = emf.createEntityManager();
        try {
            tx = em.getTransaction();
            tx.begin();
            Ballot object = em.find(Ballot.class, ballot.getId());
            object.setAnswer(ballot.getAnswer());
            em.merge(object);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw new DaoException("Chyba při vytvoření entity", e);
        } finally {
            em.close();
        }
    }

    public Ballot getBallot(Long voterId, Long pollId) throws DaoException {
        em = emf.createEntityManager();
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Ballot> q = cb.createQuery(Ballot.class);
        Root<Ballot> b=q.from(Ballot.class);
        Expression<Voter> voterBallot = b.get("voter");
        Expression<Poll> pollBallot = b.get("poll");

        Voter voter = em.find(Voter.class, voterId);
        Poll poll = em.find(Poll.class, pollId);

        q.select(b).where(cb.and(cb.equal(voterBallot, voter), cb.equal(pollBallot, poll))).orderBy(cb.desc(b.get("id")));
        List<Ballot> ballots = em.createQuery(q).getResultList();
        if(ballots.size() == 1){
            return ballots.get(0);
        }else{
            return null;
        }

    }
}

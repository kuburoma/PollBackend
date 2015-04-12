package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.*;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;
import org.hibernate.Criteria;

import javax.management.Query;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PollDao extends GenericDaoImpl<Poll, Long> {

    public PollDao() {
        super(Poll.class);
    }

    public EntitiesList<Poll> findUserPolls(Long id, Integer voted, Integer offset, Integer base, String order) throws DaoException, InputException {
        try {
            initializationSearch();

            Join<Poll, Ballot> join = root.join("ballots");
            Expression<Voter> voterBallot = join.get("voter");
            Expression<Integer> answer = join.get("answer");
            Expression<Voter> supervised = root.get("voterGroup").get("supervisor");

            Voter voter = em.find(Voter.class, id);
            if(voted == 0){
                cq.where(cb.and(cb.equal(voterBallot, voter), cb.isNull(answer)));
            }
            if (voted == 1) {
                cq.where(cb.and(cb.equal(voterBallot, voter), cb.isNotNull(answer)));
            }
            if (voted == 2) {
                cq.distinct(true);
                cq.where(cb.equal(supervised, voter));
            }


            return makeEntitiesList(offset, base, order);

        } catch (PersistenceException e) {
            throw new DaoException("Error findUserPolls", e);
        } finally {
            em.close();
        }
    }

    public Poll findPollByName(String name) throws DaoException {
        try {
            initializationSearch();

            cq.where(cb.equal(root.get("name"), name));

            try {
                return em.createQuery(cq).getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } catch (PersistenceException e) {
            throw new DaoException("Error findPollByName", e);
        } finally {
            em.close();
        }
    }
}

package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.EntitiesList;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;

import javax.management.Query;
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

    public EntitiesList<Poll> findUserPolls(Long id, Boolean voted, Integer offset, Integer base, String order) throws DaoException, InputException {
        try {
            initializationSearch();

            Expression<Voter> voterBallot = root.join("ballots").get("voter");
            Expression<Long> answer = root.join("ballots").get("answer");

            Voter voter = em.find(Voter.class, id);
            if (voted) {
                cq.where(cb.and(cb.equal(voterBallot, voter), cb.equal(answer, 0L)));
            } else {
                cq.where(cb.and(cb.equal(voterBallot, voter), cb.not(cb.equal(answer, 0L))));
            }

            return makeEntitiesList(offset, base, order);

        } catch (PersistenceException e) {
            throw new DaoException("Error findUserPolls", e);
        } finally {
            em.close();
        }
    }
}

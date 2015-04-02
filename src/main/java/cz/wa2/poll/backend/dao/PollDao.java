package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.Poll;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class PollDao extends GenericDaoImpl<Poll, Long> {

    public PollDao() {
        super(Poll.class);
    }

}

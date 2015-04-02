package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.Ballot;

public class BallotDao extends GenericDaoImpl<Ballot, Long> {

    public BallotDao() {
        super(Ballot.class);
    }
}

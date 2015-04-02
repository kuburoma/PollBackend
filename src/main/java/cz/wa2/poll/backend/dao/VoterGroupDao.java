package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.VoterGroup;

public class VoterGroupDao extends GenericDaoImpl<VoterGroup, Long> {

    public VoterGroupDao() {
        super(VoterGroup.class);
    }
}

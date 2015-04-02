package cz.wa2.poll.backend.dao;

import cz.wa2.poll.backend.entities.Answer;
import cz.wa2.poll.backend.entities.Ballot;

public class AnswerDao extends GenericDaoImpl<Answer, Long> {

    public AnswerDao() {
        super(Answer.class);
    }
}

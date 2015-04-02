package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.PollDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.entities.Answer;
import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.exception.DaoException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by Nell on 18.3.2015.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/hello")
public class HelloWorld {
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces("text/plain")
    public String getClichedMessage() {
        try {
            Answer answer1 = new Answer();
            answer1.setAnswer("Ano");
            answer1.setDefaultAnswer(true);
            Answer answer2 = new Answer();
            answer1.setDefaultAnswer(false);
            answer2.setAnswer("Ne");

            Poll poll = new Poll();
            poll.setQuestion("Proč");
            poll.addAnswer(answer1);
            poll.addAnswer(answer2);

            PollDao pollDao = new PollDao();
            poll = pollDao.create(poll);
            Ballot ballot1 = new Ballot();
            ballot1.setAnswer(poll.getAnswers().get(0));
            ballot1.setPoll(poll);
            Ballot ballot2 = new Ballot();
            ballot1.setAnswer(poll.getAnswers().get(0));
            ballot1.setPoll(poll);
            poll.addBallot(ballot1);
            poll.addBallot(ballot2);
            pollDao.update(poll);

            Poll aaa = pollDao.find(poll.getId());
            List<Answer> a = aaa.getAnswers();
            List<Ballot> b = aaa.getBallots();






            return "";
        } catch (DaoException e) {
            e.printStackTrace();
            return "";
        }
    }

}

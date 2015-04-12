package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.BallotDao;
import cz.wa2.poll.backend.dao.PollDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dao.VoterGroupDao;
import cz.wa2.poll.backend.entities.*;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;
import cz.wa2.poll.backend.websocket.Producer;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nell on 18.3.2015.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/hello")
@RequestScoped
public class HelloWorld {
    // The Java method will process HTTP GET requests

    @PersistenceContext(unitName = "persist-unit")
    EntityManager em;

    PollDao pollDao = new PollDao();
    VoterDao voterDao = new VoterDao();
    VoterGroupDao voterGroupDao = new VoterGroupDao();
    BallotDao ballotDao = new BallotDao();


    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces("text/plain")
    public String getClichedMessage() throws IOException, DaoException, InputException {

       // VoterGroupDao dao = new VoterGroupDao();
        //EntitiesList<VoterGroup> list = dao.getVoterGroups(9L,0,null,null,null);

/*
        integerStringMap.put(0, "ahoj");
        integerStringMap.put(1, "lol");
        poll.setAnswers(integerStringMap);

        Poll pol2 = pollDao.create(poll);

        poll = pollDao.find(pol2.getId());

        poll.getAnswers();*/
/*
        VoterGroup voterGroup = new VoterGroup();
        voterGroup.setName("V");
        voterGroup.setDescription("V");

        voterGroupDao.create(5L, voterGroup);*/

        //voterGroupDao.putVoter(6L, 6L);
        //voterGroupDao.putVoter(6L, 7L);
        //voterGroupDao.

//        Poll poll = new Poll();
//        poll.setName("Pokus");
//        poll.setQuestion("Pokus");
//        Map<Integer, String> integerStringMap = new HashMap<Integer, String>();
//        integerStringMap.put(0, "ahoj");
//        integerStringMap.put(1, "lol");
//        poll.setAnswers(integerStringMap);
//
//        voterGroupDao.createPoll(poll,7L);

/*        pollDao.delete(7L);
        pollDao.delete(8L);
        pollDao.delete(9L);
        pollDao.delete(10L);
        pollDao.delete(11L);*/


       // list.getEntities();
      //  voters.size();
        //Producer producer = new Producer("hello");
        //producer.sendMessage("ahoj");

       // voterGroupDao.delete(6L);

        return "";

    }

}

package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.PollDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dao.VoterGroupDao;
import cz.wa2.poll.backend.dto.VoterGroupDTO;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.websocket.Producer;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

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

    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces("text/plain")
    public String getClichedMessage() throws Exception {

        Producer producer = new Producer("hello");
        producer.sendMessage("ahoj");

        return "";

    }

}

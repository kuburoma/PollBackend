package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.BallotDao;
import cz.wa2.poll.backend.dao.PollDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.dto.PollDTO;
import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.EntitiesList;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/poll")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON+ "; charset=UTF-8")
public class PollRest {

    final static Logger logger = Logger.getLogger(PollRest.class);
    ConvertorDTO convertorDTO = new ConvertorDTO();
    PollDao dao = new PollDao();


    @GET
    public Response getPolls(@QueryParam("offset") Integer offset,
                             @QueryParam("base") Integer base,
                             @QueryParam("order") String order) {
        try {
            debugMessage("getPolls: Received");
            EntitiesList<Poll> entitiesList = dao.findAll(offset, base, order);
            debugMessage("getPolls: 200");
            return Response.status(Response.Status.OK).header("Count-records", entitiesList.getTotalSize()).entity(convertorDTO.convertPollToDTO(entitiesList.getEntities())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("getPolls: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            debugMessage("getPolls: 400");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(value = "/{id}")
    public Response getPoll(@PathParam("id") Long id) {
        try {
            debugMessage("getPoll: Received");
            PollDTO object = new PollDTO(dao.find(id));
            debugMessage("getPoll: 200");
            return Response.status(Response.Status.OK).entity(object).build();
        } catch (DaoException e) {
            debugMessage("getPoll: 500");
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{id}/ballot")
    public Response getPollBallots(@PathParam("id") Long id,
                                   @QueryParam("offset") Integer offset,
                                   @QueryParam("base") Integer base,
                                   @QueryParam("order") String order) {
        try {
            debugMessage("getPollBallots: Received");
            BallotDao ballotDao = new BallotDao();
            EntitiesList<Ballot> entitiesList = ballotDao.findPollBallots(id, offset, base, order);
            debugMessage("getPollBallots: 200");
            return Response.status(Response.Status.OK).header("Count-records", entitiesList.getTotalSize()).entity(convertorDTO.convertBallotToDTO(entitiesList.getEntities())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("getPollBallots: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            debugMessage("getPollBallots: 400");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    public Response updatePoll(PollDTO pollDTO) {
        try {
            debugMessage("updatePoll: Received");
            dao.update(pollDTO.toEntity());
            debugMessage("updatePoll: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("updatePoll: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path(value = "/{id}")
    public Response deletePoll(@PathParam("id") Long id) {
        try {
            debugMessage("deletePoll: Received");
            dao.delete(id);
            debugMessage("deletePoll: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("deletePoll: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void debugMessage(String message){
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}

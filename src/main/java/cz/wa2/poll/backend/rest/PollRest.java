package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.PollDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dao.VoterGroupDao;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.dto.PollDTO;
import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.exception.DaoException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/poll")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class PollRest {

    ConvertorDTO convertorDTO = new ConvertorDTO();
    PollDao pollDao = new PollDao();

    @GET
    public Response getPolls() {
        try {
            return Response.status(Response.Status.OK).entity(convertorDTO.convertPollToDTO(pollDao.findAll())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{id}")
    public Response getPoll(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(new PollDTO(pollDao.find(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{id}/ballot")
    public Response getPollBallots(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(convertorDTO.convertBallotToDTO(pollDao.getPollBallots(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    public Response updateVoter(VoterDTO voter) {
        VoterDao vd = new VoterDao();
        vd.update(voter);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path(value = "/{id}")
    public Response deleteVoter(@PathParam("id") Long id) {
        try {
            VoterDao vd = new VoterDao();
            vd.delete(id);
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

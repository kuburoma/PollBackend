package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.BallotDao;
import cz.wa2.poll.backend.dao.PollDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dao.VoterGroupDao;
import cz.wa2.poll.backend.dto.BallotDTO;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.exception.DaoException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/voter")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class VoterRest {

    ConvertorDTO convertorDTO = new ConvertorDTO();
    VoterDao vd = new VoterDao();
    VoterGroupDao voterGroupDao = new VoterGroupDao();
    PollDao pollDao = new PollDao();
    BallotDao ballotDao = new BallotDao();

    @GET
    public Response getVoters() {
        try {
            VoterDao vd = new VoterDao();
            return Response.status(Response.Status.OK).entity(convertorDTO.convertVoterToDTO(vd.findAll(null, null))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{id}")
    public Response getVoter(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(new VoterDTO(vd.find(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{id}/supervised_groups")
    public Response getSupervisedGroups(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(convertorDTO.convertVoterGroupToDTO(voterGroupDao.getSupervisedGroups(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{id}/registred_groups")
    public Response getRegistredGroups(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(convertorDTO.convertVoterGroupToDTO(voterGroupDao.getVoterGroupsWithVoter(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{id}/notregistred_groups")
    public Response getNotregistredGroups(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(convertorDTO.convertVoterGroupToDTO(voterGroupDao.getVoterGroupsWithoutVoter(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{id}/nonvoted_polls")
    public Response getNonvotedPolls(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(convertorDTO.convertPollToDTO(pollDao.getNonvotedPolls(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{id}/voted_polls")
    public Response getVotedPolls(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(convertorDTO.convertPollToDTO(pollDao.getVotedPolls(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/login")
    public Response getLogin(@QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            Voter voter = vd.getVoterByEmail(email);

            if (voter != null && voter.getPassword().equals(password)) {
                return Response.status(Response.Status.OK).entity(new VoterDTO(voter)).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    public Response saveVoter(VoterDTO voter) {
        try {
            Voter voterByEmail = vd.getVoterByEmail(voter.getEmail());
            if (voterByEmail == null) {
                return Response.status(Response.Status.OK).entity(new VoterDTO(vd.create(voter.toEntity()))).build();
            } else {
                return Response.status(Response.Status.CONFLICT).entity("Email already exist").build();
            }
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/{voter}/poll/{poll}/ballot")
    public Response updateBallot(@PathParam("voter") Long voterId, @PathParam("poll") Long pollId, BallotDTO ballot) {
        try {
            return Response.status(Response.Status.OK).entity(new BallotDTO(ballotDao.getBallot(voterId,pollId))).build();
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

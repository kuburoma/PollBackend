package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.BallotDao;
import cz.wa2.poll.backend.dao.PollDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dao.VoterGroupDao;
import cz.wa2.poll.backend.dto.BallotDTO;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.dto.VoterGroupDTO;
import cz.wa2.poll.backend.entities.EntitiesList;
import cz.wa2.poll.backend.entities.Poll;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/voter")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class VoterRest {

    ConvertorDTO convertorDTO = new ConvertorDTO();
    VoterDao dao = new VoterDao();
    VoterGroupDao voterGroupDao = new VoterGroupDao();
    PollDao pollDao = new PollDao();
    BallotDao ballotDao = new BallotDao();

    @GET
    public Response getVoterGroups(@QueryParam("offset") Integer offset,
                                   @QueryParam("base") Integer base,
                                   @QueryParam("order") String order) {
        try {
            EntitiesList<Voter> entitiesList = dao.findAll(offset, base, order);
            return Response.status(Response.Status.OK).header("Count-records", entitiesList.getTotalSize()).entity(convertorDTO.convertVoterToDTO(entitiesList.getEntities())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(value = "/{id}")
    public Response getVoter(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(new VoterDTO(dao.find(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Vrati skupinu vzhledem ke vztahu k voterovy a to dle findWho
     * 0 - Neni ani clenem ci supervisorem skupiny.
     * 1 - Je clenem skupiny
     * 2 - Je supervisorem skupiny
     *
     * @param id
     * @param findWho
     * @param offset
     * @param base
     * @param order
     * @return
     */
    @GET
    @Path(value = "/{id}/group")
    public Response getVoterGroups(@PathParam("id") Long id,
                                   @QueryParam("findWho") Integer findWho,
                                   @QueryParam("offset") Integer offset,
                                   @QueryParam("base") Integer base,
                                   @QueryParam("order") String order) {
        try {
            EntitiesList<VoterGroup> entitiesList = voterGroupDao.findVoterGroups(id, findWho, offset, base, order);
            return Response.status(Response.Status.OK).header("Count-records", entitiesList.getTotalSize()).entity(convertorDTO.convertVoterGroupToDTO(entitiesList.getEntities())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(value = "/{id}/poll")
    public Response getPolls(@PathParam("id") Long id,
                             @QueryParam("voted") Integer voted,
                             @QueryParam("offset") Integer offset,
                             @QueryParam("base") Integer base,
                             @QueryParam("order") String order) {
        try {
            EntitiesList<Poll> entitiesList = pollDao.findUserPolls(id, voted, offset, base, order);
            return Response.status(Response.Status.OK).header("Count-records", entitiesList.getTotalSize()).entity(convertorDTO.convertPollToDTO(entitiesList.getEntities())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @GET
    @Path(value = "/{voter}/poll/{poll}/ballot")
    public Response getBallot(@PathParam("voter") Long voterId,
                              @PathParam("poll") Long pollId) {
        try {
            return Response.status(Response.Status.OK).entity(new BallotDTO(ballotDao.findBallot(voterId, pollId))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/login")
    public Response getLogin(@QueryParam("email") String email,
                             @QueryParam("password") String password) {
        try {
            Voter voter = dao.findVoterByEmail(email);

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
    @Path(value = "/{id}/group")
    public Response saveVoterGroup(
            @PathParam("id") Long voterId,
            VoterGroupDTO voterGroup) {
        try {
            voterGroupDao.create(voterId, voterGroup.toEntity());

            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    public Response saveVoter(VoterDTO voter) {
        try {
            if (!testMinLength(voter.getFirstName(), 4)) {
                throw new InputException("Křestní jméno musí mít minimálně délku 4.");
            }
            if (!testMinLength(voter.getLastName(), 4)) {
                throw new InputException("Příjmení musí mít minimálně délku 4.");
            }
            if (!testMinLength(voter.getEmail(), 4)) {
                throw new InputException("Email musí mít minimálně délku 4.");
            }
            if (!testMinLength(voter.getPassword(), 4)) {
                throw new InputException("Heslo musí mít minimálně délku 4.");
            }
            Voter voterByEmail = dao.findVoterByEmail(voter.getEmail());

            if (voterByEmail != null) {
                throw new InputException("Email je již používán");
            }
            return Response.status(Response.Status.OK).entity(new VoterDTO(dao.create(voter.toEntity()))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    public Response updateVoter(VoterDTO voter) {
        try {
            VoterDao dao = new VoterDao();
            dao.update(voter.toEntity());
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
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

    private boolean testMinLength(String string, int length) {
        if (string == null) {
            return false;
        }
        if (string.length() < length) {
            return false;
        }
        return true;
    }
}

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
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/voter")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class VoterRest {

    final static Logger logger = Logger.getLogger(VoterGroupRest.class);
    ConvertorDTO convertorDTO = new ConvertorDTO();
    VoterDao dao = new VoterDao();
    VoterGroupDao voterGroupDao = new VoterGroupDao();
    PollDao pollDao = new PollDao();
    BallotDao ballotDao = new BallotDao();

    @GET
    public Response getVoters(@QueryParam("offset") Integer offset,
                                   @QueryParam("base") Integer base,
                                   @QueryParam("order") String order) {
        try {
            debugMessage("getVoters: Received");
            EntitiesList<Voter> entitiesList = dao.findAll(offset, base, order);
            debugMessage("getVoters: 200");
            return Response.status(Response.Status.OK).header("Count-records", entitiesList.getTotalSize()).entity(convertorDTO.convertVoterToDTO(entitiesList.getEntities())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("getVoters: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            debugMessage("getVoters: 400");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(value = "/{id}")
    public Response getVoter(@PathParam("id") Long id) {
        try {
            debugMessage("getVoter: Received");
            VoterDTO object = new VoterDTO(dao.find(id));
            debugMessage("getVoter: 200");
            return Response.status(Response.Status.OK).entity(object).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("getVoter: 500");
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
            debugMessage("getVoterGroups: Received");
            EntitiesList<VoterGroup> entitiesList = voterGroupDao.findVoterGroups(id, findWho, offset, base, order);
            debugMessage("getVoterGroups: 200");
            return Response.status(Response.Status.OK).header("Count-records", entitiesList.getTotalSize()).entity(convertorDTO.convertVoterGroupToDTO(entitiesList.getEntities())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("getVoterGroups: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            debugMessage("getVoterGroups: 400");
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
            debugMessage("getPolls: Received");
            EntitiesList<Poll> entitiesList = pollDao.findUserPolls(id, voted, offset, base, order);
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
    @Path(value = "/{voter}/poll/{poll}/ballot")
    public Response getBallot(@PathParam("voter") Long voterId,
                              @PathParam("poll") Long pollId) {
        try {
            debugMessage("getBallot: Received");
            BallotDTO object = new BallotDTO(ballotDao.findBallot(voterId, pollId));
            debugMessage("getBallot: 200");
            return Response.status(Response.Status.OK).entity(object).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("getBallot: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/login")
    public Response getLogin(@QueryParam("email") String email,
                             @QueryParam("password") String password) {
        try {
            debugMessage("getLogin: Received");
            Voter voter = dao.findVoterByEmail(email);

            if (voter != null && voter.getPassword().equals(password)) {
                debugMessage("getLogin: 200");
                return Response.status(Response.Status.OK).entity(new VoterDTO(voter)).build();
            } else {
                debugMessage("getLogin: 401");
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("getLogin: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @POST
    @Path(value = "/{id}/group")
    public Response saveVoterGroup(
            @PathParam("id") Long voterId,
            VoterGroupDTO voterGroup) {
        try {
            debugMessage("saveVoterGroup: Received");
            if(voterGroupDao.findVoterGroupByName(voterGroup.getName()) == null){
                voterGroupDao.create(voterId, voterGroup.toEntity());
            }else{
                throw new InputException("Skupina se stejným jménem již existuje.");
            }
            debugMessage("saveVoterGroup: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("saveVoterGroup: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            debugMessage("saveVoterGroup: 400");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response saveVoter(VoterDTO voter) {
        try {
            debugMessage("saveVoter: Received");
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
            debugMessage("saveVoter: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            debugMessage("saveVoter: 400");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    public Response updateVoter(VoterDTO voter) {
        try {
            debugMessage("updateVoter: Received");
            Voter help = dao.findVoterByEmail(voter.getEmail());
            if(help != null && help.getId() == voter.getId()){
                throw new InputException("Uživatel s tímto emailem již existuje");
            }
            dao.update(voter.toEntity());
            debugMessage("updateVoter: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("updateVoter: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            debugMessage("updateVoter: 400");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path(value = "/{id}")
    public Response deleteVoter(@PathParam("id") Long id) {
        try {
            debugMessage("deleteVoter: Received");
            VoterDao vd = new VoterDao();
            vd.delete(id);
            debugMessage("deleteVoter: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("deleteVoter: 500");
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

    private void debugMessage(String message){
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}

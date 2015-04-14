package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.BallotDao;
import cz.wa2.poll.backend.dao.PollDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dao.VoterGroupDao;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.dto.PollDTO;
import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.dto.VoterGroupDTO;
import cz.wa2.poll.backend.entities.*;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;
import cz.wa2.poll.backend.websocket.Producer;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("/votergroup")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class VoterGroupRest {

    final static Logger logger = Logger.getLogger(VoterGroupRest.class);
    ConvertorDTO convertorDTO = new ConvertorDTO();
    VoterGroupDao dao = new VoterGroupDao();
    VoterDao voterDao = new VoterDao();
    PollDao pollDao = new PollDao();

    @GET
    public Response getVoterGroups(@QueryParam("offset") Integer offset,
                                   @QueryParam("base") Integer base,
                                   @QueryParam("order") String order) {
        try {
            debugMessage("getVoterGroups: Received");
            EntitiesList<VoterGroup> entitiesList = dao.findAll(offset, base, order);
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
    @Path(value = "/{id}")
    public Response getVoterGroup(@PathParam("id") Long id) {
        try {
            debugMessage("getVoterGroup: Received");
            VoterGroupDTO object = new VoterGroupDTO(dao.find(id));
            debugMessage("getVoterGroup: 200");
            return Response.status(Response.Status.OK).entity(object).build();
        } catch (DaoException e) {
            debugMessage("getVoterGroup: 500");
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}/voter")
    public Response getVoters(@PathParam("id") Long id,
                              @QueryParam("offset") Integer offset,
                              @QueryParam("base") Integer base,
                              @QueryParam("order") String order) {
        try {
            debugMessage("getVoters: Received");
            EntitiesList<Voter> entitiesList = voterDao.findVotersOfVoterGroup(id, offset, base, order);
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

    @POST
    public Response saveVoterGroup(VoterGroupDTO voterGroupDTO) {
        try {
            debugMessage("saveVoterGroup: Received");
            VoterGroup voterGroup = dao.create(voterGroupDTO.toEntity());
            debugMessage("saveVoterGroup: 200");
            return Response.status(Response.Status.OK).entity(new VoterGroupDTO(voterGroup)).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("saveVoterGroup: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/{id}/poll")
    public Response createPoll(@PathParam("id") Long id, PollDTO pollDTO) {
        try {
            debugMessage("createPoll: Received");
            if(voterDao.findVotersOfVoterGroup(id,null,null,null).getEntities().size() < 1 ){
                throw new InputException("Hlasování by nemělo žádné členy");
            }
            if (pollDao.findPollByName(pollDTO.getName()) == null) {
                PollDTO dto = new PollDTO(dao.createPoll(pollDTO.toEntity(), id));
                Producer producer = new Producer("hlasovani");
                producer.sendMessage("Nové hlasování: "+dto.getName());
                producer.close();
                debugMessage("createPoll: 200");
                return Response.status(Response.Status.OK).entity(dto).build();
            } else {
                throw new InputException("Hlasování se stejným jménem již existuje.");
            }
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("createPoll: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            debugMessage("createPoll: 400");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (IOException e) {
            e.printStackTrace();
            debugMessage("createPoll: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateVoterGroup(@PathParam("id") Long id, VoterGroupDTO voterGroupDTO) {
        try {
            debugMessage("updateVoterGroup: Received");
            VoterGroup vg = dao.find(id);
            vg.setName(voterGroupDTO.getName());
            vg.setDescription(voterGroupDTO.getDescription());
            dao.update(vg);
            debugMessage("updateVoterGroup: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("updateVoterGroup: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{votergroup}/voter/{voter}")
    public Response putVoterToVotergroup(@PathParam("votergroup") Long votergroup, @PathParam("voter") Long voter) {
        try {
            debugMessage("putVoterToVotergroup: Received");
            dao.putVoter(votergroup, voter);
            debugMessage("putVoterToVotergroup: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("putVoterToVotergroup: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path(value = "/{id}")
    public Response deleteVoterGroup(@PathParam("id") Long id) {
        try {
            debugMessage("deleteVoterGroup: Received");
            dao.delete(id);
            debugMessage("deleteVoterGroup: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("deleteVoterGroup: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{votergroup}/voter/{voter}")
    public Response deleteVoterFromVotergroup(@PathParam("votergroup") Long votergroup, @PathParam("voter") Long voter) {
        try {
            debugMessage("deleteVoterFromVotergroup: Received");
            dao.removeVoter(votergroup, voter);
            debugMessage("deleteVoterFromVotergroup: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("deleteVoterFromVotergroup: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void debugMessage(String message){
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}

package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.BallotDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dao.VoterGroupDao;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.dto.PollDTO;
import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.dto.VoterGroupDTO;
import cz.wa2.poll.backend.entities.*;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("/votergroup")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON+ "; charset=UTF-8")
public class VoterGroupRest {

    ConvertorDTO convertorDTO = new ConvertorDTO();
    VoterGroupDao dao = new VoterGroupDao();
    VoterDao voterDao = new VoterDao();

    @GET
    public Response getVoterGroups(@QueryParam("offset") Integer offset,
                             @QueryParam("base") Integer base,
                             @QueryParam("order") String order) {
        try {
            EntitiesList<VoterGroup> entitiesList = dao.findAll(offset, base, order);
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
    @Path(value = "/{id}")
    public Response getVoterGroup(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(new VoterGroupDTO(dao.find(id))).build();
        } catch (DaoException e) {
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
            EntitiesList<Voter> entitiesList = voterDao.findVotersOfVoterGroup(id, offset, base, order);
            return Response.status(Response.Status.OK).header("Count-records", entitiesList.getTotalSize()).entity(convertorDTO.convertVoterToDTO(entitiesList.getEntities())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response saveVoterGroup(VoterGroupDTO voterGroupDTO) {
        try {
            VoterGroup voterGroup = dao.create(voterGroupDTO.toEntity());
            return Response.status(Response.Status.OK).entity(new VoterGroupDTO(voterGroup)).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/{id}/poll")
    public Response createPoll(@PathParam("id") Long id, PollDTO pollDTO) {
        try {
            return Response.status(Response.Status.OK).entity(new PollDTO(dao.createPoll(pollDTO.toEntity(), id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response saveVoterGroup(@PathParam("id") Long id, VoterGroupDTO voterGroupDTO) {
        try {

            VoterGroup vg = dao.find(id);
            vg.setName(voterGroupDTO.getName());
            vg.setDescription(voterGroupDTO.getDescription());
            dao.update(vg);
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{votergroup}/voter/{voter}")
    public Response putVoterToVotergroup(@PathParam("votergroup") Long votergroup, @PathParam("voter") Long voter) {
        try {
            dao.putVoter(votergroup, voter);
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path(value = "/{id}")
    public Response deleteBallot(@PathParam("id") Long id) {
        try {
            dao.delete(id);
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{votergroup}/voter/{voter}")
    public Response deleteVoterFromVotergroup(@PathParam("votergroup") Long votergroup, @PathParam("voter") Long voter) {
        try {
            dao.removeVoter(votergroup, voter);
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

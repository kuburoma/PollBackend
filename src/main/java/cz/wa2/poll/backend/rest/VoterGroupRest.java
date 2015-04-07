package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.VoterGroupDao;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.dto.PollDTO;
import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.dto.VoterGroupDTO;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.entities.VoterGroup;
import cz.wa2.poll.backend.exception.DaoException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Path("/votergroup")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class VoterGroupRest {

    ConvertorDTO convertorDTO = new ConvertorDTO();
    VoterGroupDao vd = new VoterGroupDao();

    @GET
    @Path("/{id}/voters")
    public Response getVoters(@PathParam("id") Long id) {
        try {
            List<Voter> voters = vd.findVoters(id);
            List<VoterDTO> voterDTOs = new ArrayList<VoterDTO>();
            Iterator<Voter> it = voters.iterator();
            while (it.hasNext()) {
                voterDTOs.add(new VoterDTO(it.next()));
            }
            return Response.status(Response.Status.OK).entity(voterDTOs).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    public Response saveVoterGroup(VoterGroupDTO voterGroupDTO) {
        try {
            VoterGroup voterGroup = vd.create(voterGroupDTO.toEntity());
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
            return Response.status(Response.Status.OK).entity(new PollDTO(vd.createPoll(pollDTO.toEntity(), id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response saveVoterGroup(@PathParam("id") Long id, VoterGroupDTO voterGroupDTO) {
        try {

            VoterGroup vg = vd.find(id);
            vg.setName(voterGroupDTO.getName());
            vg.setDescription(voterGroupDTO.getDescription());
            vd.update(vg);
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
            vd.putVoter(votergroup,voter);
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
            vd.removeVoter(votergroup, voter);
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.VoterGroupDao;
import cz.wa2.poll.backend.dto.ConvertorDTO;
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

    @GET
    @Path("/{id}/voters")
    public Response getVotersOfVoterGroup(@PathParam("id") Long id) {
        try {
            VoterGroupDao vd = new VoterGroupDao();
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
            VoterGroupDao vd = new VoterGroupDao();
            VoterGroup voterGroup = vd.create(voterGroupDTO.toEntity());
            return Response.status(Response.Status.OK).entity(new VoterGroupDTO(voterGroup)).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response saveVoterGroup(@PathParam("id") Long id, VoterGroupDTO voterGroupDTO) {
        try {
            VoterGroupDao vd = new VoterGroupDao();
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
    @Path("/{id}/remove/{voter}/voter")
    public Response saveVoterGroup(@PathParam("id") Long id, @PathParam("voter") Long idVoter) {
        try {
            VoterGroupDao vd = new VoterGroupDao();
            vd.removeVoter(id, idVoter);
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<VoterGroupDTO> convertVoterGroupToDTO(List<VoterGroup> voterGroups){
        List<VoterGroupDTO> voterGroupDTOs = new ArrayList<VoterGroupDTO>();
        Iterator<VoterGroup> it = voterGroups.iterator();
        while (it.hasNext()){
            voterGroupDTOs.add(new VoterGroupDTO(it.next()));
        }
        return voterGroupDTOs;
    }

}

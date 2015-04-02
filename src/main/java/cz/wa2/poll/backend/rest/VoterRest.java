package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.dto.VoterFullDTO;
import cz.wa2.poll.backend.entities.Voter;
import cz.wa2.poll.backend.exception.DaoException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Path("/voter")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class VoterRest {

    @GET
    public Response getVoters() {
        try {
            VoterDao vd = new VoterDao();
            List<Voter> voters = vd.findAll();
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

    @GET
    @Path(value = "/{id}")
    public Response getVoter(@PathParam("id") Long id) {
        try {
            VoterDao vd = new VoterDao();
            return Response.status(Response.Status.OK).entity(new VoterDTO(vd.find(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path(value = "/login")
    public Response getLogin(@QueryParam("email") String email, @QueryParam("password") String password) {
        VoterDao vd = new VoterDao();
        Voter voter = vd.getVoterByEmail(email);
        if (voter == null) {
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
        if (voter.getPassword().equals(password)) {
            return Response.status(Response.Status.OK).entity(new VoterDTO(voter)).build();
        } else {
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
    }

    @POST
    public Response saveVoter(VoterFullDTO voter) {
        try {
            VoterDao vd = new VoterDao();
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

    @PUT
    public Response updateVoter(VoterFullDTO voter) {
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

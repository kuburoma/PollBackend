package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.BallotDao;
import cz.wa2.poll.backend.dto.BallotDTO;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.EntitiesList;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/ballot")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON+ "; charset=UTF-8")
public class BallotRest {

    ConvertorDTO convertorDTO = new ConvertorDTO();
    BallotDao ballotDao = new BallotDao();


    @GET
    public Response getVoters(
            @QueryParam("offset") Integer offset,
            @QueryParam("base") Integer base,
            @QueryParam("order") String order) {
        try {
            EntitiesList<Ballot> ballotEntitiesList = ballotDao.findAll(offset, base, order);

            return Response.status(Response.Status.OK).header("Count-records", ballotEntitiesList.getTotalSize()).entity(convertorDTO.convertBallotToDTO(ballotEntitiesList.getEntities())).build();
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
            return Response.status(Response.Status.OK).entity(new BallotDTO(ballotDao.find(id))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    public Response saveVoter(BallotDTO ballotDTO) {
        try {
            return Response.status(Response.Status.OK).entity(new BallotDTO(ballotDao.create(ballotDTO.toEntity()))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PUT
    public Response updateBallot(BallotDTO ballotDTO) {
        try {
            ballotDao.update(ballotDTO.toEntity());
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path(value = "/{id}")
    public Response deleteBallot(@PathParam("id") Long id) {
        try {
            ballotDao.delete(id);
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

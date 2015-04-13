package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.BallotDao;
import cz.wa2.poll.backend.dto.BallotDTO;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.entities.Ballot;
import cz.wa2.poll.backend.entities.EntitiesList;
import cz.wa2.poll.backend.exception.DaoException;
import cz.wa2.poll.backend.exception.InputException;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/ballot")
@Consumes({MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON+ "; charset=UTF-8")
public class BallotRest {

    final static Logger logger = Logger.getLogger(BallotRest.class);
    ConvertorDTO convertorDTO = new ConvertorDTO();
    BallotDao dao = new BallotDao();


    @GET
    public Response getBallots(
            @QueryParam("offset") Integer offset,
            @QueryParam("base") Integer base,
            @QueryParam("order") String order) {
        try {

            debugMessage("getBallots: Received");

            EntitiesList<Ballot> entitiesList = dao.findAll(offset, base, order);

            debugMessage("getBallots: 200");

            return Response.status(Response.Status.OK).header("Count-records", entitiesList.getTotalSize()).entity(convertorDTO.convertBallotToDTO(entitiesList.getEntities())).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("getBallots: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (InputException e) {
            e.printStackTrace();
            debugMessage("getBallots: 200");
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path(value = "/{id}")
    public Response getBallot(@PathParam("id") Long id) {
        try {
            debugMessage("getBallot: Received");
            BallotDTO object = new BallotDTO(dao.find(id));
            debugMessage("getBallot: 200");
            return Response.status(Response.Status.OK).entity(object).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("getBallot: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

/*    @POST
    public Response saveBallot(BallotDTO ballotDTO) {
        try {
            debugMessage("saveVoter: Received");


            return Response.status(Response.Status.OK).entity(new BallotDTO(dao.create(ballotDTO.toEntity()))).build();
        } catch (DaoException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }*/


    @PUT
    public Response updateBallot(BallotDTO ballotDTO) {
        try {
            debugMessage("updateBallot: Received");

            dao.update(ballotDTO.toEntity());
            debugMessage("updateBallot: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("updateBallot: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DELETE
    @Path(value = "/{id}")
    public Response deleteBallot(@PathParam("id") Long id) {
        try {
            debugMessage("deleteBallot: Received");
            dao.delete(id);
            debugMessage("deleteBallot: 200");
            return Response.status(Response.Status.OK).build();
        } catch (DaoException e) {
            e.printStackTrace();
            debugMessage("deleteBallot: 500");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void debugMessage(String message){
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}

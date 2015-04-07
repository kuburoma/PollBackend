package cz.wa2.poll.backend.rest;

import cz.wa2.poll.backend.dao.BallotDao;
import cz.wa2.poll.backend.dao.PollDao;
import cz.wa2.poll.backend.dao.VoterDao;
import cz.wa2.poll.backend.dto.BallotDTO;
import cz.wa2.poll.backend.dto.ConvertorDTO;
import cz.wa2.poll.backend.dto.PollDTO;
import cz.wa2.poll.backend.dto.VoterDTO;
import cz.wa2.poll.backend.exception.DaoException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/ballot")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class BallotRest {

    ConvertorDTO convertorDTO = new ConvertorDTO();
    BallotDao ballotDao = new BallotDao();


    @PUT
    public Response updateBallot(BallotDTO ballotDTO) {
        try {
            ballotDao.update(ballotDTO.toEntity());
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).build();
    }

}

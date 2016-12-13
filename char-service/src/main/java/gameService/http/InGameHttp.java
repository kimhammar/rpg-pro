package gameService.http;

import gameService.ejb.GameHelper;
import gameService.ejb.GameSession;
import gameService.exceptions.MapOutOfBoundsExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.response.ResponseHelper;
import shared.response.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Map;

/**
 * Created by kimha on 12/3/16.
 */
@Path("/play")
public class InGameHttp {


    private static final Logger LOGGER = LoggerFactory.getLogger(InGameHttp.class);

    @Inject
    GameHelper gameHelper;


    @Inject
    RestResponse<Map<String, Object>> respen;

    @Inject
    ResponseHelper responseHelper;


    @GET
    @Path("/select/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Map<String,Object>> chooseCharacter(@Context SecurityContext sc, @PathParam("id") int creatureId) {
        String userName = sc.getUserPrincipal().getName();
        LOGGER.info(String.format("Selecting creature for player [%s]", userName));


        try {
            gameHelper.joinGame(userName, creatureId);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            restie.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            responseHelper.putItem("msg", e.getMessage());
        }

        restie.setData(responseHelper.getData());
        restie.setStatus(responseHelper.getStatus());
        return restie;
    }


    @Inject
    RestResponse<Map<String, Object>> restie;

    @Path("/move")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse moveCharacter(@Context SecurityContext sc, @QueryParam("direction") String direction) {
        String userName = sc.getUserPrincipal().getName();
        LOGGER.info(String.format("Player [%s] trying to move creature", userName));
        RestResponse<GameSession> restResponse = new RestResponse<>();

        if (direction == null) {
            restResponse.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            responseHelper.putItem("msg", "direction can't be null");
            return restResponse;
        } else {
            direction = direction.toUpperCase();
        }

        try {
            gameHelper.requestMove(direction);
        } catch (MapOutOfBoundsExeption mapOutOfBoundsExeption) {
            LOGGER.warn(mapOutOfBoundsExeption.getMessage());
            restie.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        }
        restie.setData(responseHelper.getData());
        restie.setStatus(responseHelper.getStatus());
        return restie;
    }


}

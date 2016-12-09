package gameService.http;

import gameService.ejb.GameHelper;
import gameService.ejb.GameSession;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import shared.exceptions.FailedToJoinGameException;
import shared.entities.RestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.UUID;

/**
 * Created by kimha on 12/3/16.
 */
@Path("/play")
public class InGameHttp {




    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(InGameHttp.class);

    @Inject
    GameHelper gameHelper;


    @GET
    @Path("/select")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String> chooseCharacter(@Context SecurityContext sc, @QueryParam("creatureId") int creatureId) {
        String userName = sc.getUserPrincipal().getName();
        LOGGER.info(String.format("Selecting creature for player [%s]", userName));
        RestResponse<String> restResponse = null;
        try {
            restResponse = gameHelper.joinGame(sc, creatureId);
        } catch (FailedToJoinGameException e) {
            //TODO: Log here
            e.printStackTrace();
        }
        return restResponse;
    }


//    @RequestScoped
//    @Inject
//    String transactionId = UUID.randomUUID().toString();


    @Path("/move")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse moveCharacter(@Context SecurityContext sc, @QueryParam("direction") String direction) {
//        MDC.put("transactionId", transactionId);
        String userName = sc.getUserPrincipal().getName();
        LOGGER.info(String.format("Player [%s] trying to move creature", userName));
        RestResponse<GameSession> restResponse = new RestResponse<>();

        if (direction == null) {
            restResponse.setStatus(420);
            return restResponse;
        } else {
            direction = direction.toUpperCase();
        }

        restResponse = gameHelper.requestMove(direction);


        return restResponse;
    }


    @GET
    @Path("/dosomething")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String> doSomething() {
        RestResponse<String> restResponse = new RestResponse<>();
        return restResponse;

    }

}

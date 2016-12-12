package http;

import gameService.ejb.Game;
import shared.response.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by kimha on 12/3/16.
 */
@Path("/game")
public class RestHttp {

    @Inject
    Game game;

    @Path("/{move}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<MoveResponse> makeAMove(@QueryParam("move") String move, @Context SecurityContext context){
        String userName = context.getUserPrincipal().getName();
        MoveRequest moveRequest = new MoveRequest(userName,move);
        return null;
    }
}

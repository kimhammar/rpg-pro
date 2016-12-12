package http;

import entities.GameStatus;
import shared.response.RestResponse;

import javax.resource.spi.work.SecurityContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by kimha on 12/6/16.
 */

@Path("/play")
public class GameHttp {

    @GET
    @Path("/join")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<GameStatus> joinGame(@Context SecurityContext sc, @QueryParam("creatureId") Integer id){
        //Add creature to game instance


        return null;
    }

}

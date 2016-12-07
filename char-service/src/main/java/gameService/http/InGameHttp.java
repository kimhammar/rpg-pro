package gameService.http;

import gameService.ejb.GameHelper;
import shared.exceptions.FailedToJoinGameException;
import shared.entities.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by kimha on 12/3/16.
 */
@Path("/play")
public class InGameHttp {


    @Inject
    GameHelper gameHelper;




    @GET
    @Path("/select")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String> chooseCharacter(@Context SecurityContext sc, @QueryParam("creatureId") int creatureId){
        RestResponse<String> restResponse = null;
        try {
            restResponse = gameHelper.joinGame(sc, creatureId);
        } catch (FailedToJoinGameException e) {
            //TODO: Log here
            e.printStackTrace();
        }
        return restResponse;
    }



    @Path("/move")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse moveCharacter(@Context SecurityContext sc, @QueryParam("direction") String direction){
        RestResponse<String> restResponse = new RestResponse<>();

        if(direction == null){
            restResponse.setStatus(400);
            restResponse.setData("direction must be \"north\",\"south\",\"west\" or \"east\"");
            return restResponse;
        } else {
        }
        return null;
    }


    @GET
    @Path("/dosomething")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String> doSomething(){
        RestResponse<String> restResponse = new RestResponse<>();
        return restResponse;

    }

}

package http;

import ejb.CharacterEJB;
import entities.Creature;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by kimha on 11/24/16.
 */
@Path("/")
public class RestHttp {

    private static final Logger LOGGER = Logger.getLogger(RestHttp.class.getName());

    @Inject
    private CharacterEJB characterEJB;


    //Should be a post to path("/"), get used for easier testing
    //TODO: change to POST
    @GET
    @Path("store")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String> getCharacters(@Context SecurityContext securityContext, @QueryParam("creatureName") String creatureName,
                                              @QueryParam("creatureType") String creatureType) {
        //Get username for user sending request
        String userName = securityContext.getUserPrincipal().getName();

        //Try to store data submitted by user
        RestResponse<String> restResponse = characterEJB.storeCreature(userName, creatureName, creatureType);

        //Return reponse object with status and possible data
        return restResponse;
    }


    //Endpoint for retrieving all characters for logged in user
    //TODO: make it consume JSON
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<List<Creature>> getCreatures(@Context SecurityContext securityContext) {
        //Get username for user sending request
        String userName = securityContext.getUserPrincipal().getName();

        //Log who is trying to retrieve creatures
        LOGGER.info("Retrieving creatures for player " + userName);

        RestResponse<List<Creature>> restResponse = characterEJB.getCreature(userName);
        return restResponse;
    }


    //TODO: add PUT and DELETE


}

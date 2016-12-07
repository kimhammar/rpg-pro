package creatureService.http;

import shared.ejb.CharacterDbEjb;
import shared.entities.Creature;
import shared.entities.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.UUID;


/**
 * Created by kimha on 11/24/16.
 */
@Path("/character")
public class RestHttp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestHttp.class);

    @Inject
    private CharacterDbEjb characterDbEjb;

//    @Inject
    String uuid = UUID.randomUUID().toString();


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

        RestResponse<List<Creature>> restResponse = characterDbEjb.getCreatures(userName);
        restResponse.setTransactionId(uuid + "");
        return restResponse;
    }

    //Should be a post to path("/"), get used for easier testing
    //TODO: change to POST
    @GET
    @Path("/store")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String> getCharacters(@Context SecurityContext securityContext, @QueryParam("creatureName") String creatureName) {
            if (creatureName == null) {
                RestResponse<String> restResponse = new RestResponse<>();
                restResponse.setStatus(400);
                restResponse.setTransactionId(uuid + "");
                LOGGER.info(String.format("Failed to create creature for user [%s], field was null ", securityContext.getUserPrincipal().getName()));
                return restResponse;
            }

        //Get username for user sending request
        String userName = securityContext.getUserPrincipal().getName();

        //Try to store data submitted by user
        RestResponse<String> restResponse = characterDbEjb.storeCreature(userName, creatureName);
        restResponse.setTransactionId(uuid);
        //Return reponse object with status and possible data
        return restResponse;
    }





    //TODO: add PUT and DELETE


}

package creatureService.http;

import ch.qos.logback.core.util.StatusPrinter;
import org.apache.log4j.MDC;
import shared.ejb.CharacterDbEjb;
import shared.entities.Creature;
import shared.entities.RestResp;
import shared.entities.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by kimha on 11/24/16.
 */
@Path("/character")
public class RestHttp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestHttp.class);

    @Inject
    private CharacterDbEjb characterDbEjb;

    String transactionId = UUID.randomUUID().toString();

    @Inject
    RestResp resp;


    //Endpoint for retrieving all characters for logged in user
    //TODO: make it consume JSON
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResp getCreatures(@Context SecurityContext securityContext) {
        //Get username for user sending request
        String userName = securityContext.getUserPrincipal().getName();


        LOGGER.info(String.format("Trying to retrieve creatures for player [%s]", userName));
//        characterDbEjb.getCreatures(userName);
        resp.setTransactionId(transactionId + "");
        return resp;
    }

// Endpoint for retrieving all characters for logged in user
//    //TODO: make it consume JSON
//    @GET
//    @Path("/")
//    @Produces(MediaType.APPLICATION_JSON)
//    public RestResponse<List<Creature>> getCreatures(@Context SecurityContext securityContext) {
//        //Get username for user sending request
//        String userName = securityContext.getUserPrincipal().getName();
//
//
//        LOGGER.info(String.format("Trying to retrieve creatures for player [%s]", userName));
//
//        RestResponse<List<Creature>> restResponse = characterDbEjb.getCreatures(userName);
//        restResponse.setTransactionId(transactionId + "");
//        return restResponse;
//    }


    @GET
    @Path("/balle")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Map<String,String>> getString(@Context SecurityContext securityContext) {
        Map<String,String> string = new HashMap<>();
        string.put("name",securityContext.getUserPrincipal().getName());
        string.put("Zlatte",securityContext.getUserPrincipal().getName());
        string.put("Batte",securityContext.getUserPrincipal().getName());
        RestResponse<Map<String,String>> restResponse = new RestResponse<>();


        restResponse.setData(string);
        return restResponse;
    }




    //Should be a post to path("/"), get used for easier testing
    //TODO: change to POST
    @GET
    @Path("/store")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String> createCreature(@Context SecurityContext securityContext, @QueryParam("creatureName") String creatureName) {
        String userName = securityContext.getUserPrincipal().getName();
        LOGGER.info(String.format("Trying to add new creature for player [%s]", userName));

        if (creatureName == null) {
                RestResponse<String> restResponse = new RestResponse<>();
                restResponse.setStatus(400);
                restResponse.setTransactionId(transactionId + "");
                LOGGER.info(String.format("Failed to create creature for user [%s], field was null ", securityContext.getUserPrincipal().getName()));
                return restResponse;
            }

        //Get username for user sending request

        //Try to store data submitted by user

        RestResponse<String> restResponse = characterDbEjb.storeCreature(userName, creatureName);
        restResponse.setTransactionId(transactionId);
        //Return reponse object with status and possible data
        return restResponse;
    }



    //TODO: add PUT and DELETE


}

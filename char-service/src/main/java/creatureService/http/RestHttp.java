package creatureService.http;

import creatureService.entities.Creature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import creatureService.ejb.CharacterDbEjb;
import shared.response.ResponseHelper;
import shared.response.RestResponse;
import shared.response.TestBean;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Map;


/**
 * Created by kimha on 11/24/16.
 */
@Path("/character")
public class RestHttp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestHttp.class);

    @Inject
    private CharacterDbEjb characterDbEjb;

    @Inject
    ResponseHelper responseHelper;

    @Inject
    RestResponse<Map<String, Object>> respen;


    //Endpoint for retrieving all characters for logged in user
    //TODO: make it consume JSON
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse getCreatures(@Context SecurityContext securityContext) {
        //Get username for user sending request
        String userName = securityContext.getUserPrincipal().getName();

        characterDbEjb.getCreatures(userName);
        LOGGER.info(String.format("Trying to retrieve creatures for player [%s]", userName));
        respen.setData(responseHelper.getData());
        respen.setStatus(responseHelper.getStatus());
        return respen;
    }


    //Should be a post to path("/"), get used for easier testing
    //TODO: change to POST
    @POST
    @Path("/store")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Map<String, Object>> createCreature(@Context SecurityContext securityContext, Creature creature) {
        String userName = securityContext.getUserPrincipal().getName();
        LOGGER.info(String.format("Trying to add new creature for player [%s]", creature.getName()));


        LOGGER.info(String.format("Trying to add new creature for player [%s]", userName));

        if (creature.getName() == null) {
            respen.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            responseHelper.putItem("msg", "Failed to create a new creature, field was null");
            respen.setData(responseHelper.getData());
            LOGGER.info(String.format("Failed to create creature for user [%s], field was null ", securityContext.getUserPrincipal().getName()));
            return respen;
        }

        try {
            creature.setOwner(userName);
            characterDbEjb.storeCreature(creature);
        } catch (Exception e) {
            LOGGER.warn(String.format("User [%s] tried to store creature with invalid name [%s]", userName, creature.getName()));
            responseHelper.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            responseHelper.putItem("msg", "Validation failed");
            responseHelper.putItem("error", "must follow constraints");
        }

        respen.setData(responseHelper.getData());
        respen.setStatus(responseHelper.getStatus());
        return respen;
    }


    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Map<String, Object>> deleteCreature(@Context SecurityContext securityContext, @PathParam("id") int id) {
        String userName = securityContext.getUserPrincipal().getName();
        LOGGER.info(String.format("SuperAdmin [%s] trying to delete creature with id [%d] ", userName, id));
        characterDbEjb.deleteCreature(id);
        respen.setData(responseHelper.getData());
        respen.setStatus(responseHelper.getStatus());
        return respen;
    }


    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    @GET
    @Path("/abab")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseHelper getAbAb(){
        responseHelper.setStatus(12);
        responseHelper.putItem("hall","12");
        return responseHelper;
    }

    @GET
    @Path("/baba")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Map<String,Object>> getBaba(){
        responseHelper.setStatus(12);
        responseHelper.putItem("hall","12");
        respen.setData(responseHelper.getData());
        return respen;
    }




    @Inject
    TestBean testBean;

    @GET
    @Path("/caca")
    @Produces(MediaType.APPLICATION_JSON)
    public TestBean getCaca(){
        testBean.setStatus(12);
        testBean.putItem("hall","12");
        return testBean;
    }
//    https://issues.jboss.org/browse/RESTEASY-1238
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////


}

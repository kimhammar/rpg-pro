package ejb;

import entities.Creature;
import http.RestResponse;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by kimha on 11/24/16.
 */

@Singleton
@Startup
public class CharacterEJB {

    private static final Logger LOGGER = Logger.getLogger(CharacterEJB.class.getName());


    @PersistenceContext(unitName = "rpgchar")
    private EntityManager em;


    //Method used for retrieving creatures owned by the user
    public RestResponse getCreature(String userName) {
        RestResponse<List<Creature>> restResponse = new RestResponse<>();

        //TODO: make this a proper query instead of hard coded
        Query q = em.createQuery(String.format("SELECT c FROM Creature c WHERE c.owner LIKE \'%s\'", userName));
        List<Creature> resultList = q.getResultList();
        restResponse.setStatus(200);
        restResponse.setData(resultList);
        return restResponse;
    }

    @Inject
    private Creature creature;


    //Method used for storing a new creature for the user
    public RestResponse storeCreature(String user, String creatureName, String creatureType) {
        LOGGER.info("Trying to save creature for " + user);

        //TODO: return created object
        RestResponse<String> restResponse = new RestResponse<>();
        String error = "fields cannot be null";
        try {
            if (creatureName == null | creatureType == null) {
                throw new NullPointerException(error);
            }
            Creature creature = new Creature();
            creature.setName(creatureName);
            creature.setType(creatureType);
            creature.setOwner(user);
            em.persist(creature);
            restResponse.setStatus(201);
            restResponse.setData("persisted");
            LOGGER.info("Creature persisted for user " + user);
        } catch (Exception e) {
            restResponse.setStatus(400);
            restResponse.setData("failed to persist creature." + error);
            LOGGER.warning("Failed to persist creature for user" + e.getStackTrace());
        }
        return restResponse;
    }
}

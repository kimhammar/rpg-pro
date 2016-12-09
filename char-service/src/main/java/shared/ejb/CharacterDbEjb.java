package shared.ejb;

import shared.entities.Creature;
import shared.entities.RestResp;
import shared.entities.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kimha on 11/24/16.
 */

@Singleton
@Startup
public class CharacterDbEjb {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterDbEjb.class);
//
//    @PostConstruct
//    void init(){
//        LOGGER.info("Character controller initializing...");
//    }


    @PersistenceContext(unitName = "rpgchar")
    private EntityManager em;

    @Inject
    RestResp resp;


    //Method used for retrieving creatures owned by the user
//    public RestResponse getCreatures(String userName) {
//        RestResponse<List<Creature>> restResponse = new RestResponse<>();
//
//        LOGGER.info(String.format("Reading creatures for player [%s] from database", userName));
//
//        //TODO: make this a proper query instead of hard coded
//        Query q = em.createQuery(String.format("SELECT c FROM Creature c WHERE c.owner LIKE \'%s\'", userName));
//        List<Creature> resultList = q.getResultList();
//        restResponse.setStatus(200);
//        restResponse.setData(resultList);
//        return restResponse;
//    }

    public void getCreatures(String userName) {

        LOGGER.info(String.format("Reading creatures for player [%s] from database", userName));

        //TODO: make this a proper query instead of hard coded
        Query q = em.createQuery(String.format("SELECT c FROM Creature c WHERE c.owner LIKE \'%s\'", userName));
        List<Creature> resultList = q.getResultList();
        resp.setStatus(200);
        Map<String,Object> creatures = new HashMap<>();
        creatures.put("creatures",resultList);
        resp.setData(creatures);
    }

    //Method used for retrieving creatures owned by the user
    public Creature getCreature(int id) {

        //TODO: make this a proper query instead of hard coded
        Query q = em.createQuery(String.format("SELECT c FROM Creature c WHERE c.id=%d", id ));
        Creature resultList = (Creature) q.getSingleResult();
        return resultList;
    }

    public void updateCreature(Creature c){
        em.persist(c);
    }



    @Inject
    private Creature creature;


    //Method used for storing a new creature for the user
    public RestResponse storeCreature(String user, String creatureName) {

        //TODO: return created object
        RestResponse<String> restResponse = new RestResponse<>();
        try {
            if (creatureName == null) {
                throw new NullPointerException("fields cannot be null");
            }
            Creature creature = new Creature();
            creature.setName(creatureName);
            creature.setOwner(user);
            em.persist(creature);
            restResponse.setStatus(201);
            restResponse.setData("persisted");
            LOGGER.info(String.format("Creature persisted for player [%s]", user));

        } catch (Exception e) {
            restResponse.setStatus(400);
            restResponse.setData("failed to persist creature." + e.getMessage());
            LOGGER.error(String.format("Failed to persist creature for player [%s] \n ERROR: %s", e.getMessage()));
        }
        return restResponse;
    }
}

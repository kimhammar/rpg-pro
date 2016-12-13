package creatureService.ejb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import creatureService.entities.Creature;
import shared.response.ResponseHelper;

import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by kimha on 11/24/16.
 */

@Stateless
@Startup
public class CharacterDbEjb {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterDbEjb.class);


    @PersistenceContext(unitName = "rpgchar")
    private EntityManager em;

    @Inject
    ResponseHelper responseHelper;


    //Get all creatures for a certain user
    public void getCreatures(String userName) {
        LOGGER.info(String.format("Reading creatures for player [%s] from database", userName));
        List<Creature> creatures = em.createNamedQuery("Creature.getAll").setParameter("owner", userName).getResultList();
        responseHelper.putItem("creatures", creatures);
    }


    //Load one specific creature for user
    public Creature getCreature(int id) {
        Creature creature = (Creature) em.createNamedQuery("Creature.getOne").setParameter("id", id).getSingleResult();
        return creature;
    }


    //Method used for storing a new creature for the user
    public void storeCreature(Creature creature) {

        em.persist(creature);
        responseHelper.setStatus(Response.Status.CREATED.getStatusCode());
        responseHelper.putItem("msg", String.format("Your creature [%s] was persisted", creature.getName()));
        LOGGER.info(String.format("New creature [%s] persisted for player [%s]", creature.getName(), creature.getOwner()));
    }


    //Method for updating a creature
    public void updateCreature(Creature c) {
        em.createNamedQuery("Creature.update").setParameter("id", c.getId())
                .setParameter("inGame", c.isInGame()).executeUpdate();
        LOGGER.info(String.format("Creature with id [%d] updated", c.getId()));
    }


    //Delete an entity
    public void deleteCreature(Integer id) {
        LOGGER.info(String.format("Trying to delete character "));
        int i = em.createNamedQuery("Creature.delete").setParameter("id", id).executeUpdate();
        if (i > 0) {
            responseHelper.putItem("msg", "creature deleted");
            LOGGER.info(String.format("Creature with id [%d] deleted", id));
        } else {
            responseHelper.putItem("msg", "no such creature");
            responseHelper.setStatus(Response.Status.NO_CONTENT.getStatusCode());
            LOGGER.info(String.format("No creature with [%d] or no permission", id));
        }
    }
}

package gameService.ejb;

import gameService.entities.GameResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.response.ResponseHelper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by kimha on 12/11/16.
 */
@Stateless
public class GameDbEjb {

    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);


    @PersistenceContext(unitName = "rpgchar")
    private EntityManager em;

    @Inject
    ResponseHelper responseHelper;


    //Method used for storing a new creature for the user
    public void storeGamestats(GameResults gameResults) {
        em.persist(gameResults);
        LOGGER.info("Game results persisted");
    }

    //Todo: methods for reading stats


}

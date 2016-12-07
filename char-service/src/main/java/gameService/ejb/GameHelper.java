package gameService.ejb;

import shared.ejb.CharacterDbEjb;
import shared.entities.Creature;
import shared.exceptions.FailedToJoinGameException;
import shared.entities.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by kimha on 12/3/16.
 */
@Stateless
public class GameHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHelper.class);

    @Inject
    GameEJB gameEJB;

    @Inject
    CharacterDbEjb characterDbEjb;

    @Inject
    Creature creature;

    @Inject
    GameSession gameSession;

    public RestResponse<String> joinGame(@Context SecurityContext sc, int id) throws FailedToJoinGameException {

        //Check player didn't load any character
        if (gameSession.getCurrentCreature() == null) {
            LOGGER.info("Character loading...");
            creature = characterDbEjb.getCreature(id);
            //Check player owns requeted creature

            gameSession.setCurrentCreature(creature);
        }


        if (!creature.getOwner().equals(sc.getUserPrincipal().getName())) {
            throw new FailedToJoinGameException("Owner did not match request");
        }




        creature = gameSession.getCurrentCreature();


        //Check character isn't ingame
        if (creature.isInGame() == false) {
            creature.setInGame(true);
//            characterDbEjb.updateCreature(creature);
            gameEJB.addCreature(creature);
            LOGGER.info("Character joined game");
        } else {
            LOGGER.warn("Character already in use!");
        }


        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setStatus(200);
        restResponse.setData(creature.toString());
        return restResponse;
    }

//    public RestResponse<String> moveCharacter(SecurityContext sc, Direction direction) {
//        gameEJB.move(sc, direction);
//
//    }

}

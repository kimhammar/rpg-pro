package gameService.ejb;

import gameService.exceptions.MapOutOfBoundsExeption;
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
            creature = characterDbEjb.getCreature(id);
            LOGGER.info(String.format("Creature [%s] for player[%s] loaded" , creature.getName(), creature.getOwner()));
            //Check player owns requeted creature

            gameSession.setCurrentCreature(creature);
        }


        if (!creature.getOwner().equals(sc.getUserPrincipal().getName())) {
            throw new FailedToJoinGameException("Owner did not match request");
        }




        creature = gameSession.getCurrentCreature();


        //Check character isn't ingame
        if (creature.isAlive() == false) {
            creature.setAlive(true);
//            characterDbEjb.updateCreature(creature);
            gameEJB.addCreature(creature);
            LOGGER.info(String.format("Creature [%s] joined game", creature.getName()));
        } else {
            LOGGER.warn(String.format("Creature [%s] already in use!",creature.getName()));
        }


        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setStatus(200);
        restResponse.setData(creature.toString());
        return restResponse;
    }



    public RestResponse<GameSession> requestMove(String direction){
        RestResponse<GameSession> restResponse = new RestResponse<>();
        if(gameSession.getCurrentCreature() == null){
            restResponse.setStatus(420);
            return restResponse;
        }
        creature = gameSession.getCurrentCreature();
        switch (direction) {
            case "N":
            case "S":
            case "E":
            case "W":
                try {
                    LOGGER.info(String.format("Trying move creature [%s]", creature.getName()));
                    String msg = gameEJB.move(creature, direction);
                    gameSession.setLastActionMessage(msg);
                } catch (MapOutOfBoundsExeption mapOutOfBoundsExeption) {
                    restResponse.setStatus(420);
                    gameSession.setLastActionMessage(mapOutOfBoundsExeption.getMessage());
                    //Log invalid move player and stacktrace
                    LOGGER.warn("Map out of bounds");
                }
                //If everything ok
                restResponse.setStatus(200);
                break;
            default:
                //In case of invalid request
                restResponse.setStatus(400);
                break;
        }
        restResponse.setData(gameSession);
        return restResponse;
    }

//    public RestResponse<String> moveCharacter(SecurityContext sc, Direction direction) {
//        gameEJB.move(sc, direction);
//
//    }

}

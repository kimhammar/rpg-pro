package gameService.ejb;

import gameService.exceptions.MapOutOfBoundsExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import creatureService.ejb.CharacterDbEjb;
import creatureService.entities.Creature;
import shared.response.ResponseHelper;
import gameService.exceptions.FailedToJoinGameException;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by kimha on 12/3/16.
 */
@Stateless
public class GameHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHelper.class);

    @Inject
    GameInterface game;

    @Inject
    CharacterDbEjb characterDbEjb;

    @Inject
    Creature creature;

    @Inject
    GameSession gameSession;

    @Inject
    ResponseHelper responseHelper;

    //Tries to add a creature to the game
    public void joinGame(String userName, int id) throws FailedToJoinGameException {
        //Check player didn't load any character
        if (gameSession.getCurrentCreature() == null) {
            creature = characterDbEjb.getCreature(id);
            LOGGER.info(String.format("Creature [%s] for player[%s] loaded", creature.getName(), creature.getOwner()));
            //Check player owns requeted creature
            gameSession.setCurrentCreature(creature);
        }


        //Check correct owner
        if (!creature.getOwner().equals(userName)) {
            LOGGER.warn(String.format("User [%s] tried to use creature [%s]", userName, creature.getName()));
            throw new FailedToJoinGameException("Owner did not match request");
        }
        creature = gameSession.getCurrentCreature();


        //Check character isn't ingame
        if (creature.isInGame() == false) {
            creature.setInGame(true);
            //update in database
            characterDbEjb.updateCreature(creature);
            game.addCreature(creature);
            LOGGER.info(String.format("Creature [%s] joined game", creature.getName()));
        } else {
            LOGGER.warn(String.format("Creature [%s] already in use!", creature.getName()));
            responseHelper.putItem("msg", "You are already in a game!");
            return;
        }

        //Add information
        responseHelper.putItem("msg", String.format("Your character has entered the arena! Make a move!", creature.getName()));
        responseHelper.putItem("position", String.format("[X:%d, Y:%d]", creature.getxPos(), creature.getyPos()));
    }


    //Try to move a creature
    public void requestMove(String direction) throws MapOutOfBoundsExeption {
        if (gameSession.getCurrentCreature() == null) {
            responseHelper.putItem("msg", "you must chose a creature");
            LOGGER.info(String.format("Creature not chosen yet"));
            responseHelper.setStatus(420);
            return;
        }

        creature = gameSession.getCurrentCreature();
        if(creature.isInGame() == false){
            responseHelper.putItem("msg", "You have been killed :( time to join agian!");
            LOGGER.info(String.format("Slayed creature [%s] tried to join",creature.getName()));
            responseHelper.setStatus(420);
            return;
        }

        //Check correct input
        switch (direction) {
            case "N":
            case "S":
            case "E":
            case "W":
                    LOGGER.info(String.format("Trying move creature [%s]", creature.getName()));
                    game.move(creature, direction);
                    //Probably should put this catch closer to surface

                break;
            default:
                //In case of invalid request
                LOGGER.info(String.format("Invalid move requested from player [%s]", gameSession.getCurrentCreature().getOwner()));
                responseHelper.putItem("msg", "that's not a valid direction,choose N S W E");
                responseHelper.setStatus(400);
                break;
        }
        //Check if player still in game, else make available for play
        //TODO: put this at better place
        if (!creature.isInGame()) {
            LOGGER.info(String.format("Updating [inGame] for creature [%s]",creature.getName()));
            characterDbEjb.updateCreature(creature);
        }
    }

}

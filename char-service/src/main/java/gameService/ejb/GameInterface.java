package gameService.ejb;

import gameService.exceptions.MapOutOfBoundsExeption;
import creatureService.entities.Creature;

import java.util.Map;

/**
 * Created by kimha on 12/12/16.
 */
public interface GameInterface {
    void move(Creature creature, String direction) throws MapOutOfBoundsExeption;
    void addCreature(Creature creature);
    Map<String,String> getMap();
}

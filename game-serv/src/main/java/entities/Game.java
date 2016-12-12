package entities;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by kimha on 11/27/16.
 * This class represents a game instance
 */
@Entity
public class Game {
    private List<creatureService.entities.Creature> creatureList;
    private TreasureMap map;

    public void placeCreature(Integer x, Integer y) {

    }



}

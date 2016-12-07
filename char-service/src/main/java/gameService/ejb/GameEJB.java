package gameService.ejb;

import shared.constants.Direction;
import shared.entities.Creature;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.*;

/**
 * Created by kimha on 12/1/16.
 */
@Singleton
@Startup
public class GameEJB {

    private Random random = new Random();
    private int width = 4;
    private int height = 3;
    private Creature[][] map;
    private Map<Integer,Creature> inGameCreatures = new HashMap<>();




    @PostConstruct
    public void createNewMap() {
        map = new Creature[height][width];
        spawnTroll();
    }


    public void spawnTroll(){
        Creature creature = new Creature();
        creature.setName("Troll");
        creature.setxPos( random.nextInt(width));
        creature.setyPos( random.nextInt(height));
    }




    public void move(Integer chreatureId, Direction direction){
        if(Direction.NORTH == direction){

        }
    }


    //TODO: return already joined message
    public void addCreature(Creature c) {
        if (!inGameCreatures.entrySet().contains(c)) {
            c.setxPos(random.nextInt(width));
            c.setyPos(random.nextInt(height));
            inGameCreatures.put(c.getId(),c);
        }
//        if (map[yPos][xPos] != null) {
//            System.out.println("DEAD");
//        }
//        System.out.println(c.getName() + " added to the game!");
//        System.out.println(c.getCreaturePosition());
//        map[yPos][xPos] = c;
    }


}

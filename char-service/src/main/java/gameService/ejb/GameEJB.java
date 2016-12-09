package gameService.ejb;

import gameService.entities.GameStats;
import gameService.exceptions.MapOutOfBoundsExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.entities.Creature;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

/**
 * Created by kimha on 12/1/16.
 */
@ApplicationScoped
public class GameEJB {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameEJB.class);
    private Random random = new Random();
    private int width = 4;
    private int height = 3;
    private Creature[][] map;
    private Map<Integer, Creature> inGameCreatures = new HashMap<>();
    private GameStats gameStats = new GameStats();
    private int playersLeft;
    private int gameId;


    @Inject
    GameSession gameSession;


    //Start a new game instance
    @PostConstruct
    public void startNewGame() {
        playersLeft = 0;
        gameId++;
        //TODO: add id
        LOGGER.info(String.format("New battle has begun! Id:%d", gameId));
        map = new Creature[height][width];
        spawnTroll();
    }


    //Spawn a bot troll
    public void spawnTroll() {
        Creature creature = new Creature();
        creature.setName("Troll");
        spawnCreatureAtRandomLocation(creature);
        updatePlayersNumber();
        playersLeft++;
    }




    //Add a new player creature to the game
    //TODO: return already joined message
    public void addCreature(Creature c) {
        //Check so creature not already in game
        if (!inGameCreatures.entrySet().contains(c)) {
            spawnCreatureAtRandomLocation(c);
            inGameCreatures.put(c.getId(), c);
            updatePlayersNumber();
            playersLeft++;
        }
    }



    //Player requesting to move
    public String move(Creature creature, String direction) throws MapOutOfBoundsExeption {
        int predictedX = creature.getxPos();
        int predictedY = creature.getyPos();
        StringBuilder returnMessage = new StringBuilder();


        LOGGER.info(String.format("Creature [%s] trying to move [%s]", creature.getName(), direction));

        switch (direction) {
            case "N":
                predictedY--;
                break;
            case "S":
                predictedY++;
                break;
            case "W":
                predictedX--;
                break;
            case "E":
                predictedX++;
                break;
        }

        //Check move is allowed
        if (predictedX < 0 || predictedX > width - 1 || predictedY < 0 || predictedY > height - 1) {
            LOGGER.info(String.format("Creature  [%s] tried to move outside map boundaries", creature.getName()));
            throw new MapOutOfBoundsExeption(String.format("Requested move is out of map boundaries Y: %d X %d", predictedY, predictedX));
        }

        //Check if stabbed someone
        //Todo:make some funny random weapon
        if (map[predictedY][predictedX] != null) {
            LOGGER.info(String.format("Player [%s]s creature [%s] killed [%s]", creature.getOwner(), creature.getName(), map[predictedY][predictedX].getName()));
            returnMessage.append(String.format("You killed player [%s] with a [%s]", map[predictedY][predictedX].getName(),
                    " rock"));
            map[predictedY][predictedX].setAlive(false);
            playersLeft--;
        }
        LOGGER.info(String.format("Creature [%s] moved [%s] to [X:%d Y:%d]", creature.getName(), direction, predictedX, predictedY));
        map[creature.getyPos()][creature.getxPos()] = null;
        map[predictedY][predictedX] = creature;
        creature.setyPos(predictedY);
        creature.setxPos(predictedX);
        logGameStatus();
        if (checkIfWinner()){
            LOGGER.info(String.format("Player [%s] with creature [%s] has won the game!", creature.getOwner(), creature.getName()));
            LOGGER.info(String.format("Starting new game!"));
            gameStats.setWinner(creature.getOwner());
            gameSession.setCurrentCreature(null);
            //TODO:Persist gamestats in database
            creature.setAlive(false);
            startNewGame();
        }

        return returnMessage.toString();

    }

    //Check if there is a winner
    private boolean checkIfWinner() {
        if(playersLeft == 1){
            return true;
        }
        return false;
    }


    //Log the status of the game
    private void logGameStatus(){
        LOGGER.info(String.format("%d player%s left", playersLeft, playersLeft < 1 ? "" : "s"));
    }



    //Update total number of players who entered the game
    private void updatePlayersNumber(){
        gameStats.setPlayers(gameStats.getPlayers() + 1);
    }


    //Randomize a creatures location and spawn it
    private void spawnCreatureAtRandomLocation(Creature creature) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        if (map[y][x] == null) {
            creature.setxPos(x);
            creature.setyPos(y);
            LOGGER.info(String.format("Creature [%s] spawned at [X:%d Y:%d]", creature.getName(),
                    creature.getxPos(), creature.getyPos()));
            map[y][x] = creature;

        } else {
            LOGGER.info(String.format("Location not empty.. Trying to respawn creature [%s]", creature.getName()));
            spawnCreatureAtRandomLocation(creature);
        }
    }

}

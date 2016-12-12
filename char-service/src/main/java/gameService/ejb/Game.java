package gameService.ejb;

import gameService.entities.GameResults;
import gameService.exceptions.MapOutOfBoundsExeption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import creatureService.entities.Creature;
import shared.response.ResponseHelper;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.*;

import static javax.ejb.ConcurrencyManagementType.BEAN;

/**
 * Created by kimha on 12/1/16.
 */
@ConcurrencyManagement(BEAN)
@Singleton
@Startup
public class Game implements GameInterface{

    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private Random random = new Random();
    private int width = 4;
    private int height = 3;
    private Creature[][] map;
    private GameResults gameResults = new GameResults();
    private int playersLeft;
    private int gameId;

    @Inject
    GameSession gameSession;

    @Inject
    GameDbEjb gameDbEjb;

    @Inject
    ResponseHelper responseHelper;


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


    //Spawn a bot troll at a specified interval
    @Schedule( minute = "*/5", hour = "*")
//    @Schedule(second = "*", minute = "*/5", hour = "*")
    public void spawnTroll() {
        if(playersLeft >= (height * width)-1){
            LOGGER.info(String.format("Map is full. [%d]/[%d] players in game, can't spawn troll",
                    playersLeft, (width * height)));
            return;
        }
        Creature troll = new Creature();
        troll.setName("Troll" + +random.nextInt(1000));
        addCreature(troll);
    }


    //Add a new player creature to the game
    //TODO: return already joined message
    @Override
    public void addCreature(Creature c) {
        spawnCreatureAtRandomLocation(c);
        updatePlayersNumber();
        playersLeft++;
    }


    //Player requesting to move
    //TODO: break up this method
    @Override
    public void move(Creature creature, String direction) throws MapOutOfBoundsExeption {
        List<String> eventList = new ArrayList<>();

        int predictedX = creature.getxPos();
        int predictedY = creature.getyPos();

        responseHelper.putItem("position", String.format("[X:%d][Y:%d]", creature.getxPos(), creature.getyPos()));
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
            eventList.add(String.format("You tried to move outside the map, cannot move [%s]", direction));
            responseHelper.putItem("events", eventList);
            throw new MapOutOfBoundsExeption(String.format("Creature [%s] tried to move out of map boundaries Y: %d X: %d"
                    ,creature.getName(), predictedY, predictedX));
        }

        //Check if stabbed someone
        //Todo:make some funny random weapon
        if (map[predictedY][predictedX] != null) {
            LOGGER.info(String.format("Player [%s]s creature [%s] killed [%s]", creature.getOwner(), creature.getName(), map[predictedY][predictedX].getName()));
            eventList.add(String.format("You killed player [%s] with a [%s]", map[predictedY][predictedX].getName(),
                    " rock"));

            map[predictedY][predictedX].setInGame(false);
            playersLeft--;
            creature.addKill();
        }
        eventList.add(String.format("Your creature [%s] moved [%s] to [X:%d Y:%d]", creature.getName(), direction, predictedX, predictedY));
        LOGGER.info(String.format("Creature [%s] moved [%s] to [X:%d Y:%d]", creature.getName(), direction, predictedX, predictedY));
        map[creature.getyPos()][creature.getxPos()] = null;
        map[predictedY][predictedX] = creature;
        creature.setyPos(predictedY);
        creature.setxPos(predictedX);
        responseHelper.putItem("position", String.format("[X:%d][Y:%d]", predictedX, predictedY));

        logGameStatus();
        if (checkIfWinner()) {
            eventList.add("you have won the game");
            LOGGER.info(String.format("Player [%s] with creature [%s] has won the game!", creature.getOwner(), creature.getName()));
            LOGGER.info(String.format("Starting new game!"));
            gameResults.setWinnerId(creature.getId());


            gameSession.setCurrentCreature(null);
            storeGame();
            creature.setInGame(false);
            creature.setKillCount(0);

            startNewGame();
        }
        responseHelper.putItem("kills", creature.getKillCount());
        responseHelper.putItem("events", eventList);
    }


    //TODO: look into this if possible generalize it
    private boolean checkIfCollision(int x, int y, Creature creature) {
        if (map[y][x] != null) {
            LOGGER.info(String.format("Player [%s]s creature [%s] killed [%s]", creature.getOwner(), creature.getName(), map[y][x].getName()));
            map[y][x].setInGame(false);
            playersLeft--;
            return true;
        } else {
            return false;
        }
    }


    //Save the status of the game
    private void storeGame() {
        LOGGER.info(String.format("Persisting and resettings game results"));
        gameDbEjb.storeGamestats(gameResults);
        gameResults = new GameResults();
    }

    //Check if there is a winner
    private boolean checkIfWinner() {
        if (playersLeft == 1) {
            return true;
        }
        return false;
    }


    //Log the status of the game
    private void logGameStatus() {
        String status = String.format("%d player%s left", playersLeft, playersLeft < 1 ? "" : "s");
        responseHelper.putItem("status", status);
        LOGGER.info(status);
    }


    //Update total number of players who entered the game
    private void updatePlayersNumber() {
        gameResults.setPlayers(gameResults.getPlayers() + 1);
    }


    //TODO: check spawnkill
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

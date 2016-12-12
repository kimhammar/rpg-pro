package gameService.ejb;

import creatureService.entities.Creature;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * Created by kimha on 12/7/16.
 */
@SessionScoped
public class GameSession implements Serializable {

    private Creature currentCreature;

    private int gamesWon;

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public Creature getCurrentCreature() {
        return currentCreature;
    }

    public void setCurrentCreature(Creature currentCreature) {
        this.currentCreature = currentCreature;
    }

    public GameSession() {
    }


}

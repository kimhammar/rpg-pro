package gameService.ejb;

import shared.entities.Creature;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * Created by kimha on 12/7/16.
 */
@SessionScoped
public class GameSession implements Serializable{






    private Creature currentCreature;

    public Creature getCurrentCreature() {
        return currentCreature;
    }

    public void setCurrentCreature(Creature currentCreature) {
        this.currentCreature = currentCreature;
    }

    public GameSession() {
    }

    private String lastActionMessage;

    public String getLastActionMessage() {
        return lastActionMessage;
    }

    public void setLastActionMessage(String lastActionMessage) {
        this.lastActionMessage = lastActionMessage;
    }
}

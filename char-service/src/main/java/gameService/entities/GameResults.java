package gameService.entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by kimha on 12/9/16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "GameStats.getAll", query = "SELECT c FROM GameResults c"),
//        @NamedQuery(name = "GameResults.getAll", query = "SELECT c FROM Creature c"),
})
public class GameResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int winnerId;

    @Min(2)
    private int players;

    public GameResults() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }
}


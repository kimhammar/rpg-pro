package creatureService.entities;

import gameService.ejb.Game;
import gameService.entities.GameResults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kimha on 11/25/16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Creature.getAll", query = "SELECT c FROM Creature c " +
                "WHERE c.owner = :owner"),
        @NamedQuery(name = "Creature.getOne", query = "SELECT c FROM Creature c " +
                "WHERE c.id = :id"),
        @NamedQuery(name = "Creature.delete", query = "DELETE FROM Creature c WHERE c.id = :id"),
        @NamedQuery(name = "Creature.update", query = "UPDATE Creature c SET c.inGame = :inGame " +
                "WHERE c.id = :id")
})
public class Creature implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]*$")
    private String name;

    @NotNull
    private String owner;

    @Transient
    private Integer xPos;

    @Transient
    private Integer yPos;

    @Transient
    private int killCount;

    private List<GameResults> gameResults;

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }


    private boolean inGame;


    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public Creature() {
    }

    public Integer getxPos() {
        return xPos;
    }

    public void setxPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    public void setyPos(Integer yPos) {
        this.yPos = yPos;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Creature{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                ", isAlive=" + inGame +
                '}';
    }

    public void addKill() {
        killCount++;
    }
}

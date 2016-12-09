package shared.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by kimha on 11/25/16.
 */
@Entity
public class Creature implements Serializable{


    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String owner;

    private Integer xPos;
    private Integer yPos;

    private boolean alive;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean inGame) {
        this.alive = inGame;
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
                ", isAlive=" + alive +
                '}';
    }
}

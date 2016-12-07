package entities;

/**
 * Created by kimha on 12/1/16.
 */
public class Creature implements Killable{
    private String Name;
    private boolean dead;

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public CreaturePosition getCreaturePosition() {
        return creaturePosition;
    }

    public void setCreaturePosition(CreaturePosition creaturePosition) {
        this.creaturePosition = creaturePosition;
    }

    private CreaturePosition creaturePosition;


    public Creature() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isAlive() {
        return dead;
    }

    public void setAlive(boolean alive) {
        this.dead = alive;
    }


    @Override
    public void die() {
        System.out.println("Ohhh noooo");
        dead = true;
    }
}

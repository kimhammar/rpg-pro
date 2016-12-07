package entities;

/**
 * Created by kimha on 12/1/16.
 */
public class CreaturePosition {
    private int xPos;
    private int yPos;

    public CreaturePosition() {
    }

    public CreaturePosition(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    @Override
    public String toString(){
        return "Your position is: x:" + xPos + " y:" + yPos;
    }

}

package http;

import java.io.Serializable;

/**
 * Created by kimha on 12/3/16.
 */
public class MoveRequest implements Serializable{
    private String userName;
    private String move;


    public MoveRequest() {
    }

    public MoveRequest(String userName, String move) {
        this.userName = userName;
        this.move = move;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}

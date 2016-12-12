package gameService.exceptions;

/**
 * Created by kimha on 12/8/16.
 * This exeption should be thrown when a player tries to move outside the map
 */
public class MapOutOfBoundsExeption extends Exception {
    public MapOutOfBoundsExeption(String msg) {
        super(msg);
    }
}

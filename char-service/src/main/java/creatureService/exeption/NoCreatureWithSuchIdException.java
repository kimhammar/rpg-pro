package creatureService.exeption;

/**
 * Created by kimha on 12/11/16.
 */
public class NoCreatureWithSuchIdException extends Exception {
    public NoCreatureWithSuchIdException(String msg) {
        super(msg);
    }
}

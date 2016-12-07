package shared.exceptions;

/**
 * Created by kimha on 12/6/16.
 */
public class FailedToJoinGameException extends Throwable {
    public FailedToJoinGameException(String msg) {
        super(msg);
    }
}

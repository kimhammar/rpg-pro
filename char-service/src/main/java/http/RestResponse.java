package http;

import java.io.Serializable;

/**
 * Created by kimha on 11/25/16.
 */
public class RestResponse<T> implements Serializable{
    private int status;
    private T data;

    public RestResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

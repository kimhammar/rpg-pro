package shared.entities;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by kimha on 11/25/16.
 */
public class RestResponse<T> implements Serializable{
    private int status;
    private T data;
    private String transactionId;

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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}

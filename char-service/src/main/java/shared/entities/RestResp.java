package shared.entities;

import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kimha on 11/25/16.
 */

@RequestScoped
public class RestResp{
    private int status;
    private Map<String,Object> data;
    private String transactionId;

    public RestResp() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String,Object> getData() {
        return data;
    }

    public void setData(Map<String,Object> data) {
        this.data = data;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}

package shared.response;

import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kimha on 11/25/16.
 */

@RequestScoped
public class ResponseHelper implements Serializable {
    private Map<String, Object> data = new HashMap<>();
    private int status = 200;


    public ResponseHelper() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void putItem(String key, Object object) {
        data.put(key, object);
    }

}
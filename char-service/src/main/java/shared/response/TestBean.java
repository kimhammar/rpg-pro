package shared.response;

import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kimha on 12/13/16.
 */
@Stateless
public class TestBean {
    private Map<String, Object> data = new HashMap<>();
    private int status = 200;


    public TestBean() {
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

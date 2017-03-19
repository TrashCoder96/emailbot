package ro;

import java.util.Map;
import java.util.UUID;

/**
 * Created by ivan on 18.03.17.
 */
public class BotRo {

    private String id = UUID.randomUUID().toString();

    private String name;

    private String type;

    private Map<String, String> params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

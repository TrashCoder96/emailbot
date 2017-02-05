package controller;

import org.hibernate.validator.constraints.NotBlank;

import java.util.HashMap;

/**
 * Created by asus-pc on 04.02.2017.
 */
public class ParamsRo {

    @NotBlank
    private String id;

    private HashMap<String, String> params;

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

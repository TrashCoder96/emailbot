package controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by asus-pc on 05.02.2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResRoot {

    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

}

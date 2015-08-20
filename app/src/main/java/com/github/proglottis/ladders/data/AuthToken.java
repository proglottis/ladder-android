package com.github.proglottis.ladders.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 20/08/15.
 */
public class AuthToken {
    private String token;

    public static AuthToken fromJSON(JSONObject obj) throws JSONException {
        AuthToken token = new AuthToken();
        token.token = obj.getString("token");
        return token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

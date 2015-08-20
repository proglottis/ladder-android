package com.github.proglottis.ladders.data;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 20/08/15.
 */
public class Token {
    private long iat;
    private long exp;
    private String userId;
    private String userName;

    public static Token decode(String s) throws JSONException {
        s = s.substring(s.indexOf('.'), s.lastIndexOf('.'));
        JSONObject obj = new JSONObject(new String(Base64.decode(s, 0)));
        return fromJSON(obj);
    }

    public static Token fromJSON(JSONObject obj) throws JSONException {
        Token t = new Token();
        t.iat = obj.getLong("iat");
        t.exp = obj.getLong("exp");
        t.userId = obj.getString("user_id");
        t.userName = obj.getString("user_name");
        return t;
    }

    public long getIat() {
        return iat;
    }

    public long getExp() {
        return exp;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}

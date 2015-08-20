package com.github.proglottis.ladders.data;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 20/08/15.
 */
public class Token {
    private long sub;
    private long iat;
    private long exp;
    private String name;

    public static Token decode(String s) throws JSONException {
        s = s.substring(s.indexOf('.'), s.lastIndexOf('.'));
        JSONObject obj = new JSONObject(new String(Base64.decode(s, 0)));
        return fromJSON(obj);
    }

    public static Token fromJSON(JSONObject obj) throws JSONException {
        Token t = new Token();
        t.sub = obj.getLong("sub");
        t.iat = obj.getLong("iat");
        t.exp = obj.getLong("exp");
        t.name = obj.getString("name");
        return t;
    }

    public String getId() {
        return String.valueOf(sub);
    }

    public long getSub() {
        return sub;
    }

    public long getIat() {
        return iat;
    }

    public long getExp() {
        return exp;
    }

    public String getName() {
        return name;
    }
}

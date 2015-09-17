package com.github.proglottis.ladders.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by steventan on 17/09/15.
 */
public class PushNotificationKey {

    private String gcm;

    public PushNotificationKey(String gcm) {
        this.gcm = gcm;
    }

    public static PushNotificationKey fromJSON(JSONObject json) throws JSONException {
        String gcm = json.getString("gcm");
        return new PushNotificationKey(gcm);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("gcm", gcm);
        return obj;
    }
}

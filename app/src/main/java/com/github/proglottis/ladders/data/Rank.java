package com.github.proglottis.ladders.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 21/08/15.
 */
public class Rank {
    private String playerId;
    private int position;

    public Rank() {
    }

    public Rank(String playerId, int position) {
        this.playerId = playerId;
        this.position = position;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("player_id", playerId);
        obj.put("position", position);
        return obj;
    }
}

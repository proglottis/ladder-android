package com.github.proglottis.ladders.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 21/08/15.
 */
public class Rank {
    private String id;
    private int position;
    private String playerId;
    private Player player;

    public Rank() {
    }

    public Rank(String playerId, int position) {
        this.playerId = playerId;
        this.position = position;
    }

    public static Rank fromJSON(JSONObject obj) throws JSONException {
        Rank r = new Rank();
        r.id = obj.getString("id");
        r.position = obj.getInt("position");
        if (obj.has("player_id")) {
            r.playerId = obj.getString("player_id");
        }
        r.player = Player.fromJSON(obj.getJSONObject("player"));
        return r;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("player_id", playerId);
        obj.put("position", position);
        return obj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

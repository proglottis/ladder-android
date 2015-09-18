package com.github.proglottis.ladders.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by james on 21/08/15.
 */
public class Rank {
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private String id;
    private int position;
    private String playerId;
    private Player player;
    private Date confirmedAt;

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
        String rawConfirmedAt = obj.getString("confirmed_at");
        if (rawConfirmedAt != null) {
            try {
                r.confirmedAt = DATE_FORMAT.parse(rawConfirmedAt);
            } catch (ParseException e) {
                r.confirmedAt = null;
            }
        }
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

    public Date getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(Date confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public boolean isConfirmed() {
        return confirmedAt != null;
    }
}

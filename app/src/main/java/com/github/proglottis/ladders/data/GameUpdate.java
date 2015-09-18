package com.github.proglottis.ladders.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 18/09/15.
 */
public class GameUpdate {
    private String gameId;
    private boolean isConfirmation;

    public GameUpdate() {
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public boolean isConfirmation() {
        return isConfirmation;
    }

    public void setIsConfirmation(boolean isConfirmation) {
        this.isConfirmation = isConfirmation;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", gameId);
        if (isConfirmation()) {
            obj.put("confirm", 1);
        }
        return obj;
    }
}

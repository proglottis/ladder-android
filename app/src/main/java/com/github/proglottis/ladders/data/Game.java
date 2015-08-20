package com.github.proglottis.ladders.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 21/08/15.
 */
public class Game {
    private String tournamentId;
    private Rank[] ranks;

    public Game() {
    }

    public Game(String tournamentId, Rank[] ranks) {
        this.tournamentId = tournamentId;
        this.ranks = ranks;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("tournament_id", tournamentId);
        JSONArray rankObjs = new JSONArray();
        for (Rank rank : ranks) {
            rankObjs.put(rank.toJSON());
        }
        obj.put("game_ranks_attributes", rankObjs);
        return obj;
    }
}

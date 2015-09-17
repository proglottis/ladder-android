package com.github.proglottis.ladders.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 21/08/15.
 */
public class Game {
    private String id;
    private String tournamentId;
    private Tournament tournament;
    private User owner;
    private Rank[] ranks;

    public Game() {
    }

    public Game(String tournamentId, Rank[] ranks) {
        this.tournamentId = tournamentId;
        this.ranks = ranks;
    }

    public static Game fromJSON(JSONObject obj) throws JSONException {
        Game g = new Game();
        g.id = obj.getString("id");
        if (obj.has("tournament_id")) {
            g.tournamentId = obj.getString("tournament_id");
        }
        g.tournament = Tournament.fromJSON(obj.getJSONObject("tournament"));
        g.owner = User.fromJSON(obj.getJSONObject("owner"));

        JSONArray ranks = obj.getJSONArray("game_ranks");
        g.ranks = new Rank[ranks.length()];
        for(int i = 0; i < ranks.length(); i++) {
            g.ranks[i] = Rank.fromJSON(ranks.getJSONObject(i));
        }
        return g;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Rank[] getRanks() {
        return ranks;
    }

    public void setRanks(Rank[] ranks) {
        this.ranks = ranks;
    }

    public String title() {
        String s = "";
        for (Rank rank : ranks) {
            if (!s.isEmpty()) {
                s += " vs ";
            }
            s += rank.getPlayer().getUser().getName();
        }
        return s;
    }
}

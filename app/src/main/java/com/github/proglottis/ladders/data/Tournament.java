package com.github.proglottis.ladders.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 19/08/15.
 */
public class Tournament implements Parcelable {
    private String id;
    private String name;
    private Boolean isPublic;
    private String rankingType;
    private String createdAt;
    private Player[] players;

    public Tournament() {}

    protected Tournament(Parcel in) {
        id = in.readString();
        name = in.readString();
        isPublic = in.readInt() != 0;
        rankingType = in.readString();
        createdAt = in.readString();
        int playersLength = in.readInt();
        players = new Player[playersLength];
        in.readTypedArray(players, Player.CREATOR);
    }

    public static final Creator<Tournament> CREATOR = new Creator<Tournament>() {
        @Override
        public Tournament createFromParcel(Parcel in) {
            return new Tournament(in);
        }

        @Override
        public Tournament[] newArray(int size) {
            return new Tournament[size];
        }
    };

    public static Tournament fromJSON(JSONObject obj) throws JSONException {
        Tournament t = new Tournament();
        t.id = obj.getString("id");
        t.name = obj.getString("name");
        t.isPublic = obj.getBoolean("public");
        t.rankingType = obj.getString("ranking_type");
        t.createdAt = obj.getString("created_at");
        if (obj.has("players")) {
            JSONArray players = obj.getJSONArray("players");
            t.players = new Player[players.length()];
            for (int i = 0; i < players.length(); i++) {
                t.players[i] = Player.fromJSON(players.getJSONObject(i));
            }
        }
        return t;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeInt(isPublic ? 1 : 0);
        parcel.writeString(rankingType);
        parcel.writeString(createdAt);
        parcel.writeInt(players.length);
        parcel.writeTypedArray(players, i);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getRankingType() {
        return rankingType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setRankingType(String rankingType) {
        this.rankingType = rankingType;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
}

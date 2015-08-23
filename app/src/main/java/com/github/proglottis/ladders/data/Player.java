package com.github.proglottis.ladders.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by james on 19/08/15.
 */
public class Player implements Parcelable {

    private String id;
    private Integer position;
    private Integer losingStreakCount;
    private Integer winningStreakCount;
    private User user;

    public Player() {}

    protected Player(Parcel in) {
        id = in.readString();
        int p = in.readInt();
        position = p == -1 ? null : p;
        losingStreakCount = in.readInt();
        winningStreakCount = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Comparator<Player> BY_NAME = new Comparator<Player>() {
        @Override
        public int compare(Player lhs, Player rhs) {
            return lhs.getUser().getName().compareTo(rhs.getUser().getName());
        }
    };

    public static final Comparator<Player> BY_POSITION = new Comparator<Player>() {
        @Override
        public int compare(Player p1, Player p2) {
            Integer pos1 = p1.getPosition();
            Integer pos2 = p2.getPosition();
            if(pos1 == null) {
                pos1 = Integer.MAX_VALUE;
            }
            if(pos2 == null) {
                pos2 = Integer.MAX_VALUE;
            }
            return pos1.compareTo(pos2);
        }
    };

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public static Player fromJSON(JSONObject obj) throws JSONException {
        Player player = new Player();
        player.id = obj.getString("id");
        if(!obj.isNull("position")) {
            player.position = obj.getInt("position");
        }
        player.losingStreakCount = obj.getInt("losing_streak_count");
        player.winningStreakCount = obj.getInt("winning_streak_count");
        player.user = User.fromJSON(obj.getJSONObject("user"));
        return player;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeInt(position == null ? -1 : position);
        parcel.writeInt(losingStreakCount);
        parcel.writeInt(winningStreakCount);
        parcel.writeParcelable(user, i);
    }

    public String getId() {
        return id;
    }

    public Integer getPosition() {
        return position;
    }

    public Integer getLosingStreakCount() {
        return losingStreakCount;
    }

    public Integer getWinningStreakCount() {
        return winningStreakCount;
    }

    public User getUser() {
        return user;
    }
}

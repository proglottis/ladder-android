package com.github.proglottis.ladders.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james on 19/08/15.
 */
public class User implements Parcelable {
    private String id;
    private String imageURL;
    private String name;

    public User() {}

    protected User(Parcel in) {
        id = in.readString();
        imageURL = in.readString();
        name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static User fromJSON(JSONObject obj) throws JSONException {
        User user = new User();
        user.id = obj.getString("id");
        user.imageURL = obj.getString("image_url");
        user.name = obj.getString("name");
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(imageURL);
        parcel.writeString(name);
    }

    public String getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }
}

package com.training.dle.androidtwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable{
    private String id;
    private String screenName;
    private String name;
    private String profileImageUrl;
    private String tagLine;
    private int followersCount = 0;
    private int followingCount = 0;

    public String getId() {
        return id;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagLine() {
        return tagLine;
    }


    public static User fromJson(JSONObject json){
        User user =  new User();
        try {
            user.id = json.getString("id_str");
            user.name = json.getString("name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.screenName = json.getString("screen_name");
            user.tagLine = json.getString("description");
            user.followersCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}

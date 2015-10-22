package com.training.dle.androidtwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String id;
    private String screenName;
    private String name;
    private String profileImageUrl;

    public String getId() {
        return id;
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

    public static User fromJson(JSONObject json){
        User user =  new User();
        try {
            user.id = json.getString("id_str");
            user.name = json.getString("name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.screenName = json.getString("screen_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}

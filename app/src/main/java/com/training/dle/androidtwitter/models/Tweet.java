package com.training.dle.androidtwitter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.Locale;
import java.text.ParseException;

public class Tweet {
    private String id;
    private String body;
    private User user;
    private String createdTime;

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public static Tweet fromJson(JSONObject json){
        Tweet tweet = new Tweet();
        try {
            tweet.id = json.getString("id_str");
            tweet.createdTime = json.getString("created_at");
            tweet.body = json.getString("text");
            tweet.user = User.fromJson( json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonarray){
        List<Tweet> tweets = new ArrayList<Tweet>();
        try {
            for (int i = 0; i < jsonarray.length(); i++) {
                Tweet t = Tweet.fromJson(jsonarray.getJSONObject(i));
                tweets.add(t);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        Collections.sort(tweets, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet lhs, Tweet rhs) {
                long id = Long.parseLong(lhs.id);
                long next = Long.parseLong(rhs.id);
                if (id > next){
                    return -1;
                }
                if (id < next){
                    return 1;
                }
                return 0;
            }
        });
        return tweets;
    }

    public Date getCreatedDate() throws ParseException {
        //Sat Oct 24 16:31:35 +0000 2015
        final String TWITTER = "EEE MMM dd HH:mm:ss Z yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);
        return sf.parse( this.getCreatedTime() );
    }
}

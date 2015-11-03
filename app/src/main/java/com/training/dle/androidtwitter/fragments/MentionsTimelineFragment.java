package com.training.dle.androidtwitter.fragments;

import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.training.dle.androidtwitter.TwitterClient;
import com.training.dle.androidtwitter.models.Tweet;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class MentionsTimelineFragment extends TweetsListFragment{

    protected void populateTimeline(long maxId){
        client.getMentionsTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                stopLoading = false;
                List<Tweet> tws = Tweet.fromJsonArray(response);
                if (tws.size() < TwitterClient.NUMBER_OF_ROWS){
                    stopLoading = true;
                }
                adapter.addAll(tws);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                stopLoading = true;
                Log.d("DEBUG", "MENTIONS TIMELINE FAIL:" + errorResponse.toString());
            }
        });
    }

}

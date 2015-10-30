package com.training.dle.androidtwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.training.dle.androidtwitter.ResultScrollListener;
import com.training.dle.androidtwitter.TwitterApplication;
import com.training.dle.androidtwitter.TwitterClient;
import com.training.dle.androidtwitter.models.Tweet;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserTimelineFragment extends TweetsListFragment{

    private TwitterClient client;
    private ResultScrollListener scrollListener;
    private boolean stopLoading = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollListener = new ResultScrollListener(8, 0) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (stopLoading){
                    return false;
                }
                populateTimeline(page);

                return true;
            }
        };
        lvTweets.setOnScrollListener(scrollListener);
    }

    public void refresh(){
        populateTimeline();
    }

    private void populateTimeline(){
        if (scrollListener != null) {
            Log.i("DEBUG", "scrolllllllllllll");
            scrollListener.reset();
        }
        adapter.clear();
        populateTimeline(0);
    }

    private void populateTimeline(int page){
        long maxId = TwitterClient.MAX_ID;
        int size = adapter.getCount();
        if (page > 0 && size > 0){
            Tweet t = adapter.getItem(size - 1);
            maxId = Long.parseLong(t.getId());
        }
        Log.i("DEBUG", "USER TIMELINE CALLLLLLING MAX: " + maxId);
        populateTimeline(maxId);
    }

    private void populateTimeline(long maxId){
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(maxId, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                stopLoading = false;
                adapter.addAll(Tweet.fromJsonArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                stopLoading = true;
                Log.d("DEBUG", "USER TIMELINE FAIL:" + errorResponse.toString());
            }
        });
    }

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragment.setArguments(args);
        return fragment;
    }

}

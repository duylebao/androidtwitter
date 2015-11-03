package com.training.dle.androidtwitter.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.training.dle.androidtwitter.ProfileActivity;
import com.training.dle.androidtwitter.R;
import com.training.dle.androidtwitter.ResultScrollListener;
import com.training.dle.androidtwitter.TweetsArrayAdapter;
import com.training.dle.androidtwitter.TwitterApplication;
import com.training.dle.androidtwitter.TwitterClient;
import com.training.dle.androidtwitter.models.Tweet;
import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment{
    protected List<Tweet> tweets;
    protected TweetsArrayAdapter adapter;
    protected ListView lvTweets;
    protected ResultScrollListener scrollListener;
    protected boolean stopLoading = false;
    protected TwitterClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView)v.findViewById(R.id.lvTweets);
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tweet tweet = (Tweet)parent.getItemAtPosition(position);
                String screenName = tweet.getUser().getScreenName();
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                i.putExtra("screen_name", screenName);
                startActivity(i);
            }
        });
        lvTweets.setAdapter(adapter);
        return v;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<Tweet>();
        adapter = new TweetsArrayAdapter(getActivity(), tweets);
        populateTimeline();
    }

    protected void populateTimeline(){
        if (scrollListener != null) {
            scrollListener.reset();
        }
        adapter.clear();
        populateTimeline(0);
    }

    public void refresh(){
        populateTimeline();
    }

    protected void populateTimeline(int page){
        long maxId = TwitterClient.MAX_ID;
        int size = adapter.getCount();
        if (page > 0 && size > 0){
            Tweet t = adapter.getItem(size - 1);
            maxId = Long.parseLong(t.getId());
        }
        populateTimeline(maxId);
    }

    protected abstract void populateTimeline(long maxId);

}

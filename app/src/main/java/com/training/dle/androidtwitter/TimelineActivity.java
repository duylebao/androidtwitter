package com.training.dle.androidtwitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.training.dle.androidtwitter.models.Tweet;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private List<Tweet> tweets;
    private TweetsArrayAdapter adapter;
    private ListView lvTweets;
    public static final int REQUEST_RESULT = 22212;
    private ResultScrollListener scrollListener;
    private boolean stopLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        lvTweets = (ListView)findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        adapter = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(adapter);
        client = TwitterApplication.getRestClient();
        populateTimeline();

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

    private void populateTimeline(){
        tweets.clear();
        populateTimeline(0);
    }

    private void populateTimeline(int page){
        long maxId = TwitterClient.MAX_ID;
        if (page > 0 && this.tweets.size() > 0){
            Tweet t = tweets.get( tweets.size() - 1);
            maxId = Long.parseLong(t.getId());
        }
        populateTimeline(maxId);
    }

    private void populateTimeline(long maxId){
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                stopLoading = false;
                tweets.addAll(Tweet.fromJsonArray(response));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                stopLoading = true;
                Log.d("DEBUG", "FAIL:" + errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnuCompose) {
            // instantiate an intent
            Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);

            // start activity
            startActivityForResult(i, REQUEST_RESULT);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_RESULT){
            populateTimeline();
        }
    }
}

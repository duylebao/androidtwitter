package com.training.dle.androidtwitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.training.dle.androidtwitter.fragments.TweetsListFragment;
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
    private ResultScrollListener scrollListener;
    public static final int REQUEST_RESULT = 22212;
    private boolean stopLoading = false;
    private TweetsListFragment tweetsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        if (savedInstanceState == null) {
            tweetsListFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        }
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
        tweetsListFragment.getTweetViewList().setOnScrollListener(scrollListener);
    }

    private void populateTimeline(){
        tweetsListFragment.getTweetAdapter().clear();
        populateTimeline(0);
    }

    private void populateTimeline(int page){
        long maxId = TwitterClient.MAX_ID;
        int size = tweetsListFragment.getTweetAdapter().getCount();
        if (page > 0 && size > 0){
            Tweet t = tweetsListFragment.getTweetAdapter().getItem(size - 1);
            maxId = Long.parseLong(t.getId());
        }
        populateTimeline(maxId);
    }

    private void populateTimeline(long maxId){
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                stopLoading = false;
                tweetsListFragment.getTweetAdapter().addAll(Tweet.fromJsonArray(response));
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

package com.training.dle.androidtwitter;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeActivity extends AppCompatActivity {
    private Button btnCancel;
    private Button btnTweet;
    private EditText tvComposeTweet;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnTweet = (Button)findViewById(R.id.btnTweetSubmit);
        tvComposeTweet = (EditText)findViewById(R.id.etComposeText);
        client = TwitterApplication.getRestClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public void onCancelSubmit(View view) {
        this.finish();
    }

    public void onTweetSubmit(View view) {
        // need to save to twitter
        // on success, call int to reload
        if (tvComposeTweet.getText().toString().isEmpty()){
            return;
        }

        client.tweet(tvComposeTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable){
                Log.i("DEBUG", "FAIL:" + response);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("DEBUG", "FAIL:" + errorResponse.toString());
            }
        });
    }
}

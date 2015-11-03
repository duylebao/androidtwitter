package com.training.dle.androidtwitter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.training.dle.androidtwitter.fragments.ProfileHeaderFragment;
import com.training.dle.androidtwitter.fragments.UserTimelineFragment;
import com.training.dle.androidtwitter.models.User;
import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private TwitterClient client;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // get screen name
        String screenName = getIntent().getStringExtra("screen_name");
        client = TwitterApplication.getRestClient();
        client.getUserInfo(screenName, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.fromJson(response);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                if (savedInstanceState == null){
                    UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(user.getScreenName());
                    // display fragment
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fl_container, userTimelineFragment);
                    ft.replace(R.id.fl_header_container, ProfileHeaderFragment.newInstance(user));
                    ft.commit();
                }
            }
        });
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

}

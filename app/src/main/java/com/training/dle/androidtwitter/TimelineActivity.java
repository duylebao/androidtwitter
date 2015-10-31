package com.training.dle.androidtwitter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.training.dle.androidtwitter.fragments.HomeTimelineFragment;
import com.training.dle.androidtwitter.fragments.MentionsTimelineFragment;
import com.training.dle.androidtwitter.fragments.TweetsListFragment;

public class TimelineActivity extends AppCompatActivity {
    public static final int REQUEST_RESULT = 22212;
    private TweetsPagerAdapter adapter;
    private ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        adapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager = (ViewPager)findViewById(R.id.viewpager);
        vpPager.setAdapter(adapter);

        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setViewPager(vpPager);
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
            adapter.notifyDataSetChanged();
        }
    }

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
        startActivity(i);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter{
        final String[] titles = {"Home","Mentions"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new HomeTimelineFragment();
            }else if (position == 1){
                return new MentionsTimelineFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof TweetsListFragment){
                ((TweetsListFragment)object).refresh();
            }
            return super.getItemPosition(object);
        }
    }
}

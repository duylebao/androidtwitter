package com.training.dle.androidtwitter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import com.squareup.picasso.Picasso;
import com.training.dle.androidtwitter.models.Tweet;

import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);
            viewHolder.username = (TextView)convertView.findViewById(R.id.tvUsername);
            viewHolder.profileImage = (ImageView)convertView.findViewById(R.id.ivImageProfile);
            viewHolder.body = (TextView)convertView.findViewById(R.id.tvBody);
            viewHolder.date = (TextView)convertView.findViewById(R.id.tvDate);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.body.setText(tweet.getBody());
        try {
            long time = tweet.getCreatedDate().getTime();
            String date = DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            viewHolder.date.setText( date );
        }catch(Exception e){
            e.printStackTrace();
        }
        // clear out photo view
        viewHolder.profileImage.setImageResource( android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.profileImage);
        viewHolder.username.setText(tweet.getUser().getScreenName());
        return convertView;
    }

    private static class ViewHolder {
        TextView username;
        ImageView profileImage;
        TextView body;
        TextView date;
    }
}

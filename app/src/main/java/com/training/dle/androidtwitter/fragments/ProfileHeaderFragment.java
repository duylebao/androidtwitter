package com.training.dle.androidtwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.training.dle.androidtwitter.R;
import com.training.dle.androidtwitter.models.User;

public class ProfileHeaderFragment extends Fragment{
    private TextView tvName;
    private TextView tvTagLine;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private ImageView ivProfileImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_header, container, false);
        tvName = (TextView)v.findViewById(R.id.tvFullName);
        tvTagLine = (TextView)v.findViewById(R.id.tvTagLine);
        tvFollowers = (TextView)v.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView)v.findViewById(R.id.tvFollowing);
        ivProfileImage = (ImageView)v.findViewById(R.id.ivProfileImage);
        populateProfileHeader();
        return v;
    }


    private void populateProfileHeader(){
        User user = (User)getArguments().getSerializable("user");
        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagLine());
        tvFollowers.setText(user.getFollowersCount() + " followers");
        tvFollowing.setText(user.getFollowingCount() + " following");
        Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    public static ProfileHeaderFragment newInstance(User user) {
        ProfileHeaderFragment fragment = new ProfileHeaderFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }
}

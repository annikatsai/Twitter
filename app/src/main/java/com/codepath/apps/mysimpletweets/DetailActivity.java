package com.codepath.apps.mysimpletweets;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) findViewById(R.id.tvBody);
        TextView tvTimeStamp = (TextView) findViewById(R.id.tvTimestamp);

        ivProfile.setImageResource(android.R.color.transparent); // clear out the old image for a recycled view
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfile);
        tvName.setText(tweet.getUser().getName());
        tvUserName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTimeStamp.setText(tweet.getDetailTimeStamp());
    }
}

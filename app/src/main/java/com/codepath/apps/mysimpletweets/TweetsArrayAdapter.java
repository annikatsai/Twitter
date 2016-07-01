package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

// Taking the Tweet objects and turning them into Views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {


    public TweetsArrayAdapter(Context context,List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // Override and setup custom template
    private static class ViewHolder {
        private TextView tvUserName;
        private TextView tvName;
        private ImageView ivProfileImage;
        private TextView tvBody;
        private TextView tvTimestamp;
        private ImageView ivRetweet;
        private ImageView ivFavorite;
    }

    // View Holder Pattern
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Get the tweet
        final Tweet tweet = getItem(position);
        // 2. Find or inflate the template
//        final ViewHolder mHolder;
        final ViewHolder mHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            mHolder = new ViewHolder();
            mHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            mHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            mHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            mHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            mHolder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            mHolder.ivFavorite = (ImageView) convertView.findViewById(R.id.ivFavorite);

            mHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    TwitterClient client = TwitterApplication.getRestClient();
                    if (tweet.getFavorited() == false) {
                        mHolder.ivFavorite.setImageResource(R.drawable.ic_favorited);
                        tweet.isFavorited = true;
                        client.favoriteTweet(tweet.getUid(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    } else {
                        mHolder.ivFavorite.setImageResource(R.drawable.ic_favorite);
                        tweet.isFavorited = false;
                        client.unfavoriteTweet(tweet.getUid(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    }
                }
            });
            mHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), FriendProfileActivity.class);
                    i.putExtra("screen_name", mHolder.tvUserName.getText().toString());
                    getContext().startActivity(i);
                }
            });

            mHolder.ivRetweet = (ImageView) convertView.findViewById(R.id.ivRetweet);
            mHolder.ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    TwitterClient client = TwitterApplication.getRestClient();
                    if (tweet.getRetweeted() == false) {
                        tweet.isRetweeted = true;
                        mHolder.ivRetweet.setImageResource(R.drawable.ic_retweeted);
                        client.retweetTweet(tweet.getUid(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    } else {
                        mHolder.ivRetweet.setImageResource(R.drawable.ic_retweet);
                        tweet.isRetweeted = false;
                        client.unretweetTweet(tweet.getUid(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    }
                }
            });

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        // 4. Populate data into subviews
        if (tweet.isFavorited)
            mHolder.ivFavorite.setImageResource(R.drawable.ic_favorited);
        else
            mHolder.ivFavorite.setImageResource(R.drawable.ic_favorite);

        if (tweet.isRetweeted)
            mHolder.ivRetweet.setImageResource(R.drawable.ic_retweeted);
        else
            mHolder.ivRetweet.setImageResource(R.drawable.ic_retweet);

        mHolder.tvName.setText(tweet.getUser().getName());
        mHolder.tvUserName.setText("@" + tweet.getUser().getScreenName());
        mHolder.tvBody.setText(tweet.getBody());
        mHolder.tvTimestamp.setText(tweet.getCreatedAt());
        mHolder.ivProfileImage.setImageResource(android.R.color.transparent); // clear out the old image for a recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(mHolder.ivProfileImage);
        // 5. Return the view to be inserted into the list
        return convertView;
    }
}


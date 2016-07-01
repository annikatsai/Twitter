package com.codepath.apps.mysimpletweets.models;

import android.text.format.DateUtils;

import com.codepath.apps.mysimpletweets.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

// Parse the JSON + Store the data, encapsulate state logic or display  logic
@Parcel
public class Tweet {

    // list out the attributes
    public String body;
    public long uid;   // unique id for the tweet
    public User user;  // store embedded user object
    public String createdAt;
    public String detailTimeStamp;

    public Boolean getFavorited() {
        return isFavorited;
    }

    public Boolean getRetweeted() {
        return isRetweeted;
    }

    public Boolean isFavorited;
    public Boolean isRetweeted;

    public Tweet() {}
    // Deserialize the JSON and build tweet objects
    // Tweet.fromJSON("{...}") => <Tweet>


    public String getBody() {
        return body;
    }

    public String getDetailTimeStamp() {return detailTimeStamp;}

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        // Extract the values from the json, store them
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = TimeFormatter.getTimeDifference(jsonObject.get("created_at").toString());
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.detailTimeStamp = TimeFormatter.getTimeStamp(jsonObject.get("created_at").toString());
            tweet.isFavorited = jsonObject.getBoolean("favorited");
            tweet.isRetweeted = jsonObject.getBoolean("retweeted");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Return the tweet object
        return tweet;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }


    // Tweet.fromJSONArray([{...},{...}]) => List<Tweet>
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        // Iterate the json array and create tweets
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        // Return the finished list
        return tweets;
    }
}

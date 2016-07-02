package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchTweetsFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the client
        client = TwitterApplication.getRestClient();    // singleton client
        populateTimeline(args.getString("q"));

    }

    static Bundle args;
    public static SearchTweetsFragment newInstance(String query) {
        SearchTweetsFragment searchFragment = new SearchTweetsFragment();
        args = new Bundle();
        args.putString("q", query);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    // Send API request to get timeline json
    // Fill the listview by creating the tweet objects from the json
    public void populateTimeline(String query) {
        //String query = getArguments().getString("q");
        client.searchTweets(query, new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", json.toString());
                JSONArray tweetResults = null;
                try {
                    aTweets.clear();
                    tweetResults = json.getJSONArray("statuses");
                    aTweets.addAll(Tweet.fromJSON(json).fromJSONArray(tweetResults));
                    aTweets.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
